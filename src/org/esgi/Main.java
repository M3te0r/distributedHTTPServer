package org.esgi;

import org.esgi.service.IHTTPServer;
import org.esgi.service.impl.HTTPServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {


        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(new File(Main.class.getResource("resources/conf.properties").getPath())));
            IHTTPServer server = new HTTPServer();
            server.configure(properties);
            server.run();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}