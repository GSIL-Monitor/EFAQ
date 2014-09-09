package cn.edu.pku.sei.jinyong.entity;

public class Sentence {

	private String	sentence;

	public Sentence() {
		sentence = "";
	}

	public Sentence(String s) {
		this.sentence = s;
	}

	public String getSentence() {
		return sentence;
	}

	public void setSentence(String sentence) {
		this.sentence = sentence;
	}

	public String toString() {
		return sentence;
	}

}
