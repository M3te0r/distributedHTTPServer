package org.esgi.service;

import java.util.Properties;

/**
 * Created by matpe on 01/06/2016.
 */
public interface IHTTPServer {


    public void run();

    public void configure(Properties properties);

}
