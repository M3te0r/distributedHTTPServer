package org.esgi.service.impl;

import org.esgi.service.IHTTPRequest;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by matpe on 31/05/2016.
 */
public class HTTPRequest implements IHTTPRequest{

    private HashMap<String, String> HTTPHeaders;
    private String HTTPRequestVerb;
    private String HTTPRequestPath;
    private String HTTPRequestVersion;
    private String hostname;
    private BufferedReader bufferedReader;
    private InputStream inputStream;
    private byte[] body;
    private HashMap<String, String> parameters;

    public HTTPRequest(InputStream inputStream) {

        this.inputStream = inputStream;
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));


    }

    @Override
    public void read() throws IOException {
        String line = bufferedReader.readLine();
        String requests[] = line.split(" ");
        HTTPRequestVerb = requests[0];
        HTTPRequestPath = requests[1];
        HTTPRequestVersion = requests[2];
        HTTPHeaders = new HashMap<>();
        while (!(line = bufferedReader.readLine().trim()).isEmpty()){
            String header[] = line.split(":");
            HTTPHeaders.put(header[0].trim().toLowerCase(), header[1].trim());
        }


        String contentLength = HTTPHeaders.get("content-length");
        if(contentLength != null) {
            int size = Integer.valueOf(contentLength);
            if(size < 0)
                System.out.println("Malformed Content length");

            body = new byte[size];
            int i = 0;

            while(i < size) {
                body[i++] = (byte)bufferedReader.read();
            }
        }
        readHostname();
        readParameters();
    }

    private void readParameters() throws UnsupportedEncodingException {
        parameters = new HashMap<>();
        int paramStart = HTTPRequestPath.indexOf('?');
        if(paramStart > 0 && paramStart < HTTPRequestPath.length()) {

            String[] keyValues = URLDecoder.decode(HTTPRequestPath.substring(paramStart + 1), "UTF-8").split("&");

            for(String keyValue : keyValues) {
                String[] keyAndValue = keyValue.split("=");
                if(keyAndValue.length == 2)
                    parameters.put(keyAndValue[0], keyAndValue[1]);
            }
        }
    }

    @Override
    public String getMethod() {
        return HTTPRequestVerb;
    }

    @Override
    public String getLocation() {
        return HTTPRequestPath;
    }

    @Override
    public String getProtocol() {
        return HTTPRequestVersion;
    }

    @Override
    public String getHeader(String name) {
        return HTTPHeaders.get(name);
    }

    @Override
    public Map<String, String> getHeaders() {
        return HTTPHeaders;
    }

    @Override
    public Set<String> getHeadersNames() {
        return HTTPHeaders.keySet();
    }

    @Override
    public byte[] getBody() {
        return new byte[0];
    }

    @Override
    public Map<String, String> getParameters() {
        return  parameters;
    }

    private void readHostname() {
        hostname = HTTPHeaders.get("host");
        if(hostname != null) {
            int portPos = hostname.indexOf(":");
            if(portPos > 0)
                hostname = hostname.substring(0, portPos);
        }
    }

    @Override
    public String getHostname() {
        return hostname;
    }
}
