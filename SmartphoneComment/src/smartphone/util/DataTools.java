package smartphone.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import smartphone.twitter.analyzer.StatisticBean;

public class DataTools {
	/**
	 * 清除内容中的emoji字符
	 * 
	 * @param content
	 * @return
	 */
	public static String removeFourChar(String content) {
		byte[] conbyte = content.getBytes();
		for (int i = 0; i < conbyte.length; i++) {
			if ((conbyte[i] & 0xF8) == 0xF0) {
				for (int j = 0; j < 4; j++) {
					conbyte[i + j] = 0x30;
				}
				i += 3;
			}
		}
		content = new String(conbyte);
		return content.replaceAll("0000", "");
	}

	/**
	 * 
	 * @param statistic
	 * @param filePath
	 * @throws IOException
	 */
	public static void saveJsonFile(Map<String, Map<String, StatisticBean>> statistic, String filePath)
			throws IOException {
		StringBuffer jsonStr = new StringBuffer("[");

		StringBuffer labelsLine = new StringBuffer("");
		StringBuffer positiveLine = new StringBuffer("");
		StringBuffer neutralLine = new StringBuffer("");
		StringBuffer negativeLine = new StringBuffer("");
		int positivePie = 0;
		int neutralPie = 0;
		int negativePie = 0;

		Iterator<String> dayKeys = null;
		Iterator<String> smartphoneKeys = statistic.keySet().iterator();
		Map<String, StatisticBean> smartphoneMap = null;
		String smartphoneKey = null;
		String dayKey = null;
		StatisticBean statisticBean = null;
		while (smartphoneKeys.hasNext()) {
			positivePie = 0;
			neutralPie = 0;
			negativePie = 0;

			labelsLine = new StringBuffer("");
			positiveLine = new StringBuffer("");
			neutralLine = new StringBuffer("");
			negativeLine = new StringBuffer("");

			smartphoneKey = smartphoneKeys.next();
			smartphoneMap = statistic.get(smartphoneKey);
			dayKeys = smartphoneMap.keySet().iterator();
			while (dayKeys.hasNext()) {
				dayKey = dayKeys.next();
				statisticBean = smartphoneMap.get(dayKey);

				labelsLine.append("\"" + dayKey + "\",");
				positiveLine.append(statisticBean.getPositiveCount() + ",");
				neutralLine.append(statisticBean.getNeutralCount() + ",");
				negativeLine.append(statisticBean.getNegativeCount() + ",");
				positivePie += statisticBean.getPositiveCount();
				neutralPie += statisticBean.getNeutralCount();
				negativePie += statisticBean.getNegativeCount();
			}

			jsonStr.append("{");
			jsonStr.append("\"smartphone\": \"" + smartphoneKey + "\",");
			jsonStr.append("\"line\": {");
			jsonStr.append("\"name\": \"" + smartphoneKey + "_line\",");

			jsonStr.append("\"labels\": [");
			jsonStr.append(labelsLine.deleteCharAt(labelsLine.length() - 1));
			jsonStr.append("],");

			jsonStr.append("\"positive\": [");
			jsonStr.append(positiveLine.deleteCharAt(positiveLine.length() - 1));
			jsonStr.append("],");

			jsonStr.append("\"neutral\": [");
			jsonStr.append(neutralLine.deleteCharAt(neutralLine.length() - 1));
			jsonStr.append("],");

			jsonStr.append("\"negative\": [");
			jsonStr.append(negativeLine.deleteCharAt(negativeLine.length() - 1));
			jsonStr.append("]");
			jsonStr.append("},");

			jsonStr.append("\"pie\": {");
			jsonStr.append("\"name\": \"" + smartphoneKey + "_pie\",");

			jsonStr.append("\"positive\": ");
			jsonStr.append(positivePie);
			jsonStr.append(",");

			jsonStr.append("\"neutral\": ");
			jsonStr.append(neutralPie);
			jsonStr.append(",");

			jsonStr.append("\"negative\": ");
			jsonStr.append(negativePie);
			jsonStr.append("}");

			jsonStr.append("},");
		}

		jsonStr.deleteCharAt(jsonStr.length() - 1).append("]");

		File file = new File(filePath);
		FileOutputStream fileOutputStream = new FileOutputStream(file);

		fileOutputStream.write(jsonStr.toString().getBytes());
		fileOutputStream.close();
	}
}
