package org.esgi.service.impl;

import org.esgi.service.IHTTPHostRouter;
import org.esgi.service.IHTTPRouter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by matpe on 02/06/2016.
 */

public class HTTPHostRouter implements IHTTPHostRouter {

    private Map<String, IHTTPRouter> routes = new HashMap<>();

    @Override
    public void addRoute(String hostname, IHTTPRouter router) {
        routes.put(hostname, router);
    }

    @Override
    public IHTTPRouter resolve(String hostname) {
        IHTTPRouter router = routes.get(hostname);

        if(router == null)
            router = routes.get("*"); // Try default fallback

        return router;
    }
}
