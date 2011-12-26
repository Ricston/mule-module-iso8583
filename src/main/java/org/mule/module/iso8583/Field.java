/*
 * Copyright (c) Ricston Ltd.  All rights reserved.  http://www.ricston.com
 *
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.module.iso8583;

public class Field {

    private Object value;
    private FieldType type;
    private int length;

    public Object getValue() {
        return value;
    }

    public Field setValue(Object value) {
        this.value = value;
        return this;
    }

    public FieldType getType() {
        return type;
    }

    public Field setType(FieldType type) {
        this.type = type;
        return this;
    }

    public int getLength() {
        return length;
    }

    public Field setLength(int length) {
        this.length = length;
        return this;
    }
}
