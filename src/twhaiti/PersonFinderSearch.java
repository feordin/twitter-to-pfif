package twhaiti;

import java.net.URLEncoder;

import javax.annotation.Resource;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class PersonFinderSearch  {

    @Resource(name = "maxResults")
    private Integer maxResults = 800;

    public static void main(String[] args) throws Exception {  	
    	
    	PersonFinderSearch s = new PersonFinderSearch();
    	int count = s.getCount("pedro fuentes", "http://haiticrisis.appspot.com/results?role=seek&query=%s");
        System.out.println(count);
    }

    public int getCount(String query, String queryUrl)
            throws Exception {
    	int count = 0;

        try {
            String url = String.format(
                    queryUrl, URLEncoder.encode(query, "UTF-8"));

            Document document = PageGetter.getDocument(url);
            XPath xPath = XPathFactory.newInstance().newXPath();

            NodeList nodesEntries = (NodeList) xPath.evaluate(".//li[@class='resultItem']", document, XPathConstants.NODESET);
            if (nodesEntries.getLength() < maxResults)
            	count = nodesEntries.getLength();
            else
            	count = maxResults;

        } catch (Exception exception) {
            throw new Exception(exception);
        }
        return count;
    }
}
