/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.iso8583;

import org.junit.Test;
import org.mule.DefaultMuleMessage;
import org.mule.api.MuleMessage;
import org.mule.api.client.MuleClient;
import org.mule.tck.junit4.FunctionalTestCase;

import static org.junit.Assert.assertArrayEquals;

public class Iso8583TestCase extends FunctionalTestCase {

    // [Header, MTI = 0800, Bitmap, Field 7 = 1226124404, field 11 = 102, Field 70 = 301]
    private static final byte[] ECHO_REQUEST = { 0x30, 0x30, 0x35, 0x35, 0x30, 0x38, 0x30, 0x30, 0x38, 0x32, 0x32, 0x30, 0x30,
                                                 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x34,
                                                 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30,
                                                 0x30, 0x31, 0x32, 0x32, 0x36, 0x31, 0x32, 0x34, 0x34, 0x30, 0x34, 0x30, 0x30,
                                                 0x30, 0x31, 0x30, 0x32, 0x33, 0x30, 0x31 };

    // [Header, MTI = 0810, Bitmap, Field 7 = 1226124404, field 11 = 102, Field 39 = 00, Field 70 = 301]
    private static final byte[] ECHO_REPLY =  { 0x30, 0x30, 0x35, 0x35, 0x30, 0x38, 0x31, 0x30, 0x38, 0x32, 0x32, 0x30, 0x30,
                                                0x30, 0x30, 0x30, 0x30, 0x32, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x34,
                                                0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30, 0x30,
                                                0x30, 0x31, 0x32, 0x32, 0x36, 0x31, 0x32, 0x34, 0x34, 0x30, 0x34, 0x30, 0x30,
                                                0x30, 0x31, 0x30, 0x32, 0x30, 0x30, 0x33, 0x30, 0x31 };

    protected String getConfigResources() {
        return "iso8583-functional-test-config.xml";
    }

    @Test
    public void testIso8583Service() throws Exception {
        MuleClient client = muleContext.getClient();
        MuleMessage result = client.send("vm://iso8583.service", new DefaultMuleMessage(ECHO_REQUEST, muleContext));
        assertArrayEquals(ECHO_REPLY, result.getPayloadAsBytes());
    }

}
