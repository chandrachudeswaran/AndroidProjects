package com.example.chandra.gallery500px;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;


/**
 * Created by chandra on 11/13/2015.
 */
public class RequestParams {


    String base_url;
    String method;
    HashMap<String, String> params = new HashMap<>();


    public RequestParams(String base_url, String method) {
        this.base_url = base_url;
        this.method = method;
    }

    public void addParams(String key, String value) {

        params.put(key, value);
    }

    public String getEncodeParams() {

        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()) {
            try {
                String value = URLEncoder.encode(params.get(key), "UTF-8");

                if (sb.length() > 0) {
                    sb.append("&");
                }
                sb.append(key + "=" + value);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        }
        return sb.toString();
    }

    public String getEncodedUrl() {
        return this.base_url + "?" + getEncodeParams();
    }

    public HttpURLConnection getConnection() throws IOException {

        if (method.equals("GET")) {

            URL url = new URL(getEncodedUrl());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            return con;
        } else {
            URL url = new URL(this.base_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(method);
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(getEncodeParams());
            writer.flush();
            return con;
        }
    }
}
