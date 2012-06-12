/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.iso8583;

import com.solab.iso8583.IsoMessage;
import com.solab.iso8583.IsoValue;
import com.solab.iso8583.MessageFactory;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.api.transport.PropertyScope;
import org.mule.transformer.AbstractMessageTransformer;

import java.util.HashMap;

public class Iso8583ToMuleMessage extends AbstractMessageTransformer {

    private MessageFactory messageFactory;
    private int isoHeaderLength;

    @Override
    public Object transformMessage(MuleMessage muleMessage, String outputEncoding) throws TransformerException {
        IsoMessage isoMessage = null;
        HashMap<Integer, Field> fields = new HashMap<Integer, Field>();

        try {
            isoMessage = messageFactory.parseMessage(muleMessage.getPayloadAsBytes(), isoHeaderLength);
        } catch (Exception e) {
            throw new TransformerException(org.mule.config.i18n.MessageFactory.createStaticMessage(e.getMessage()), e);
        }

        for (int i = 2; i < 129; i++) {
            if (isoMessage.getField(i) != null) {
                fields.put(i, mapIsoValueToField(isoMessage.getField(i)));
            }
        }

        muleMessage.setPayload(fields);
        muleMessage.setProperty("iso8583.mti", isoMessage.getType(), PropertyScope.INBOUND);
        muleMessage.setProperty("iso8583.header", isoMessage.getIsoHeader(), PropertyScope.INBOUND);

        return muleMessage;
    }

    public MessageFactory getMessageFactory() {
        return messageFactory;
    }

    public void setMessageFactory(MessageFactory messageFactory) {
        this.messageFactory = messageFactory;
    }

    public int getIsoHeaderLength() {
        return isoHeaderLength;
    }

    public void setIsoHeaderLength(int isoHeaderLength) {
        this.isoHeaderLength = isoHeaderLength;
    }

    private Field mapIsoValueToField(IsoValue isoValue) throws TransformerException {
        FieldType fieldType;

        switch (isoValue.getType()) {

            case NUMERIC:
                fieldType = FieldType.NUMERIC;
                break;

            case ALPHA:
                fieldType = FieldType.ALPHA;
                break;

            case LLVAR:
                fieldType = FieldType.LLVAR;
                break;

            case LLLVAR:
                fieldType = FieldType.LLLVAR;
                break;

            case DATE10:
                fieldType = FieldType.DATE10;
                break;

            case DATE4:
                fieldType = FieldType.DATE4;
                break;

            case DATE_EXP:
                fieldType = FieldType.DATE_EXP;
                break;

            case TIME:
                fieldType = FieldType.TIME;
                break;

            case AMOUNT:
                fieldType = FieldType.AMOUNT;
                break;

            case BINARY:
                fieldType = FieldType.BINARY;
                break;

            case LLBIN:
                fieldType = FieldType.LLBIN;
                break;

            case LLLBIN:
                fieldType = FieldType.LLLBIN;
                break;

            default:
                throw new TransformerException(org.mule.config.i18n.MessageFactory.createStaticMessage("Could not find the field type for ISO type " + isoValue.getType()));
        }

        return new Field().setType(fieldType).setValue(isoValue.getValue()).setLength(isoValue.getLength());
    }

}
