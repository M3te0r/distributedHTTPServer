package org.esgi.service;

/**
 * Created by matpe on 30/05/2016.
 */
public interface IHTTPService {

    void serve(IHTTPRequest request, IHTTPResponse response);
}
