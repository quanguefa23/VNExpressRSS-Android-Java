package com.example.recyclerview.repository.remote;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Support class to parse XML data into specific elements
 */
class XMLDOMParser {
    Document getDocument(String xml) {
        Document document;
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = factory.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            is.setEncoding("UTF-8");
            document = db.parse(is);
        }
        catch (Exception e) {
            Log.e("Error: ", e.getMessage(), e);
            return null;
        }

        return document;
    }

    String getValue(Element item, String name) {
        NodeList nodes = item.getElementsByTagName(name);
        return this.getTextNodeValue(nodes.item(0));
    }

    String getValueSummary(Element item, String name) {
        NodeList nodes = item.getElementsByTagName(name);
        Node elem = nodes.item(0);
        Node child = elem.getFirstChild();
        return child.getNodeValue();
    }

    private String getTextNodeValue(Node elem) {
        Node child;
        if (elem != null) {
            if (elem.hasChildNodes()) {
                for (child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
                    if (child.getNodeType() == Node.TEXT_NODE  ){
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }
}

