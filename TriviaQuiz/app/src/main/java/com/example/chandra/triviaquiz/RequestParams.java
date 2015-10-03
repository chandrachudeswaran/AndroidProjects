//Homework 3
//RequestParams.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.triviaquiz;

import android.graphics.Bitmap;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by chandra on 9/23/2015.
 */
public class RequestParams {

    String base_url;
    String method;

    HashMap<String, String> params = new HashMap<String, String>();


    public void addParams(String key, String value) {
        params.put(key, value);
    }



    public String addQuestion(String question,ArrayList<String> options,String url,int index){

        StringBuilder sb = new StringBuilder();
        sb.append(question);

        for(String answers:options){

            sb.append(";");
            sb.append(answers);
        }
        sb.append(";"+url);
        sb.append(";"+index+";");
        return sb.toString();
    }

    public RequestParams(String base_url, String method) {
        this.base_url = base_url;
        this.method = method;
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




    public HttpURLConnection getConnection()throws IOException {

        URL url = new URL(this.base_url);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);

        if(method.equals(TriviaAppConstants.METHOD_GET)){
            return con;
        }
        if (method.equals(TriviaAppConstants.METHOD_POST)) {
            con.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
            writer.write(getEncodeParams());
            writer.flush();
            return con;

        }
       return null;
    }


}
