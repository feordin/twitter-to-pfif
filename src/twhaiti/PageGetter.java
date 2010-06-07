package twhaiti;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

import org.ccil.cowan.tagsoup.HTMLSchema;
import org.ccil.cowan.tagsoup.Parser;
import org.ccil.cowan.tagsoup.XMLWriter;


import org.w3c.dom.Document;

import org.xml.sax.InputSource;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class PageGetter{
	
	public static Document getDocument(String url)
		throws IOException
		,ParserConfigurationException
		,SAXException
	{
		HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
		con.setRequestMethod("GET");
		con.setDoInput(true);
		con.connect();
		con.disconnect();
		
		if(con.getResponseCode() != 200){
			System.err.println("Not expected status code "+con.getResponseCode());
			return null;
		}
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		XMLReader parser = new Parser();
		parser.setProperty(Parser.schemaProperty, new HTMLSchema());
		ContentHandler contentHandler = new XMLWriter(
			new OutputStreamWriter(out,"UTF-8")
		);
		parser.setContentHandler(contentHandler);
		
		InputSource inputSource = new InputSource(con.getInputStream());
		inputSource.setEncoding("UTF-8");
		parser.parse(inputSource);
		
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = dbFactory.newDocumentBuilder();
		InputStream in = new ByteArrayInputStream(out.toByteArray());
		return builder.parse(in);
	}
	
}