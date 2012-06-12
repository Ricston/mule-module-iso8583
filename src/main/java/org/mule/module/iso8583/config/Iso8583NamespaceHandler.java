/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.iso8583.config;

import org.mule.config.spring.parsers.specific.MessageProcessorDefinitionParser;
import org.mule.module.iso8583.Iso8583ToMuleMessage;
import org.mule.module.iso8583.MuleMessageToIso8583;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Registers a Bean Definition Parser for handling elements defned in META-INF/mule-iso8583.xsd
 *
 */
public class Iso8583NamespaceHandler extends NamespaceHandlerSupport
{
    public void init()
    {
        registerBeanDefinitionParser("message-to-iso8583-transformer", new MessageProcessorDefinitionParser(MuleMessageToIso8583.class));
        registerBeanDefinitionParser("iso8583-to-message-transformer", new MessageProcessorDefinitionParser(Iso8583ToMuleMessage.class));
    }
}
