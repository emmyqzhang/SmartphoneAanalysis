package smartphone.main;

import java.io.IOException;
import java.util.Map;

import smartphone.twitter.analyzer.Frequency;
import smartphone.twitter.analyzer.StatisticBean;
import smartphone.util.DataTools;

public class StatisticMain {

	public static void main(String[] args) {
		Frequency frequency = new Frequency();
		Map<String, Map<String, StatisticBean>>statistic = frequency.doStatisticAnalysis();

		try {
			//把分析结果的Map存成json格式的文件
			DataTools.saveJsonFile(statistic,"C:/statistic.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
