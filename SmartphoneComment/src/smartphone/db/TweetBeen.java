package smartphone.db;

import java.util.Date;

public class TweetBeen {

	private long id = 0;
	private long tweet_id = 0;
	private Date createdAt = null;
	private long currentUserRetweetId = 0;
	private int favoriteCount = 0;
	private String inReplyToScreenName = null;
	private long inReplyTostatusId = 0;
	private long inReplyUserId = 0;
	private String lang = null;
	private String content = null;
	private String userName = null;
	private String isTruncated = null;
	private String country = null;
	private String countryCode = null;
	private String manufacturer = null;
	private String smartphone = null;

	public TweetBeen(long id, long tweet_id, Date createdAt, long currentUserRetweetId, int favoriteCount,
			String inReplyToScreenName, long inReplyTostatusId, long inReplyUserId, String lang, String content,
			String userName, String isTruncated, String country, String countryCode, String manufacturer,
			String smartphone) {
		this.id = id;
		this.tweet_id = tweet_id;
		this.createdAt = createdAt;
		this.currentUserRetweetId = currentUserRetweetId;
		this.favoriteCount = favoriteCount;
		this.inReplyToScreenName = inReplyToScreenName;
		this.inReplyTostatusId = inReplyTostatusId;
		this.inReplyUserId = inReplyUserId;
		this.lang = lang;
		this.content = content;
		this.userName = userName;
		this.isTruncated = isTruncated;
		this.country = country;
		this.countryCode = countryCode;
		this.manufacturer = manufacturer;
		this.smartphone = smartphone;
	}

	public long getTweet_id() {
		return tweet_id;
	}

	public void setTweet_id(long tweet_id) {
		this.tweet_id = tweet_id;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public long getCurrentUserRetweetId() {
		return currentUserRetweetId;
	}

	public void setCurrentUserRetweetId(long currentUserRetweetId) {
		this.currentUserRetweetId = currentUserRetweetId;
	}

	public int getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(int favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public String getInReplyToScreenName() {
		return inReplyToScreenName;
	}

	public void setInReplyToScreenName(String inReplyToScreenName) {
		this.inReplyToScreenName = inReplyToScreenName;
	}

	public long getInReplyTostatusId() {
		return inReplyTostatusId;
	}

	public void setInReplyTostatusId(long inReplyTostatusId) {
		this.inReplyTostatusId = inReplyTostatusId;
	}

	public long getInReplyUserId() {
		return inReplyUserId;
	}

	public void setInReplyUserId(long inReplyUserId) {
		this.inReplyUserId = inReplyUserId;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getIsTruncated() {
		return isTruncated;
	}

	public void setIsTruncated(String isTruncated) {
		this.isTruncated = isTruncated;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getSmartphone() {
		return smartphone;
	}

	public void setSmartphone(String smartphone) {
		this.smartphone = smartphone;
	}
}
