/*
 * Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
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

package cascadeDelete;

import javax.ejb.*;

/**
 * Created Dec 23, 2002 12:43:03 PM
 * Code generated by the Forte For Java EJB Builder
 * @author mvatkina
 */


public abstract class DBean implements javax.ejb.EntityBean {
    
    private javax.ejb.EntityContext context;
    
    
    /**
     * @see javax.ejb.EntityBean#setEntityContext(javax.ejb.EntityContext)
     */
    public void setEntityContext(javax.ejb.EntityContext aContext) {
        context=aContext;
    }
    
    
    /**
     * @see javax.ejb.EntityBean#ejbActivate()
     */
    public void ejbActivate() {
        
    }
    
    
    /**
     * @see javax.ejb.EntityBean#ejbPassivate()
     */
    public void ejbPassivate() {
        
    }
    
    
    /**
     * @see javax.ejb.EntityBean#ejbRemove()
     */
    public void ejbRemove() {
        System.out.println("Debug: D ejbRemove");
        
        cascadeDelete.LocalA a = getA();
        System.out.println("A: " + ((a==null)? "null" : a.getName()));
        
        cascadeDelete.LocalB b = getB();
        System.out.println("B: " + ((b==null)? "null" : b.getName()));
        
        cascadeDelete.LocalC c = getC();
        System.out.println("C: " + ((c==null)? "null" : c.getName()));
        
    }
    
    
    /**
     * @see javax.ejb.EntityBean#unsetEntityContext()
     */
    public void unsetEntityContext() {
        context=null;
    }
    
    
    /**
     * @see javax.ejb.EntityBean#ejbLoad()
     */
    public void ejbLoad() {
        
    }
    
    
    /**
     * @see javax.ejb.EntityBean#ejbStore()
     */
    public void ejbStore() {
        
    }
    
    public abstract java.lang.Integer getId();
    public abstract void setId(java.lang.Integer id);
    
    public abstract java.lang.String getName();
    public abstract void setName(java.lang.String name);
    
    public abstract cascadeDelete.LocalA getA();
    
    public abstract void setA(cascadeDelete.LocalA a);
    
    public abstract cascadeDelete.LocalB getB();
    
    public abstract void setB(cascadeDelete.LocalB b);
    
    public abstract cascadeDelete.LocalC getC();
    
    public abstract void setC(cascadeDelete.LocalC c);
    
    public java.lang.Integer ejbCreate(java.lang.Integer id, java.lang.String name) throws javax.ejb.CreateException {
        setId(id);
        setName(name);
        return null;
    }
    
    public void ejbPostCreate(java.lang.Integer id, java.lang.String name) throws javax.ejb.CreateException {
    }
    
}
