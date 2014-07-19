/******************************************************************************
 * Copyright (C) 2014 ShenZhen ComTop Information Technology Co.,Ltd
 * All Rights Reserved.
 * �����Ϊ���ڿ����տ������ơ�δ������˾��ʽ����ͬ�⣬�����κθ��ˡ����岻��ʹ�á����ơ�
 * �޸Ļ򷢲������.
 *****************************************************************************/

package com.comtop.easynote.utils;

/**
 * ��ֵ����
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
