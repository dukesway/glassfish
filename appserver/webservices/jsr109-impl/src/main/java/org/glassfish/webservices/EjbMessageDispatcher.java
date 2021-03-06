/*
 * Copyright (c) 1997, 2018 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.webservices;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletContext;

/**
 * Common interface between jaxrpc 1.1 and jax-ws 2.0 for dispatching EJB
 * endpoint messages. 
 *
 * @author Bhakti Mehta
 */
public interface EjbMessageDispatcher {
    

    public void invoke(HttpServletRequest req, 
                       HttpServletResponse resp,
                       ServletContext ctxt,
                       EjbRuntimeEndpointInfo endpointInfo); 
}
