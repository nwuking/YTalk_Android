package com.nwuking.ytalk;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ParserByPULL {
    public static List<Face> getStudents(InputStream inStream) throws Throwable {
        List<Face> faces = null;
        Face mface = null;

        XmlPullParserFactory pullFactory = XmlPullParserFactory.newInstance();
        XmlPullParser parser = pullFactory.newPullParser();

        parser.setInput(inStream, "UTF-8");

        int eventType = parser.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {

                case XmlPullParser.START_DOCUMENT:
                    faces = new ArrayList<Face>();
                    break;

                case XmlPullParser.START_TAG:

                    String name = parser.getName();
                    if ("face".equals(name)) {

                        mface = new Face();

                        mface.setFaceid(parser.getAttributeValue(0));
                        mface.setFile(parser.getAttributeValue(1));
                        mface.setTip(parser.getAttributeValue(2));

                    }

                    break;

                case XmlPullParser.END_TAG:
                    //
                    if ("face".equals(parser.getName())) {
                        faces.add(mface);
                        mface = null;
                    }
                    break;
                default:
                    break;
            }
            eventType = parser.next();
        }
        return faces;
    }

}
