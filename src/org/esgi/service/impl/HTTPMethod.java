package org.esgi.service.impl;

/**
 * Created by matpe on 02/06/2016.
 */
public enum HTTPMethod {

    GET("GET"), POST("GET"), PUT("PUT"), DELETE("DELETE");

    private String method;

    HTTPMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }
}
