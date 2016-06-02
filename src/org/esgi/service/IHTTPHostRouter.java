package org.esgi.service;

/**
 * Created by matpe on 02/06/2016.
 */
public interface IHTTPHostRouter {
    public void addRoute(String hostname, IHTTPRouter router);

    public IHTTPRouter resolve(String hostname);
}
