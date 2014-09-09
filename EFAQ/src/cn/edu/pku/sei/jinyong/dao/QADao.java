package cn.edu.pku.sei.jinyong.dao;

import java.util.ArrayList;

import cn.edu.pku.sei.jinyong.entity.QuestionAnswer;

public class QADao {

	public void insertQA(QuestionAnswer qa) {
		String insertSql = "insert into question_answer (session_uuid,session_id,message_id,message_uuid,question,answer_message_id,answer_message_uuid) values "
				+ "(?,?,?,?,?,?,?)";
		try {
			DAOUtils.update(insertSql, qa.getSession_uuid(), qa.getSession_id(),
					qa.getMessage_id(), qa.getMessage_uuid(), qa.getQuestion(),
					qa.getAnswer_message_id(), qa.getAnswer_message_uuid());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<QuestionAnswer> getAllQA() {
		ArrayList<QuestionAnswer> qaList = new ArrayList<QuestionAnswer>();
		String sql = "select * from question_answer";
		try {
			qaList = (ArrayList<QuestionAnswer>) DAOUtils.getResult(QuestionAnswer.class, sql);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return qaList;
	}

	public QuestionAnswer getQAById(int id) {
		String sql = "select * from question_answer where id = ?";
		QuestionAnswer qa = new QuestionAnswer();

		try {
			qa = (QuestionAnswer) DAOUtils.getResult2(QuestionAnswer.class, sql, id);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return qa;
	}

	public static void main(String args[]) {
		QuestionAnswer qa = new QuestionAnswer();
		qa.setQuestion("How are you?");
		new QADao().insertQA(qa);
	}

}
