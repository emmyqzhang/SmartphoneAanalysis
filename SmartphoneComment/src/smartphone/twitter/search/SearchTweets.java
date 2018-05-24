package smartphone.twitter.search;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import smartphone.db.DatabaseManager;
import smartphone.util.DataTools;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class SearchTweets {

	private String filter = " -filter:links -filter:replies -filter:images";

	/**
	 * 
	 * @param keys
	 * @param since
	 * @param until
	 * @param language
	 * @param times
	 */
	public void search(String keys, String since, String until, String language, long times) {
		Twitter twitter = new TwitterFactory().getInstance();
		Connection conn = DatabaseManager.getConnection();
		PreparedStatement tweetStateIns = null;
		PreparedStatement retweetStateIns = null;
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

			query.setCount(100);

			QueryResult result = null;
			tweetStateIns = conn.prepareStatement(
					"insert into tweet (tweet_id, created_at, current_user_retweet_id, favorite_count, in_reply_to_screen_name, in_reply_tostatus_id, in_reply_user_id, lang, content, user_name, is_truncated, country, country_code, manufacturer, smartphone) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			retweetStateIns = conn.prepareStatement(
					"insert into retweet (tweet_id, created_at, current_user_retweet_id, favorite_count, in_reply_to_screen_name, in_reply_tostatus_id, in_reply_user_id, lang, content, user_name, is_truncated, country, country_code, manufacturer, smartphone) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");

			int num = 0;

			do {
				result = twitter.search(query);
				List<Status> tweets = result.getTweets();

				num += tweets.size();

				for (Status tweet : tweets) {
					if (tweet.isRetweet()) {
						retweetStateIns.setLong(1, tweet.getId());
						retweetStateIns.setTimestamp(2, new java.sql.Timestamp(tweet.getCreatedAt().getTime()));
						retweetStateIns.setLong(3, tweet.getCurrentUserRetweetId());
						retweetStateIns.setInt(4, tweet.getFavoriteCount());
						retweetStateIns.setString(5, tweet.getInReplyToScreenName());
						retweetStateIns.setLong(6, tweet.getInReplyToStatusId());
						retweetStateIns.setLong(7, tweet.getInReplyToUserId());
						retweetStateIns.setString(8, tweet.getLang());
						retweetStateIns.setString(9, DataTools.removeFourChar(tweet.getText()));
						retweetStateIns.setString(10, tweet.getUser().getScreenName());
						retweetStateIns.setString(11, (tweet.isTruncated() ? "1" : "0"));
						retweetStateIns.setString(12, tweet.getPlace() == null ? null : tweet.getPlace().getCountry());
						retweetStateIns.setString(13,
								tweet.getPlace() == null ? null : tweet.getPlace().getCountryCode());
						retweetStateIns.setString(14, keys);
						retweetStateIns.setString(15, keys);
						retweetStateIns.execute();
					} else {
						tweetStateIns.setLong(1, tweet.getId());
						tweetStateIns.setTimestamp(2, new java.sql.Timestamp(tweet.getCreatedAt().getTime()));
						tweetStateIns.setLong(3, tweet.getCurrentUserRetweetId());
						tweetStateIns.setInt(4, tweet.getFavoriteCount());
						tweetStateIns.setString(5, tweet.getInReplyToScreenName());
						tweetStateIns.setLong(6, tweet.getInReplyToStatusId());
						tweetStateIns.setLong(7, tweet.getInReplyToUserId());
						tweetStateIns.setString(8, tweet.getLang());
						tweetStateIns.setString(9, DataTools.removeFourChar(tweet.getText()));
						tweetStateIns.setString(10, tweet.getUser().getScreenName());
						tweetStateIns.setString(11, (tweet.isTruncated() ? "1" : "0"));
						tweetStateIns.setString(12, tweet.getPlace() == null ? null : tweet.getPlace().getCountry());
						tweetStateIns.setString(13,
								tweet.getPlace() == null ? null : tweet.getPlace().getCountryCode());
						tweetStateIns.setString(14, keys);
						tweetStateIns.setString(15, keys);
						tweetStateIns.execute();
					}
				}

				Thread.sleep(times);
			} while ((query = result.nextQuery()) != null);

			System.out.println("------------------" + keys + ":" + num + "------------------");
		} catch (TwitterException te) {
			te.printStackTrace();
			System.out.println("Failed to search tweets: " + te.getMessage());
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			try {
				tweetStateIns.close();
				tweetStateIns = null;

				retweetStateIns.close();
				retweetStateIns = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}

			DatabaseManager.close(conn);
		}
	}

	/**
	 * 清除品牌重叠的推
	 */
	public void clean() {
		Connection conn = DatabaseManager.getConnection();
		Statement tweetStateClean = null;
		Statement retweetStateClean = null;
		try {
			tweetStateClean = conn.createStatement();
			retweetStateClean = conn.createStatement();

			tweetStateClean.execute(
					"DELETE FROM `tweet` WHERE tweet_id IN (SELECT tt.tweet_id FROM (SELECT t.tweet_id,COUNT(t.tweet_id) AS num FROM `tweet` t GROUP BY t.`tweet_id`) tt WHERE num>1)");
			retweetStateClean.execute(
					"DELETE FROM `retweet` WHERE tweet_id IN (SELECT tt.tweet_id FROM (SELECT t.tweet_id,COUNT(t.tweet_id) AS num FROM `retweet` t GROUP BY t.`tweet_id`) tt WHERE num>1)");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				tweetStateClean.close();
				tweetStateClean = null;

				retweetStateClean.close();
				retweetStateClean = null;
			} catch (SQLException e) {
				e.printStackTrace();
			}

			DatabaseManager.close(conn);
		}
	}
}
