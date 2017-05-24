package com.konamgil.receiver.receiver;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by konamgil on 2017-05-24.
 */

public class XmlHelper {
    private String text;

    public XmlHelper() {;}

    /**
     * 파싱된 arraylist 객체를 반환합니다
     * @param xmldata
     * @return
     */
    public ArrayList getParsedData(String xmldata){
        return xmlParser(xmldata);
    }

    /**
     * String xml 형식의 문자열을 info 객체로 만들어줍니다
     * @param xmldata
     * @return
     */
    private ArrayList<info> xmlParser(String xmldata) {
        XmlPullParserFactory factory = null;
        XmlPullParser parser = null;

        ArrayList<info> arrayList = new ArrayList<info>();

        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);

            parser = factory.newPullParser();
            StringReader sr = new StringReader(xmldata);
            parser.setInput(sr);

            int eventType = parser.getEventType();
            info item = null;

            while(eventType != XmlPullParser.END_DOCUMENT) {
                String startTag = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:

                        if(startTag.equalsIgnoreCase("SMS")) {
                            item = new info();
                        }
                        if (startTag.equalsIgnoreCase("TimeCount")){
                            item.setTimeCount(Integer.parseInt(parser.nextText()));
                        }
                        if(startTag.equalsIgnoreCase("TimeKinds")) {
                            item.setTimeKinds(parser.nextText());
                        }
                        if(startTag.equalsIgnoreCase("RepeatCount")) {
                            item.setRepeatCount(Integer.parseInt(parser.nextText()));
                        }
                        if(startTag.equalsIgnoreCase("Message")) {
                            item.setMessage(parser.nextText());
                        }

                        break;
                    case XmlPullParser.END_TAG:

                        String endTag = parser.getName();
                        if(endTag.equals("SMS")) {
                            arrayList.add(item);
                        }
                        break;
                    case XmlPullParser.TEXT:
                        text = parser.getText();
                        break;

                }
                eventType = parser.next();
            }
        }catch(XmlPullParserException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arrayList;
    }
}
