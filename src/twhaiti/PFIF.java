package twhaiti;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author chirags
 * A collection of utility methods for PFIF
 * 
 * Possible enhancements would be to create java classes based
 * upon the pfif XSD, JAXB could be used for this and then easily
 * modified
 * 
 * 
 */
public class PFIF {

    /**
     *  Convert Twitter to PFIF
     */
    public String twitterToPFIF(JSONArray twitterArray) {
        StringBuilder sb = new StringBuilder();
        String searchedPerson = "";
        sb.append("<?xml version=\"1.0\"?>");
        sb.append("<pfif:pfif xmlns:pfif=\"http://zesty.ca/pfif/1.1\">");
        
        JSONObject twitterObj;
        for(int i=0; i<twitterArray.length(); i++) {
            try {
                twitterObj = twitterArray.getJSONObject(i);
                sb.append(convertObjectToPerson(twitterObj));
                
                //Here we are also replying to the original tweet with some info
                // on person finder, this should be refactored to a new location
                // and to allow for different person finder URLs
                
                PersonFinderSearch pfs = new PersonFinderSearch();
                searchedPerson = getName(twitterObj.getString("text"));
                int count = pfs.getCount(searchedPerson, "http://haiticrisis.appspot.com/results?role=seek&query=%s");
                TwitterReply tr = new TwitterReply();
                tr.replyToTweet(twitterObj, "feordin", "tempPassword", count, searchedPerson);
                
            } catch (Exception e) {
                System.err.println("Unable to convert a twitter object to a person object");
                // Continue..
            }
        }
        
        sb.append("</pfif:pfif>");
        return sb.toString();
    }
    
    private String getName(String text) {
		int i = 0;
		StringBuilder name = new StringBuilder();
    	String[] tokens = text.split(" ");
    	for (i=0; i<tokens.length;i++){
    		if (tokens[i].equalsIgnoreCase("imok") || tokens[i].equalsIgnoreCase("#pf") || tokens[i].equalsIgnoreCase("for") || tokens[i].equalsIgnoreCase("Looking")){
    			// do nothing
    		}
    		else{
    			name.append(" " + tokens[i]);
    		}
    	}
		return name.toString();
	}

	/*
     * PFIF Spec: http://zesty.ca/pfif/1.2/
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
