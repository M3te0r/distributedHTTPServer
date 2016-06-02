package org.esgi.service.impl;

import org.esgi.service.IHTTPRequest;
import org.esgi.service.IHTTPRouter;
import org.esgi.service.IHTTPService;

import java.util.ArrayList;
import java.util.List;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by matpe on 02/06/2016.
 */
public class HTTPRouter implements IHTTPRouter {

    private List<HttpRoute> routes = new ArrayList<>();

    @Override
    public void addRoute(HTTPMethod method, Pattern pattern, IHTTPService service) {
        routes.add(new HttpRoute(method, pattern, new String[0], service));
    }

    @Override
    public void addRoute(HTTPMethod method, Pattern pattern, String[] values, IHTTPService service) {
        routes.add(new HttpRoute(method, pattern, values, service));
    }

    @Override
    public IHTTPService resolve(IHTTPRequest request) {
        for(HttpRoute route : routes) {
            if(!request.getMethod().equals(route.method.getMethod()))
                continue;

            Matcher m = route.pattern.matcher(request.getLocation());

            if(m.find()) {
                int count = m.groupCount();

                for(int i = 1; i <= count && i <= route.values.length; i++) {
                    request.getParameters().put(route.values[i - 1], m.group(i));
                }

                return route.service;
            }
        }

        return null; // Not found
    }

    private class HttpRoute {
        public HTTPMethod method;
        public Pattern pattern;
        public IHTTPService service;
        public String[] values;

        public HttpRoute(HTTPMethod method, Pattern pattern, String[] values, IHTTPService service) {
            this.method = method;
            this.pattern = pattern;
            this.values = values;
            this.service = service;
        }
    }
}
