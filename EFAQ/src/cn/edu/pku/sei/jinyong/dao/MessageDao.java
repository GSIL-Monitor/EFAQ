package cn.edu.pku.sei.jinyong.dao;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import cn.edu.pku.sei.jinyong.entity.Email;

/**
 * @ClassName: MessageDao
 * @Description: insert message
 * @author: left
 * @date: 2013.12.25 2:15:38
 */

public class MessageDao {

	public void insertMessage(Email email) {
		String insertSql = "insert into message (uuid,messageID,projectID,subject,content,inReplyTo,"
				+ "fromName,toName,fromEmail,toEmail,keywords,sendDate) values "
				+ "(?,?,?,?,?,?,?,?,?,?,?,?)";
		if (email.getSendDate() == null)
			email.setSendDate(new Date());
		Timestamp sendTime = new Timestamp(email.getSendDate().getTime());

		try {
			DAOUtils.update(insertSql, email.getUuid(), email.getMessageID(), email.getProjectID(),
					email.getSubject(), email.getContent(), email.getInReplyTo(),
					email.getFromName(), email.getToName(), email.getFromEmail(),
					email.getToEmail(), email.getKeyWords(), sendTime);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

	}

	public ArrayList<Email> getAllMessageFromDatabase() {
		String sql = "select * from message";
		ArrayList<Email> messageList = new ArrayList<Email>();
		try {
			messageList = (ArrayList<Email>) DAOUtils.getResult(Email.class, sql);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return messageList;
	}

	public Email getEmailByUuid(String uuid) {
		String sql = "select * from message where uuid = ?";
		Email message = new Email();
		try {
			message = DAOUtils.getResult2(Email.class, sql, uuid);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return message;
	}

	public Email getEmailById(int id) {
		String sql = "select * from message where id = ?";
		Email message = new Email();
		try {
			message = DAOUtils.getResult2(Email.class, sql, id);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return message;
	}

	public Email getEmailByMessageId(String messageId) {
		String sql = "select * from message where messageID = ?";
		Email message = new Email();
		try {
			message = DAOUtils.getResult2(Email.class, sql, messageId);
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return message;
	}

	public static void main(String args[]) {
		// ArrayList<Email> messageList = new
		// MessageDao().getAllMessageFromDatabase();
		// for(Email e : messageList) {
		// System.out.println(e.toString());
		// }
		Email e = new MessageDao()
				.getEmailByMessageId("<15284.59163.913199.19577@cabernet.nelson.monkey.org>");
		System.out.println(e.toString());
	}
}
