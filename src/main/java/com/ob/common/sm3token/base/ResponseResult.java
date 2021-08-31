//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.ob.common.sm3token.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@JsonInclude(Include.NON_NULL)
public class ResponseResult<T> implements Serializable {
    private Date timestamp = new Date();
    private String id = UUID.randomUUID().toString();
    private String code = "000";
    private String message = "ok";
    private String commandName;
    private T data;
    private Map<String, Object> result = new HashMap();
    private String authToken = "";

    public ResponseResult() {
    }

    public ResponseResult(T data) {
        this.data = data;
    }

    public ResponseResult put(String name, Object obj) {
        this.result.put(name, obj);
        return this;
    }

    public void putException(DataException de) {
        this.code = de.getErrorCode();
        this.message = de.getErrorMessage();
    }

    public void putException(Exception e) {
        if (e instanceof DataException) {
            putException((DataException) e);
        } else {
            this.code = "-1";
            this.message = e.getMessage() + "," + e.toString();
        }
    }

    public <T> T toClassObject(Object obj, Class<T> valueType) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Object value = null;

        try {
            value = mapper.readValue(mapper.writeValueAsString(obj), valueType);
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return (T) value;
    }

    public String getId() {
        return this.id;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCommandName() {
        return this.commandName;
    }

    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    public Map<String, Object> getResult() {
        return this.result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseResult{timestamp=" + this.timestamp + ", id='" + this.id + '\'' + ", code='" + this.code + '\'' + ", message='" + this.message + '\'' + ", commandName='" + this.commandName + '\'' + ", data=" + this.data + ", result=" + this.result + ", authToken='" + this.authToken + '\'' + '}';
    }
}
