/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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

package org.glassfish.resources.mail.admin.cli;

import org.glassfish.config.support.GlassFishConfigBean;
import org.glassfish.hk2.api.ServiceLocator;
import org.jvnet.hk2.config.ConfigModel;
import org.jvnet.hk2.config.Dom;
import org.jvnet.hk2.config.DomDocument;
import org.junit.Ignore;

import javax.xml.stream.XMLStreamReader;

/**
 *
 * This document will create the appropriate ConfigBean implementation but will
 * not save the modified config tree.
 *
 * User: Jerome Dochez
 */
@Ignore
public class TestDocument extends DomDocument<GlassFishConfigBean> {

    public TestDocument(ServiceLocator habitat) {
        super(habitat);
    }

    @Override
    public Dom make(final ServiceLocator habitat, XMLStreamReader xmlStreamReader, GlassFishConfigBean dom, ConfigModel configModel) {
        // by default, people get the translated view.
        return new GlassFishConfigBean(habitat, this, dom, configModel, xmlStreamReader);
    }
}
