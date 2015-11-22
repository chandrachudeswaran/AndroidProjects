//HW 5
//ParserUtil.java
//Chandra Chudeswaran Sankar, Melissa Krausse
package com.example.chandra.tedradiohourpodcast;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by chandra on 10/17/2015.
 */
public class ParserUtil {

    public static class XMLParser {


        public static ArrayList<Item> parseItems(InputStream in) throws XmlPullParserException, IOException {

            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in, "UTF-8");
            ArrayList<Item> itemsList = new ArrayList<>();
            Item items = null;

            int event = parser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {

                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")|| parser.getName().equals("channel")) {
                            items = new Item();
                        } else if (parser.getName().equals("title")) {
                            items.setTitle(parser.nextText());
                        }
                        else if(parser.getName().equals("description")){
                            items.setDescription(parser.nextText());
                        }
                        else if(parser.getName().equals("pubDate")){
                            items.setDate(parser.nextText());
                        }
                        else if(parser.getName().equals("itunes:image")){
                           items.setImage_url(parser.getAttributeValue(null,"href"));
                        }
                        else if(parser.getName().equals("itunes:duration")){
                            items.setDuration(parser.nextText());
                        }
                        else if(parser.getName().equals("enclosure")){
                            items.setAudio_url(parser.getAttributeValue(null,"url"));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            itemsList.add(items);
                        }

                }
                event = parser.next();
            }

            return itemsList;
        }
    }
}