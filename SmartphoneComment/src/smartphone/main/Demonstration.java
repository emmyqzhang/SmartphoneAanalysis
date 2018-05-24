package smartphone.main;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class Demonstration {

	private String filter = " -filter:links -filter:replies -filter:images";

	public void search(String keys, String since, String until, String language) {
		Twitter twitter = new TwitterFactory().getInstance();
		try {
			Query query = new Query(keys + filter);
			if (language != null) {
				query.setLang(language);
			}
			if (since != null) {
				query.setSince(since);
			}
			if (until != null) {
				query.setUntil(until);
			}

			query.setCount(50);

			QueryResult result = null;

			result = twitter.search(query);
			List<Status> tweets = result.getTweets();

			for (Status tweet : tweets) {
				System.out.println(tweet.toString());
			}
		} catch (TwitterException te) {
			te.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Demonstration demonstration = new Demonstration();
		String[] keys = new String[] { "Samsung", "iPhone", "Motorola", "Sony", "OnePlus", "HTC" };
		String lang = "en";

		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String since = sdf.format(new Date((today.getTime() - 3600 * 24 * 1000)));
		String until = sdf.format(today);
		for (String key : keys) {
			demonstration.search(key, since, until, lang);
		}
	}
}
