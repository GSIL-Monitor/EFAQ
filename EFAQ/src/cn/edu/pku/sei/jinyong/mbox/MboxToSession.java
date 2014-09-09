package cn.edu.pku.sei.jinyong.mbox;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.log4j.Logger;

import cn.edu.pku.sei.jinyong.dao.MessageDao;
import cn.edu.pku.sei.jinyong.dao.SessionContentDao;
import cn.edu.pku.sei.jinyong.dao.SessionDao;
import cn.edu.pku.sei.jinyong.entity.Email;
import cn.edu.pku.sei.jinyong.entity.Project;
import cn.edu.pku.sei.jinyong.entity.Session;
import cn.edu.pku.sei.jinyong.entity.SessionContent;
import cn.edu.pku.sei.jinyong.mbox.crawler.ApacheMboxCrawler;
import cn.edu.pku.sei.jinyong.mbox.parser.EmailParser;
import cn.edu.pku.sei.jinyong.mbox.parser.MessageParser;
import cn.edu.pku.sei.jinyong.mbox.parser.SessionBuilder;
import cn.edu.pku.sei.jinyong.utils.Checker;
import cn.edu.pku.sei.jinyong.utils.Config;
import cn.edu.pku.sei.jinyong.utils.MBoxSpliter;
import cn.edu.pku.sei.jinyong.utils.StringUtils;
import cn.edu.pku.sei.jinyong.utils.WriteFile;

public class MboxToSession {

	private static final String		luceneUrl					= Config.getLuceneMboxUrl();

	private static final String		luceneMboxPath				= Config.getLuceneMboxFilePath();

	// private static final String luceneMboxPath =
	// "D:/lab/final/mbox_file/test";

	private static final String		luceneSessionContentPath	= Config.getLuceneSessionContentPath();

	private static final String		tomcatUrl					= Config.getTomcatMboxUrl();

	private static final String		tomcatMboxPath				= Config.getTomcatMboxFilePath();

	private static final String		tomcatSessionContentPath	= Config.getTomcatSessionContentPath();

	protected static final Logger	logger						= Logger.getLogger(MboxToSession.class
																		.getName());

	public static void getMboxFiles() {
		logger.info("start crawl the project Lucene!");
		Project project = new Project();
		project.setProjectID(1);
		project.setProjectName("Lucene");
		project.setProjectUrl("lucene.apache.org");
		project.setMboxPath(luceneMboxPath);
		ArrayList<String> urlList = new ArrayList<String>();
		urlList.add(luceneUrl);
		ApacheMboxCrawler crawler = new ApacheMboxCrawler(project, urlList);
		crawler.Crawl();
		logger.info("lucene mbox crawled end");

		// logger.info("start crawl the project tomcat!");
		// project = new Project();
		// project.setProjectID(2);
		// project.setProjectName("tomcat");
		// project.setProjectUrl("tomcat.apache.org");
		// project.setMboxPath(tomcatMboxPath);
		//
		// urlList = new ArrayList<String>();
		// urlList.add(tomcatUrl);
		// crawler = new ApacheMboxCrawler(project, urlList);
		// crawler.Crawl();
		// logger.info("tomcat mbox crawled end");
	}

	public static void saveMboxToDatabase() {
		logger.info("start save lucene mbox file to database!");
		File fileDir = new File(luceneMboxPath);
		if (fileDir.isDirectory()) {
			File files[] = fileDir.listFiles();
			if (files != null && files.length > 0) {
				for (File file : files) {
					parseMboxFile(file, 1);
				}
			}
		}
		logger.info("lucene mbox save to database finished!");

		// logger.info("start save tomcat mbox file to database");
		// fileDir = new File(tomcatMboxPath);
		// if (fileDir.isDirectory()) {
		// File files[] = fileDir.listFiles();
		// if (files != null && files.length > 0) {
		// for (File file : files) {
		// parseMboxFile(file, 2);
		// }
		// }
		// }
		// logger.info("save tomcat mbox file to database finished!");
	}

	private static void parseMboxFile(File file, int projectID) {
		if (!file.exists()) {
			logger.info("File does not exist!");
			return;
		}

		logger.info("start parse mbox file " + file.getAbsolutePath());
		long start = System.currentTimeMillis();
		ArrayList<String> emails = MBoxSpliter.splitMBox(file);
		ArrayList<Email> emailList = new ArrayList<Email>();
		for (String email : emails) {
			// EmailParser ep = new EmailParser();
			// Email e = ep.parse(email);
			Email e = MessageParser.buildMessage(email);
			if (e == null)
				continue;
			e.setUuid(UUID.randomUUID().toString());
			e.setProjectID(projectID);
			// TODO tmp set the keywords subject
			e.setKeyWords(e.getSubject());
			if (Checker.isGoodMessage(e))
				emailList.add(e);
		}

		ArrayList<Session> sessionList = new ArrayList<Session>();
		SessionBuilder sessionBuilder = new SessionBuilder(emailList);
		ArrayList<SessionContent> sessionContentList = new ArrayList<SessionContent>();
		sessionBuilder.buildSessionAndContent();
		sessionList = sessionBuilder.getSessionList();
		emailList = sessionBuilder.getEmailList();
		sessionContentList = sessionBuilder.getSessionContentList();
		MessageDao md = new MessageDao();
		for (Email e : emailList) {
			// if(Checker.isGoodMessage(e)) {
			// md.insertMessage(e);
			// // System.out.println("insert a message : " + e.getMessageID());
			// }
			md.insertMessage(e);
		}

		SessionDao sd = new SessionDao();
		for (Session s : sessionList) {
			// s.setProjectID("test project lucene");
			// if(Checker.isGoodSession(s)) {
			// sd.insertSession(s);
			// // System.out.println("insert a session : " + s.getSessionID());
			// }
			if (s.getParticipants().split(",").length == 1)
				continue;
			s.setProjectID(projectID);
			sd.insertSession(s);
		}

		SessionContentDao scd = new SessionContentDao();
		for (SessionContent sc : sessionContentList) {
			// the session length should be longer than 1
			if (sc.getParticipants().split(",").length == 1)
				continue;
			sc.setProjectID(projectID);
			scd.insertSessionContent(sc);
			// write the session content to file

			String subject = StringUtils.legalFileName(sc.getSubject());
			String filePath = (projectID == 1 ? luceneSessionContentPath : tomcatSessionContentPath)
					+ "/" + subject + ".txt";
			WriteFile.writeStringToFile(sc.getContent(), filePath);
		}
		long end = System.currentTimeMillis();
		logger.info("process mbox file " + file.getAbsolutePath() + " end!");
		System.out.println("process mbox file " + file.getAbsolutePath() + " cost time "
				+ (end - start) + " mils");

	}

	public static void main(String args[]) {
		// getMboxFiles();
		saveMboxToDatabase();
	}
}
