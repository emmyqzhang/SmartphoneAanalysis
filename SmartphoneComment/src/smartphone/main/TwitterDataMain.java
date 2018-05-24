package smartphone.main;

import smartphone.twitter.search.SearchTweets;

public class TwitterDataMain {

	public static void main(String[] args) {
		String[] keys = new String[] { "Samsung", "iPhone", "Motorola", "Sony", "OnePlus", "HTC" };
		SearchTweets searchTweets = new SearchTweets();
		String lang = "en";
		long times = 5010;

		String ym = "2016-11-";
		// 1-10
		int s = 1;
		int u = 10;

		String since = null;
		String until = null;

		for (int i = s; i < u; i++) {
			since = ym + (i < 10 ? ("0" + i) : i);
			until = ym + (i + 1 < 10 ? ("0" + (i + 1)) : i + 1);

			System.out.println();
			System.out.println("------------------" + since + "~" + until + "------------------");
			for (String key : keys) {
				System.out.println("------------------" + key + "------------------");
				searchTweets.search(key, since, until, lang, times);
			}
		}

		searchTweets.clean();
	}
}
