package org.esgi.service.impl;

import org.esgi.service.IHTTPRequest;
import org.esgi.service.IHTTPResponse;
import org.esgi.service.IHTTPService;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by matpe on 02/06/2016.
 */

public class HTTPServiceStatic implements IHTTPService {


    private File folderPath;

    public HTTPServiceStatic(Map<String, String> parameters) throws IOException {
        this(parameters.get("path"));
    }

    public HTTPServiceStatic(String folderPath) throws IOException {
        this.folderPath = new File(folderPath);
        System.out.println(this.folderPath.getCanonicalPath());
        if(!this.folderPath.isDirectory())
            throw new IOException("The static directory " + this.folderPath.getAbsolutePath() + " does not exists or is not " +
                    "accessible");
    }

    @Override
    public void serve(IHTTPRequest request, IHTTPResponse response){
        // File
        String path = request.getLocation();
        File resource = new File(folderPath, path);

        if(!resource.exists())
            System.out.println("Error");
        else if(resource.isDirectory())
            showDirectory(resource, request, response);
        else
            uploadFile(resource, request, response);
    }

    private void uploadFile(File resource, IHTTPRequest request, IHTTPResponse response) {
        // Cache check
        Date modifiedSince = getDateFrom(request.getHeader("If-Modified-Since"));
        Date fileLastModified = new Date(resource.lastModified());

        if (modifiedSince != null && !fileLastModified.before(modifiedSince)) {
            response.setStatus(304);
        } else {
            response.setStatus(200);

            try {
                response.setBody(new FileInputStream(resource));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Set file information
        response.setHeader("Last-Modified", getFormattedDate(fileLastModified));
        response.setHeader("ContentType", URLConnection.guessContentTypeFromName(resource.getName()));
    }

    private void showDirectory(File resource, IHTTPRequest request, IHTTPResponse response) {
        StringBuilder sb = new StringBuilder();

        sb.append(
                "<html>" +
                        "<head>" +
                        "<title>" + resource.getName() + "</title>" +
                        "</head>" +
                        "<body>" +
                        "<h1>" + (resource.getName().isEmpty() ? "/" : resource.getName()) + "</h1>" +
                        getInner(resource.getParentFile(), resource) +
                        "</body>" +
                        "</html>");

        response.setHeader("ContentType", URLConnection.guessContentTypeFromName("text/html"));

        response.setBody(sb.toString());
    }

    private String getInner(File root, File dir) {
        StringBuilder sb = new StringBuilder();

        sb.append("<ul>");

        Path pathAbsolute = Paths.get(root.getAbsolutePath());

        for(File file : dir.listFiles()) {
            Path pathBase = Paths.get(file.getAbsolutePath());
            Path pathRelative = pathAbsolute.relativize(pathBase);

            if(file.isDirectory()) {
                sb.append("<li><a href=\"" + pathRelative.toString() + "\">" + file.getName() + "</a>");
                sb.append(getInner(root, file));
                sb.append("</li>");
            } else if (file.isFile()) {
                sb.append("<li><a href=\"" + pathRelative.toString() + "\">" + file.getName() + "</a></li>");
            }
        }

        sb.append("</ul>");
        return sb.toString();
    }

    public static String dateFormat = "EEE, dd MMM yyyy HH:mm:ss z";
    public static SimpleDateFormat dateFormatter;

    static {
        dateFormatter = new SimpleDateFormat(HTTPServiceStatic.dateFormat, Locale.US);
        dateFormatter.setTimeZone(TimeZone.getTimeZone("GMT"));
    }

    private Date getDateFrom(String date) {
        if(date == null)
            return null;

        try {
            return dateFormatter.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    private String getFormattedDate(long timestamp) {
        return getFormattedDate(new Date(timestamp));
    }

    private String getFormattedDate(Date date) {
        return dateFormatter.format(date);
    }
}
