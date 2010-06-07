package twhaiti;

/**
 * This class is to provide methods for replying to the tweet that we found
 * on twitter
 * for now it sends a direct tweet with the count of the name we found
 *
 * Possible upgrades:
 *  * improve the content going back to the end user
 */
import java.net.URLEncoder;

import org.json.JSONObject;
import org.json.JSONException;

import twitter4j.DirectMessage;
import twitter4j.Twitter;
import twitter4j.TwitterException;

public class TwitterReply {

	public String replyToTweet(JSONObject twitterObj, String user, String password, int count, String searchedPerson){
		
		String recipient = "";
		StringBuilder message = new StringBuilder();
		try{
			recipient = twitterObj.getString("from_user");
			message.append("@" + recipient);
		
		
		String queryUrl = "http://haiticrisis.appspot.com//results?role=seek&query=%s";
		String url = String.format(
                queryUrl, URLEncoder.encode(searchedPerson, "UTF-8"));
		message.append(" Found " + count + " person(s) at " + url +"  related to:" + searchedPerson);
		}
		catch (Exception ex){
			return("Failed to send message: " + ex.getMessage());
		}
		
	    Twitter twitter = new Twitter(user, password);
	    try {
	    	DirectMessage sentMessage = twitter.sendDirectMessage(recipient, message.toString());
	    	twitter.updateStatus(message.toString(), twitterObj.getLong("id") );
	    	return sentMessage.getRecipient().toString();
	    } 
	    catch (Exception te) {
	    	return("Failed to send message: " + te.getMessage());
	    }
	}
}