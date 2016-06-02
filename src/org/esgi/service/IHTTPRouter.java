package org.esgi.service;

import org.esgi.service.impl.HTTPMethod;

import java.util.regex.Pattern;

/**
 * Created by matpe on 02/06/2016.
 */


public interface IHTTPRouter {

    void addRoute(HTTPMethod method, Pattern pattern, IHTTPService service);

    void addRoute(HTTPMethod method, Pattern pattern, String[] values, IHTTPService service);

    IHTTPService resolve(IHTTPRequest request);
}
