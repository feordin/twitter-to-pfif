package twhaiti;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author chirags
 * A collection of utility methods for PFIF
 */
public class PFIF {

    /**
     *  Convert Twitter to PFIF
     */
    public String twitterToPFIF(JSONArray twitterArray) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\"?>");
        sb.append("<pfif:pfif xmlns:pfif=\"http://zesty.ca/pfif/1.1\">");
        
        JSONObject twitterObj;
        for(int i=0; i<twitterArray.length(); i++) {
            try {
                twitterObj = twitterArray.getJSONObject(i);
                sb.append(convertObjectToPerson(twitterObj));
            } catch (Exception e) {
                System.err.println("Unable to convert a twitter object to a person object");
                // Continue..
            }
        }
        
        sb.append("</pfif:pfif>");
        return sb.toString();
    }
    
    /*
     * PFIF Spec: http://zesty.ca/pfif/1.1/
     * Twitter API: http://apiwiki.twitter.com/Twitter-API-Documentation
     */
    private String convertObjectToPerson(JSONObject twitterObj) throws Exception {        
        StringBuilder sb = new StringBuilder()
          .append("<pfif:person>")
          .append("<pfif:first_name>Twitter</pfif:first_name>")
          .append("<pfif:last_name>Twitter</pfif:last_name>")
          .append("<pfif:photo_url></pfif:photo_url>")
          
          .append("<pfif:home_street></pfif:home_street>")
          .append("<pfif:home_neighborhood></pfif:home_neighborhood>")
          .append("<pfif:home_city></pfif:home_city>")
          .append("<pfif:home_state></pfif:home_state>")
          .append("<pfif:home_zip></pfif:home_zip>")
          
          .append("<pfif:author_name>" + twitterObj.getString("from_user") + "</pfif:author_name>")
          .append("<pfif:author_email></pfif:author_email>") // Not Available from Twitter
          .append("<pfif:author_phone></pfif:author_phone>") // Not Available from Twitter
          .append("<pfif:source_date></pfif:source_date>")
          .append("<pfif:source_name>http://twitter.com/" + twitterObj.getString("from_user") + "</pfif:source_name>")
          .append("<pfif:source_url>http://twitter.com/" + twitterObj.getString("from_user") + "/status/" + twitterObj.getString("id") + "</pfif:source_url>")
          
          .append("<pfif:entry_date></pfif:entry_date>")
          .append("<pfif:person_record_id>twitter-haiti.appspot.com/" + twitterObj.getString("id") + "</pfif:person_record_id>")
          .append("<pfif:other>" + twitterObj.getString("text") +"</pfif:other>")
          .append("</pfif:person>");
        return sb.toString();
    }

}
