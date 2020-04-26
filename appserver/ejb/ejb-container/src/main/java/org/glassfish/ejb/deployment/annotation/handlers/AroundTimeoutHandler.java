/*
 * Copyright (c) 1997, 2020 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.ejb.deployment.annotation.handlers;

import jakarta.interceptor.AroundTimeout;

import com.sun.enterprise.deployment.EjbInterceptor;
import com.sun.enterprise.deployment.annotation.context.EjbContext;
import com.sun.enterprise.deployment.annotation.context.EjbInterceptorContext;
import org.glassfish.apf.AnnotationHandlerFor;
import org.glassfish.apf.AnnotationInfo;
import org.glassfish.apf.AnnotationProcessorException;
import org.glassfish.apf.HandlerProcessingResult;
import org.glassfish.ejb.deployment.descriptor.EjbDescriptor;
import org.jvnet.hk2.annotations.Service;

/**
 * This handler is responsible for handling the javax.ejb.AroundTimeout attribute
 *
 */
@Service
@AnnotationHandlerFor(AroundTimeout.class)
public class AroundTimeoutHandler extends AroundInvokeHandler {
    
    public AroundTimeoutHandler() {
    }

    protected HandlerProcessingResult processAnnotation(AnnotationInfo ainfo,
            EjbContext[] ejbContexts) throws AnnotationProcessorException {

        for(EjbContext next : ejbContexts) {
            
            EjbDescriptor ejbDescriptor = 
                (EjbDescriptor) next.getDescriptor();

            ejbDescriptor.addAroundTimeoutDescriptor(
                getAroundInvocationDescriptor(ainfo));
        }

        return getDefaultProcessedResult();
    }

    protected HandlerProcessingResult processAnnotation(AnnotationInfo ainfo,
            EjbInterceptorContext ejbInterceptorContext)
            throws AnnotationProcessorException {

        EjbInterceptor ejbInterceptor =  ejbInterceptorContext.getDescriptor();

        ejbInterceptor.addAroundTimeoutDescriptor(
            getAroundInvocationDescriptor(ainfo));
            
        return getDefaultProcessedResult();
    }

}
