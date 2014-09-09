package cn.edu.pku.sei.jinyong.dao;

import cn.edu.pku.sei.jinyong.entity.Document;

public class DocumentDao {

	public void insertDocument(Document doc) {
		String sql = "insert into document (qmail_uuid,amail_uuid,question,question_seg_no,session_uuid,keywords) values (?,?,?,?,?,?)";

		try {
			DAOUtils.update(sql, doc.getQmail_uuid(), doc.getAmail_uuid(), doc.getQuestion(),
					doc.getQuestion_seg_no(), doc.getSession_uuid(),doc.getKeywords());

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
