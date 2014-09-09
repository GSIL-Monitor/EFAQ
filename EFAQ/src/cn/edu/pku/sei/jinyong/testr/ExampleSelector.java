package cn.edu.pku.sei.jinyong.testr;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import cn.edu.pku.sei.jinyong.dao.SessionDao;
import cn.edu.pku.sei.jinyong.entity.Email;
import cn.edu.pku.sei.jinyong.entity.Session;
import cn.edu.pku.sei.jinyong.utils.WriteFile;

public class ExampleSelector {

	/**
	 * @fieldName: EXAMPLE_NUM
	 * @fieldType: int
	 * @Description: number of the example
	 */
	private static int			EXAMPLE_NUM		= 1000;

	/**
	 * @fieldName: EXMAPLE_PATH
	 * @fieldType: String
	 * @Description: path of the example
	 */
	private static String		EXMAPLE_PATH	= "D:/lab/final/question_sample/";

	/**
	 * @fieldName: SESSION_NUM
	 * @fieldType: int
	 * @Description: number of Session in Lucene
	 */
	private static int			SESSION_NUM		= 9828;

	private ArrayList<Session>	exampleSessions	= new ArrayList<Session>();

	private ArrayList<Email>	exampleEmails	= new ArrayList<Email>();

	public void select() {
		Random random = new Random();

		SessionDao sessionDao = new SessionDao();
		HashSet<Integer> ids = new HashSet<Integer>();
		// ids.add(544);
		while (ids.size() < EXAMPLE_NUM) {
			int id = (Math.abs(random.nextInt()) % SESSION_NUM) + 1;
			ids.add(id);
		}

		for (Integer id : ids) {
			// System.out.println(id);
			Session session = sessionDao.getSessionById(id);
			exampleSessions.add(session);

			ArrayList<Email> tempList = sessionDao.getEmailsOfSession(session);
			exampleEmails.addAll(tempList);
		}

		writeContent();
	}

	private void writeContent() {
		for (Session session : exampleSessions) {
			String fileName = session.getSessionID();
			String path = EXMAPLE_PATH + fileName;

			SessionDao sessionDao = new SessionDao();
			ArrayList<Email> tempList = sessionDao.getEmailsOfSession(session);
			int num = 1;
			for (Email email : tempList) {
				String path2 = path + num + ".txt";
				num++;
				File file = new File(path2);
				if (!file.exists()) {
					try {
						file.createNewFile();
					}
					catch (IOException e) {
						e.printStackTrace();
					}
				}
				WriteFile.writeStringToFile(email.getContent(), path2);

			}

		}
	}

	public ArrayList<Session> getExampleSessions() {
		return exampleSessions;
	}

	public void setExampleSessions(ArrayList<Session> exampleSessions) {
		this.exampleSessions = exampleSessions;
	}

	public ArrayList<Email> getExampleEmails() {
		return exampleEmails;
	}

	public void setExampleEmails(ArrayList<Email> exampleEmails) {
		this.exampleEmails = exampleEmails;
	}

	public static void main(String args[]) {
		new ExampleSelector().select();
	}

}
