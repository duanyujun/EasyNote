/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * 本软件为深圳康拓普开发研制。未经本公司正式书面同意，其他任何个人、团体不得使用、复制、
 * 修改或发布本软件.
 *****************************************************************************/

package com.comtop.easynote.utils;

/**
 * 键值对象
 * @author  comtop
 * @since   JDK1.6
 */
public final class KeyValue implements java.io.Serializable {

/**serialVersionUID */
    private static final long serialVersionUID = -1633045032079448946L;
    /**key */
    private String key;
    /** value*/
    private String value;
    /**comments */
    private String comments;

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int hashCode() {
        int iHashCode = 0;
        iHashCode += this.key.hashCode() + this.value.hashCode();

        return iHashCode;
    }

    public String toString() {
        final StringBuffer sbKeyValueBuffer = new StringBuffer(96);
        sbKeyValueBuffer.append("<------------------------------>" + "\n");
        sbKeyValueBuffer.append(" key=" + this.key + "\n");
        sbKeyValueBuffer.append(" value=" + this.value + "\n");
        sbKeyValueBuffer.append(" comments=" + this.comments + "\n");
        sbKeyValueBuffer.append("<------------------------------>" + "\n");
        return sbKeyValueBuffer.toString();
    }

    public boolean equals(Object object) {
        if (!(object instanceof KeyValue)) {
            return false;
        }
        KeyValue objCompareKeyValue = (KeyValue) object;
        if (this.key.equals(objCompareKeyValue.getKey())
                && this.value.equals(objCompareKeyValue.getValue())) {
            return true;
        }
        return false;
    }
}
