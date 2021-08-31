//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ob.common.sm3token.base;

import java.text.MessageFormat;
import java.util.Arrays;

public class DataException extends RuntimeException {
    private String errorCode;
    private String errorMessage;
    private Object[] payload;
    private String[] formatMessage;

    public DataException(String errorCode, String errorMessage) {
        super(MessageFormat.format("*[{0}] {1}", errorCode, errorMessage));
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public DataException(String errorCode, String pattern, String[] formatMessage, Object... payload) {
        super(MessageFormat.format("*[{0}] {1} {2}", errorCode, MessageFormat.format(pattern, formatMessage), Arrays.toString(payload)));
        this.errorCode = errorCode;
        this.errorMessage = MessageFormat.format(pattern, formatMessage);
        this.formatMessage = formatMessage;
        this.payload = payload;
    }

    public String getErrorCode() {
        return this.errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        if (this.formatMessage != null) {
            this.errorMessage = MessageFormat.format(errorMessage, this.formatMessage);
        } else {
            this.errorMessage = errorMessage;
        }

    }

    public Object[] getPayload() {
        return this.payload;
    }

    public void setPayload(Object[] payload) {
        this.payload = payload;
    }

    public String toString() {
        return "DataException{errorCode=" + this.errorCode + ", errorMessage='" + this.errorMessage + '\'' + ", payload=" + Arrays.toString(this.payload) + '}';
    }
}
