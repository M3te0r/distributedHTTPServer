package org.esgi.service.impl;

import org.esgi.service.IHTTPRequest;
import org.esgi.service.IHTTPResponse;
import org.esgi.service.IHTTPService;
import java.net.URLConnection;
import java.io.*;

/**
 * Created by matpe on 31/05/2016.
 */
public class HTTPService implements IHTTPService {

    @Override
    public void serve(IHTTPRequest request, IHTTPResponse response) {
        String location = request.getLocation();

        File newFile = new File("C:\\Users\\matpe\\IdeaProjects\\public", "index.html");
        try {
            BufferedReader f = new BufferedReader(new InputStreamReader(new FileInputStream(newFile)));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = f.readLine()) != null){
                sb.append(line);
            }
            response.setBody(sb.toString());
            response.setHeader("ContentType", URLConnection.guessContentTypeFromName(newFile.getName()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
