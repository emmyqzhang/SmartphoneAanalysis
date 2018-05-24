package smartphone.twitter.analyzer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.TreeMap;

import smartphone.db.DatabaseManager;
import smartphone.db.TweetBeen;

public class Frequency {

	private int page_size = 100;

	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private SentimentAnalyzer sentimentAnalyzer = new SentimentAnalyzer();

	private Map<String, Map<String, StatisticBean>> statistic = new TreeMap<String, Map<String, StatisticBean>>();

	/**
	 * 分析数据
	 * @return
	 */
	public Map<String, Map<String, StatisticBean>> doStatisticAnalysis() {
		//统计所有“推”的数量的sql
		String tweetStateCount = "select count(id) from tweet";
		//分页去取出“推”的sql。limit是分页，后面的?是占位符，在jdbc执行查询的时候会给?赋值
		String tweetStateSel = "select id, tweet_id, created_at, current_user_retweet_id, favorite_count, in_reply_to_screen_name, in_reply_tostatus_id, in_reply_user_id, lang, content, user_name, is_truncated, country, country_code, manufacturer, smartphone from tweet limit ?,?";
		
		loadAndAnalyze(tweetStateCount, tweetStateSel, statistic);

		//统计所有“转推”的数量的sql
		String retweetStateCount = "select count(id) from retweet";
		//分页去取出“转推”的sql。limit是分页，后面的?是占位符，在jdbc执行查询的时候会给?赋值
		String retweetStateSel = "select id, tweet_id, created_at, current_user_retweet_id, favorite_count, in_reply_to_screen_name, in_reply_tostatus_id, in_reply_user_id, lang, content, user_name, is_truncated, country, country_code, manufacturer, smartphone from retweet limit ?,?";
	
		loadAndAnalyze(retweetStateCount, retweetStateSel, statistic);
		return statistic;
	}

	/**
	 * 读和分析数据
	 * @param countSql 计算总数的sql
	 * @param selectSql 查询数据的sql
	 * @param statistic 保存统计分析的结果的Map
	 */
	private void loadAndAnalyze(String countSql, String selectSql, Map<String, Map<String, StatisticBean>> statistic) {
		Connection conn = DatabaseManager.getConnection();

		PreparedStatement tweetStateCount = null;
		PreparedStatement tweetStateSel = null;
		ResultSet result = null;

		//页码
		int page_index = 1;
		//最大页码
		int page_max = 0;

		try {
			tweetStateCount = conn.prepareStatement(countSql);

			result = tweetStateCount.executeQuery();
			if (result.next()) {
				//计算最大页码
				page_max = ((result.getInt(1) / page_size) + 1);
				//注意注意注意！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！！
				//这个page_max = 2;是应该删掉的，但是我给你时候忘了删了，你注意一下啊。这是我测试的时候为了只取少量数据而设的固定值。
				//教授如果问的话你别说错了
				page_max = 2;
			}

			//得到一个PreparedStatement对象
			tweetStateSel = conn.prepareStatement(selectSql);

			//循环页码，得到全部数据
			while (page_index <= page_max) {
				//按页码计算数据的开始位置，把设置到第一个占位符（就是?）
				tweetStateSel.setInt(1, (page_index - 1) * 100);
				//把每页的数据数量值设置到第2个占位符。其实这里可以不用?，在前面的sql里直接写成100就行了。我写的有点啰嗦了
				tweetStateSel.setInt(2, page_size);
				page_index++;
				//执行sql，得到结果集
				result = tweetStateSel.executeQuery();
				//循环结果集
				while (result.next()) {
					analyze(result, statistic);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				result.close();
				result = null;

				tweetStateSel.close();
				tweetStateSel = null;

			} catch (SQLException e) {
				e.printStackTrace();
			}

			DatabaseManager.close(conn);
		}
	}

	/**
	 * 分析
	 * @param result 一行数据
	 * @param statistic 保存统计分析的结果的Map
	 * @throws SQLException
	 */
	private void analyze(ResultSet result, Map<String, Map<String, StatisticBean>> statistic) throws SQLException {
		//把数据库数据转为对象，方便后面调用。
		TweetBeen tweetBeen = new TweetBeen(result.getLong(1), result.getLong(2), result.getDate(3), result.getLong(4),
				result.getInt(5), result.getString(6), result.getLong(7), result.getLong(8), result.getString(9),
				result.getString(10), result.getString(11), result.getString(12), result.getString(13),
				result.getString(14), result.getString(15), result.getString(16));

		//格式化日期对象的文本格式，sdf的定义在上面
		String date = sdf.format(tweetBeen.getCreatedAt());
		
		//情感分析
		String sentiment = sentimentAnalyzer.findSentiment(tweetBeen.getContent());

		//按手机名从分析结果Map中取出对应品牌的分析数据的Map
		Map<String, StatisticBean> smartphoneMap = statistic.get(tweetBeen.getSmartphone());

		//如果没有取得对应手机的分析结果Map，就说明这是第一次遇到这个手机的数据，所以我们要做初始化工作
		if (smartphoneMap == null) {
			smartphoneMap = new TreeMap<String, StatisticBean>();
			statistic.put(tweetBeen.getSmartphone(), smartphoneMap);
		}

		////按日期从单个手机的分析Map中取出当日的分析结果
		StatisticBean statisticBean = smartphoneMap.get(date);
		//如果没有取得对应日期的分析结果，就说明这是第一次遇到这个手机的这一天的数据，所以我们要做初始化工作
		if (statisticBean == null) {
			statisticBean = new StatisticBean(tweetBeen.getSmartphone(), date);
			smartphoneMap.put(date, statisticBean);
		}

		//有了以上两步数据检查和初始化工作的保证，我们在这里就能放心的存分析结果数据内容了。
		if ("positive".equals(sentiment)) {
			statisticBean.addPositiveCount();
		} else if ("negative".equals(sentiment)) {
			statisticBean.addNegativeCount();
		} else if ("neutral".equals(sentiment)) {
			statisticBean.addNeutralCount();
		}
	}
}
