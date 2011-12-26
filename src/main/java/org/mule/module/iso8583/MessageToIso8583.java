/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.iso8583;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoType;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.MessageFactory;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;

import java.util.Map;

public class MessageToIso8583 extends AbstractMessageTransformer {

    private MessageFactory messageFactory;

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    public void setMessageFactory(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    private IsoValue mapFieldToIsoValue(Map.Entry<Integer, Field> field) throws TransformerException {
        IsoType isoType;

        switch (field.getValue().getType()) {
            case NUMERIC:
                isoType = IsoType.NUMERIC;
                break;

            case ALPHA:
                isoType = IsoType.ALPHA;
                break;

            case LLVAR:
                isoType = IsoType.LLVAR;
                break;

            case LLLVAR:
                isoType = IsoType.LLLVAR;
                break;

            case DATE10:
                isoType = IsoType.DATE10;
                break;

            case DATE4:
                isoType = IsoType.DATE4;
                break;

            case DATE_EXP:
                isoType = IsoType.DATE_EXP;
                break;

            case TIME:
                isoType = IsoType.TIME;
                break;

            case AMOUNT:
                isoType = IsoType.AMOUNT;
                break;

            case BINARY:
                isoType = IsoType.BINARY;
                break;

            case LLBIN:
                isoType = IsoType.LLBIN;
                break;

            case LLLBIN:
                isoType = IsoType.LLLBIN;
                break;

            default:
                throw new TransformerException(org.mule.config.i18n.MessageFactory.createStaticMessage("Could not find the ISO type for field type " + field.getValue().getType()));
        }

        return new IsoValue<String>(isoType, field.getValue().getValue().toString(), field.getValue().getLength());
    }

    @Override
    public Object transformMessage(MuleMessage muleMessage, String outputEncoding) throws TransformerException {
        Map<Integer, Field> payload = (Map<Integer, Field>) muleMessage.getPayload();
        IsoMessage isoMessage;
        String isoHeader;

        isoMessage = messageFactory.newMessage(Integer.parseInt(muleMessage.<String>getOutboundProperty("iso8583.mti"), 16));
        isoHeader = muleMessage.<String>getOutboundProperty("iso8583.header");

        if (isoHeader != null) {
            isoMessage.setIsoHeader(isoHeader);
        }

        for (Map.Entry<Integer, Field> entry : payload.entrySet()) {
            if (entry.getValue() != null) {
                isoMessage.setField(entry.getKey(), mapFieldToIsoValue(entry));
            }
        }

        muleMessage.setPayload(isoMessage.writeData());

        return muleMessage;
    }
}
