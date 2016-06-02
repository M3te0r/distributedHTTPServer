package org.esgi.service.impl;

import org.esgi.pooler.ThreadPool;
import org.esgi.service.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.net.ServerSocket;
import java.net.Socket;

import java.util.*;
import java.util.logging.Level;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by matpe on 01/06/2016.
 */
public class HTTPServer implements IHTTPServer {

    private IHTTPHostRouter hostRouter;

    private int port = 8080;

    @Override
    public void run() {
        try(ServerSocket server = new ServerSocket(port)) {
            server.setReceiveBufferSize(1024);
            String host = "localhost";
            System.out.println("Server listening on " + port);
            ThreadPool pool = new ThreadPool(10);
            while (true){
                Socket client = server.accept();
                pool.submitTask(() -> handleRequest(client));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleRequest(Socket client){

        OutputStream out;
        InputStream in;
        try {
            out = client.getOutputStream();
            in = client.getInputStream();

            IHTTPRequest request = new HTTPRequest(in);
            IHTTPResponse response = new HTTPResponse(request, out);
            request.read();
            // Get host router for the hostname
            IHTTPRouter router = hostRouter.resolve(request.getHostname());
            if(router == null) // If no route is found, 404
                System.out.println("404");

            // Route the request
            IHTTPService service = router.resolve(request);
            if(service == null) // If no route is found, 404
                System.out.println("404");

            // Perform the operation
            service.serve(request, response);

            // Write the response
            response.write();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void configure(Properties properties) {

        hostRouter = new HTTPHostRouter();

        try {
            // Get all the virtual host names
            String[] vhostsNames = ((String) properties.getOrDefault("vhosts", "*")).split(",");

            // For each virtual host, get all its information
            for(String vhostName : vhostsNames) {
                IHTTPRouter router = new HTTPRouter();

                vhostName = vhostName.trim();

                // Host information
                String hostname = (String) properties.getOrDefault(vhostName + ".path", "*");
                int port = Integer.valueOf(((String) properties.getOrDefault(vhostName + ".port", "80")));

                // Services of the virtual host
                String[] servicesNames = ((String) properties.getOrDefault(vhostName + ".services", "*")).split(",");

                // For each service, load the provided class
                for(String serviceName : servicesNames) {
                    String servicePropertyKey = vhostName + ".services." + serviceName.trim();

                    // Get the hostname
                    String route = (String) properties.getOrDefault(servicePropertyKey + ".route", "");

                    String method = (String) properties.getOrDefault(servicePropertyKey + ".method", "");

                    // Get the service class name
                    String className = (String) properties.getOrDefault(servicePropertyKey + ".class", "");

                    if(!className.isEmpty()) {
                        Class<IHTTPService> serviceClass = (Class<IHTTPService>) Class.forName(className);
                        Constructor<?> constructor = serviceClass.getConstructor(Map.class);

                        Map<String, String> serviceParameters = new HashMap<>();
                        List<?> serviceParameterKeys = Collections.list(properties.propertyNames()).stream().
                                filter(key -> ((String) key).startsWith(servicePropertyKey)).collect(Collectors.toList());

                        // Get all the properties defined for the service, and give it to the service constructor
                        for(Object serviceParameterKey : serviceParameterKeys) {
                            String shortKey = ((String) serviceParameterKey).substring(servicePropertyKey.length() + 1);
                            serviceParameters.put(shortKey, (String) properties.getOrDefault(serviceParameterKey, ""));
                        }

                        router.addRoute(
                                HTTPMethod.valueOf(method),  // Method
                                Pattern.compile(route),      // Regex path
                                new String[0],               // Capture groups
                                (IHTTPService) constructor.newInstance(serviceParameters)); // Service
                    }
                }

                hostRouter.addRoute(hostname, router);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


