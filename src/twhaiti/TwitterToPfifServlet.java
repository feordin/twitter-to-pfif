/*
 * This is a webservice to convert the latest data on Twitter to PFIF.
 * @author     Chirag Shah <chiragshah1@gmail.com>
 * @author     Eric Torreborre <etorreborre@yahoo.com>
 * @license    Public Domain
 */
package twhaiti;

import java.io.IOException;
import java.net.*;

import javax.servlet.http.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import com.google.appengine.api.urlfetch.*;

/**
 * URL: http://twitter-to-pfif.appspot.com/twitter_to_pfif?role=provide
 * 
 * This servlet is responsible for:
 * 
 * * checking the request parameters
 * * creating a response with the PFIF record that could be created by from the Twitter message
 * 
 * * Please go to http://wiki.rhok.org/Person_Finder_Sydney_team to read for a list of 
 *   improvements that can be done on this service:
 *   
 *   * don't create a record if there's already one (what happens in that case)
 *   * notify the reporter of the missing person that a record was created, and tell him/her to go to PersonFinder
 * 
 */
@SuppressWarnings("serial")
public class TwitterToPfifServlet extends HttpServlet {
	private static final URLFetchService fetch = URLFetchServiceFactory.getURLFetchService();

	private PFIF pfif = new PFIF();
	private TwitterQueryBuilder queryBuilder = new TwitterQueryBuilder();
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
		try {
			checkRole(req, resp);
			respond(resp, toPFIF(executeQuery(createTwitterQuery(req))));
		} catch (Exception e) {
			resp.sendError(400, e.getMessage());
		}
	}

	private String createTwitterQuery(HttpServletRequest req) {
		return queryBuilder.createQuery(req);
	}

	private void checkRole(HttpServletRequest req, HttpServletResponse resp) throws IOException, Exception {
		if (!"provide".equals(req.getParameter("role"))) {
			throw new Exception("Only supports the 'provide' role. Send role=provide in the query");
		}
	}

	private void respond(HttpServletResponse resp, String twitterToPfif) throws IOException {
		resp.setContentType("text/xml");
		resp.getWriter().print(twitterToPfif);
	}

	private JSONArray toJSONArray(HTTPResponse httpResponse) throws JSONException {
		return new JSONObject(new String(httpResponse.getContent())).getJSONArray("results");
	}

	private JSONArray executeQuery(String query) throws IOException,
			MalformedURLException, TwitterQueryException {
		final HTTPResponse httpResponse = fetch.fetch(createRequest(query));
		try {
			return toJSONArray(httpResponse);
		} catch (JSONException e) {
			throw new TwitterQueryException("Bad data from Twitter. Try again. Error: " + httpResponse.getResponseCode());
		}
	}

	private HTTPRequest createRequest(String query) throws MalformedURLException {
		final HTTPRequest httpRequest = new HTTPRequest(new URL(query), HTTPMethod.GET);
		httpRequest.addHeader(new HTTPHeader("User-Agent", "Twitter-PFIF-Converter"));
		return httpRequest;
	}

	private String toPFIF(JSONArray result) {
		return pfif.twitterToPFIF(result);
	}

}
