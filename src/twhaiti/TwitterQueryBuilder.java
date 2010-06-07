package twhaiti;

import static twhaiti.Option.none;
import static twhaiti.Option.some;

import javax.servlet.http.HttpServletRequest;

/**
 * This class can be extended to provide more intelligence about the type of Twitter messages to fetch.
 * 
 * The api for the Twitter search API, returning JSON data can be found here: http://search.twitter.com/api/
 * 
 * For now the only incoming parameter that is used is the "since_id" parameter. The request parameters could be extended to
 * specify:
 * 
 * * the keywords to look for: need, imok, damage, injured
 * * the status id. The retrieved tweets must have a status superior to that one: that's the since_id parameter in the request
 *   To get an idea of the current status id: http://www.twitpocalypse.com/
 * 
 * The default query was built from the http://search.twitter.com/advanced page by entering
 * "Any of these words"  = "need imok damage injured" 
 * "None of these words" = "RT via epiccolorado tweakthetweet roubboy posting twitagsearch openstreetmap"
 * "This hashtag"        = "pf"
 * 
 * A reasonable enhancement would be to:
 * 
 * * be able to pass the hashtag
 * * be able to pass the required words
 * * be able to pass the rejected words. In the case of the haiti crisis, removing tweakthetweet allows to filter those
 *   messages that describe other needs (see here: http://epic.cs.colorado.edu/groups/tweakthetweet/)
 * * include other twitter search paramerters: from person, to person, near location, etc.
 * 
 * Something to explore:
 * * how to use the EPIC project (http://epic.cs.colorado.edu/tweak-the-tweet/helping_haiti_tweak_the_twe.html)
 *   to provide twitter "enhanced" messages * 
 *
 */
public class TwitterQueryBuilder {

	private static final String TWITTER_SEARCH = "http://search.twitter.com/search.json?q=";
	private static final String DEFAULT_SEARCH_QUERY = "+imok+OR+injured+OR+looking+%23pf";

	public String createQueryTag(HttpServletRequest req, String tag)	{
		String query = DEFAULT_SEARCH_QUERY;
		query = query.replace("#pf", tag);
		return runQuery(req, query);
	}
	
	public String createQuery(HttpServletRequest req)	{
		String query = DEFAULT_SEARCH_QUERY;
		return runQuery(req, query);
	}
	
	public String runQuery(HttpServletRequest req, String query) {
		String since_id = "7816973595";
		for (String since : getParameter(req, "since_id")) {
			since_id = since;
		}
		query += "&since_id=" + since_id;
		query = TWITTER_SEARCH + query;
		return query;
	}

	private Option<String> getParameter(HttpServletRequest req, String paramName) {
		final String value = req.getParameter(paramName);
		if (null != value && !"".equals(value))
			return some(value);
		return none();
	}
	

}
