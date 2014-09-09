package cn.edu.pku.sei.jinyong.tag;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;

import cn.edu.pku.sei.jinyong.utils.ReadFile;

public class StopWords {

	public static HashSet<String>	stopWords		= new HashSet<String>();

	public static final String		STOP_WORDS_FILE	= "/stopwords.txt";

	public static void readStopWordsFromFile() {
		if (stopWords.size() == 0) {
			String path = "";
			try {
				path = StopWords.class.getResource("/").toURI().getPath();
			}
			catch (URISyntaxException e) {
				e.printStackTrace();
			}
			ArrayList<String> lines = ReadFile.readFileLines(path + STOP_WORDS_FILE);
			for (String word : lines) {

				stopWords.add(word);
			}
		}
	}

	public static HashSet<String> getStopWordsSet() {
		if (stopWords.size() == 0)
			readStopWordsFromFile();
		return stopWords;
	}

	public static void main(String args[]) {
		HashSet<String> temp = getStopWordsSet();
		for (String word : temp) {
			System.out.println(word);
		}
		System.out.println(temp.size());
	}
}
