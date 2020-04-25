/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

package org.glassfish.cdi.transaction;

import jakarta.enterprise.context.ContextNotActiveException;
import jakarta.enterprise.context.spi.Context;
import jakarta.enterprise.context.spi.Contextual;
import jakarta.enterprise.context.spi.CreationalContext;
import jakarta.enterprise.inject.spi.PassivationCapable;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import jakarta.transaction.Status;
import jakarta.transaction.TransactionScoped;
import jakarta.transaction.TransactionSynchronizationRegistry;
import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Context implementation for obtaining contextual instances of {@link TransactionScoped} beans.
 * <p/>
 * The contextual instances are destroyed when the transaction completes.
 * <p/>
 * Any attempt to call a method on a {@link TransactionScoped} bean when a transaction is not active will result in a
 * {@Link javax.enterprise.context.ContextNotActiveException}.
 *
 * A CDI Event: @Initialized(TransactionScoped.class) is fired with {@link TransactionScopedCDIEventPayload}, when the context is initialized for
 * the first time and @Destroyed(TransactionScoped.class) is fired with {@link TransactionScopedCDIEventPayload}, when the context is destroyed at
 * the end. Currently this payload is empty i.e. it doesn't contain any information.
 *
 * @author <a href="mailto:j.j.snyder@oracle.com">JJ Snyder</a>
 * @author <a href="mailto:arjav.desai@oracle.com">Arjav Desai</a>
 */
public class TransactionScopedContextImpl implements Context {
    public static final String TRANSACTION_SYNCHRONIZATION_REGISTRY_JNDI_NAME = "java:comp/TransactionSynchronizationRegistry";

    ConcurrentHashMap<TransactionSynchronizationRegistry,Set<TransactionScopedBean>> beansPerTransaction;

    public TransactionScopedContextImpl(){
        beansPerTransaction = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<TransactionSynchronizationRegistry, Set<TransactionScopedBean>> getBeansPerTransaction() {
        return beansPerTransaction;
    }

    @Override
    public Class<? extends Annotation> getScope() {
        return TransactionScoped.class;
    }

    @Override
    public <T> T get(Contextual<T> contextual, CreationalContext<T> creationalContext) {
        TransactionSynchronizationRegistry transactionSynchronizationRegistry = getTransactionSynchronizationRegistry();
        Object beanId = getContextualId(contextual);
        T contextualInstance = getContextualInstance(beanId, transactionSynchronizationRegistry);
        if (contextualInstance == null) {
            contextualInstance = createContextualInstance(contextual, beanId, creationalContext, transactionSynchronizationRegistry);
        }

        return contextualInstance;
    }

    @Override
    public <T> T get(Contextual<T> contextual) {
        TransactionSynchronizationRegistry transactionSynchronizationRegistry = getTransactionSynchronizationRegistry();
        Object beanKey = getContextualId(contextual);
        return getContextualInstance(beanKey, transactionSynchronizationRegistry);
    }

    @Override
    /**
     * Determines if this context object is active.
     *
     * @return true if there is a current global transaction and its status is
     *              {@Link javax.transaction.Status.STATUS_ACTIVE}
     *         false otherwise
     */
    public boolean isActive() {
        try {
            //Just calling it but not checking for != null on return value as its already done inside method
            getTransactionSynchronizationRegistry();
            return true;
        } catch (ContextNotActiveException ignore) {
        }
        return false;
    }

    private Object getContextualId(Contextual<?> contextual) {
        if (contextual instanceof PassivationCapable) {
            PassivationCapable passivationCapable = (PassivationCapable) contextual;
            return passivationCapable.getId();
        } else {
            return contextual;
        }
    }

    private <T> T createContextualInstance(Contextual<T> contextual,
                                           Object contextualId,
                                           CreationalContext<T> creationalContext,
                                           TransactionSynchronizationRegistry transactionSynchronizationRegistry) {
        TransactionScopedBean<T> transactionScopedBean = new TransactionScopedBean<T>(contextual, creationalContext,this);
        transactionSynchronizationRegistry.putResource(contextualId, transactionScopedBean);
        transactionSynchronizationRegistry.registerInterposedSynchronization(transactionScopedBean);
        //Adding TransactionScopedBean as Set, per transactionSynchronizationRegistry, which is unique per transaction
        //Setting synchronizedSet so that even is there are multiple transaction for an app its safe
        Set<TransactionScopedBean> transactionScopedBeanSet = beansPerTransaction.get(transactionSynchronizationRegistry);
        if (transactionScopedBeanSet != null){
            transactionScopedBeanSet = Collections.synchronizedSet(transactionScopedBeanSet);
        } else {
            transactionScopedBeanSet = Collections.synchronizedSet(new HashSet());
            //Fire this event only for the first initialization of context and not for every TransactionScopedBean in a Transaction
            TransactionScopedCDIUtil.fireEvent(TransactionScopedCDIUtil.INITIALIZED_EVENT);
            //Adding transactionScopedBeanSet in Map for the first time for this transactionSynchronizationRegistry key
            beansPerTransaction.put(transactionSynchronizationRegistry,transactionScopedBeanSet);
        }
        transactionScopedBeanSet.add(transactionScopedBean);
        //Not updating entry in main Map with new TransactionScopedBeans as it should happen by reference
        return transactionScopedBean.getContextualInstance();
    }

    private <T> T getContextualInstance(Object beanId, TransactionSynchronizationRegistry transactionSynchronizationRegistry) {
        Object obj = transactionSynchronizationRegistry.getResource(beanId);
        TransactionScopedBean<T> transactionScopedBean = (TransactionScopedBean<T>) obj;
        if (transactionScopedBean != null) {
            return transactionScopedBean.getContextualInstance();
        }
        return null;
    }

    private TransactionSynchronizationRegistry getTransactionSynchronizationRegistry() {
        TransactionSynchronizationRegistry transactionSynchronizationRegistry;
        try {
            InitialContext initialContext = new InitialContext();
            transactionSynchronizationRegistry =
                    (TransactionSynchronizationRegistry) initialContext.lookup(TRANSACTION_SYNCHRONIZATION_REGISTRY_JNDI_NAME);
        } catch (NamingException ne) {
            throw new ContextNotActiveException("Could not get TransactionSynchronizationRegistry", ne);
        }

        int status = transactionSynchronizationRegistry.getTransactionStatus();
        if (status == Status.STATUS_ACTIVE ||
                status == Status.STATUS_MARKED_ROLLBACK ||
                status == Status.STATUS_PREPARED ||
                status == Status.STATUS_UNKNOWN ||
                status == Status.STATUS_PREPARING ||
                status == Status.STATUS_COMMITTING ||
                status == Status.STATUS_ROLLING_BACK) {
            return transactionSynchronizationRegistry;
        }

        throw new ContextNotActiveException("TransactionSynchronizationRegistry status is not active.");
    }

}
