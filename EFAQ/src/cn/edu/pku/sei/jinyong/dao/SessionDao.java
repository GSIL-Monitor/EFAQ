package cn.edu.pku.sei.jinyong.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;

import cn.edu.pku.sei.jinyong.entity.Email;
import cn.edu.pku.sei.jinyong.entity.Session;

/**
 * @ClassName: SessionDao
 * @Description: �ṩ����ݿ��в���session�Ĺ���
 * @author: left
 * @date: 2013��9��25�� ����9:06:02
 */

public class SessionDao {

	public void insertSession(Session session) {
		String insertSql = "insert into session (projectID,sessionID,"
				+ "participants,promoterName,promoterEmail,subject,msgList,startTime,endTime) values "
				+ "(?,?,?,?,?,?,?,?,?)";
		// session.setProjectID("");
		// session.setSubject("");
		Timestamp startTime = new Timestamp(session.getStartTime().getTime());
		Timestamp endTime = new Timestamp(session.getEndTime().getTime());
		try {
			DAOUtils.update(insertSql, session.getProjectID(), session.getSessionID(),
					session.getParticipants(), session.getPromoterName(),
					session.getPromoterEmail(), session.getSubject(), session.getMsgList(),
					startTime, endTime);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Session> getAllSession() {
		String sql = "select * from session";
		ArrayList<Session> sessionList = new ArrayList<Session>();
		try {
			sessionList = (ArrayList<Session>) DAOUtils.getResult(Session.class, sql);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return sessionList;
	}

	public Session getSessionById(int id) {
		String sql = "select * from session where id = ?";
		Session session = new Session();
		try {
			session = DAOUtils.getResult2(Session.class, sql, id);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return session;
	}

	public Session getSessionByUuid(String sessionUuid) {
		String sql = "select * from session where sessionId = ?";
		Session session = new Session();
		try {
			session = DAOUtils.getResult2(Session.class, sql, sessionUuid);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return session;
	}

	public ArrayList<Email> getEmailsOfSession(Session session) {
		ArrayList<Email> emailList = new ArrayList<Email>();
		String[] msgList = session.getMsgList().split(",");
		MessageDao md = new MessageDao();
		for (String msgId : msgList) {
			emailList.add(md.getEmailByMessageId(msgId));
		}
		return emailList;
	}

	public static void main(String args[]) {
		SessionDao sd = new SessionDao();
		Session session = sd.getSessionById(2);
		System.out.println(session);
		ArrayList<Email> emailList = sd.getEmailsOfSession(session);
		for (Email email : emailList) {
			System.out.println(email.getMessageID());
		}
	}
}
