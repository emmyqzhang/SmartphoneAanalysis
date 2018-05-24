package smartphone.twitter.analyzer;

public class StatisticBean {

	private String smartphone = null;
	private String date = null;
	private int positiveCount = 0;
	private int negativeCount = 0;
	private int neutralCount = 0;

	public StatisticBean(String smartphone, String date) {
		this.smartphone = smartphone;
		this.date = date;
	}

	public String getSmartphone() {
		return smartphone;
	}

	public void setSmartphone(String smartphone) {
		this.smartphone = smartphone;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public int getPositiveCount() {
		return positiveCount;
	}

	public void addPositiveCount() {
		this.positiveCount++;
	}

	public void setPositiveCount(int positiveCount) {
		this.positiveCount = positiveCount;
	}

	public int getNegativeCount() {
		return negativeCount;
	}

	public void addNegativeCount() {
		this.negativeCount++;
	}

	public void setNegativeCount(int negativeCount) {
		this.negativeCount = negativeCount;
	}

	public int getNeutralCount() {
		return neutralCount;
	}

	public void addNeutralCount() {
		this.neutralCount++;
	}

	public void setNeutralCount(int neutralCount) {
		this.neutralCount = neutralCount;
	}

	public String toString(){
		return smartphone + "::" + date + "::positive=" + positiveCount + "::negative=" + negativeCount + "::neutral=" + neutralCount;
	}
}
