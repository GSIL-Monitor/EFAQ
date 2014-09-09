package cn.edu.pku.sei.jinyong.dao;

import java.sql.SQLException;

import org.apache.log4j.Logger;

import cn.edu.pku.sei.jinyong.entity.SessionContent;
import cn.edu.pku.sei.jinyong.mbox.parser.MessageParser;

public class SessionContentDao {

	protected static final Logger	logger	= Logger.getLogger(SessionContentDao.class.getName());

	public void insertSessionContent(SessionContent sessionContent) {
		String insertSql = "insert into session_content(projectID,sessionID,participants,msgList,subject,content) values (?,?,?,?,?,?)";

		try {
			DAOUtils.update(insertSql, sessionContent.getProjectID(),
					sessionContent.getSessionID(), sessionContent.getParticipants(),
					sessionContent.getMsgList(), sessionContent.getSubject(),
					sessionContent.getContent());
		}
		catch (ClassNotFoundException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
		catch (SQLException e) {
			e.printStackTrace();
			logger.info(e.getMessage());
		}
	}

	public static void main(String args[]) {

	}
}
