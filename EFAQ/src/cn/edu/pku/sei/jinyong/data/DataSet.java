package cn.edu.pku.sei.jinyong.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import cn.edu.pku.sei.jinyong.content.SegmentClassifier;
import cn.edu.pku.sei.jinyong.content.SegmentSpliter;
import cn.edu.pku.sei.jinyong.content.SentenceProcess;
import cn.edu.pku.sei.jinyong.dao.MessageDao;
import cn.edu.pku.sei.jinyong.dao.SessionDao;
import cn.edu.pku.sei.jinyong.dao.TagDao;
import cn.edu.pku.sei.jinyong.entity.Email;
import cn.edu.pku.sei.jinyong.entity.Segment;
import cn.edu.pku.sei.jinyong.entity.Sentence;
import cn.edu.pku.sei.jinyong.entity.Session;
import cn.edu.pku.sei.jinyong.entity.Tag;
import cn.edu.pku.sei.jinyong.utils.WriteFile;

/**
 * @ClassName: DataSet
 * @Description: TODO 
 * @author: left
 * @date: 2014.3.11 7:35:16
 */

public class DataSet {

	public static final int		SESSION_NUM			= 9828;

	public static final int		EXAMPLE_NUM			= 1000;

	public static final String	STORE_PATH			= "D:/lab/final/question_sample/";

	public SessionDao			sd					= new SessionDao();

	public MessageDao			md					= new MessageDao();

	SegmentClassifier			segmentClassifier	= new SegmentClassifier();

	SegmentSpliter				segmentSpliter		= new SegmentSpliter();

	public HashSet<Integer>		sessionIds			= new HashSet<Integer>();

	/**
	 * @Title:DataSet
	 * @Description: preprocess
	 * @param e
	 */

	public void preProcess(Email e) {
		segmentSpliter.process(e);
		segmentClassifier.process(e);
		SentenceProcess.splitSentence(e);
	}

	public void selectSessions() {
		System.out.println("start select sessions...");
		Random random = new Random();

		while (sessionIds.size() < EXAMPLE_NUM) {
			int id = (Math.abs(random.nextInt()) % SESSION_NUM) + 1;
			sessionIds.add(id);
		}
		System.out.println("select sessions num is :" + sessionIds.size());

		// write session ids to file
		System.out.println("start store session id to file...");
		String idPath = STORE_PATH + "sessionId.txt";
		StringBuilder sb = new StringBuilder();
		for (Integer id : sessionIds) {
			sb.append(id + "\n");
		}
		WriteFile.writeStringToFile(sb.toString(), idPath);

	}

	public void writeEmails() {
		if (sessionIds == null || sessionIds.size() == 0)
			selectSessions();
		TagDao td = new TagDao();
		for (Integer id : sessionIds) {

			Session tmp = sd.getSessionById(id);
			Email e = Session.getPromoter(tmp);
			if (e == null || e.getContent() == null || e.getContent().length() == 0) {
				continue;
			}

			System.out.println("start write email : " + e.getSubject() + " sessionId : " + id);
			preProcess(e);

			StringBuilder sb = new StringBuilder();
			StringBuilder sb2 = new StringBuilder();
			sb.append("title:" + e.getSubject() + "\n");
			sb.append("MessageId:" + e.getMessageID() + "\n");
			sb.append("SessionId:" + tmp.getSessionID() + "\n");

			ArrayList<Segment> segments = e.getEmailContent().getSegments();
			for (Segment segment : segments) {
				if (segment.getContentType() == Segment.NORMAL_CONTENT) {
					ArrayList<Sentence> sentences = segment.getSentences();
					for (Sentence sentence : sentences) {
						sb.append(sentence.toString() + "\n");
						sb2.append(sentence.toString() + "\n");
					}
				}
			}

			String path = STORE_PATH + "session_id_" + id + ".txt";
			WriteFile.writeStringToFile(sb.toString(), path);

			String path2 = STORE_PATH + "session_id_" + id + "_old.txt";
			WriteFile.writeStringToFile(e.getContent(), path2);

			// store to database
			System.out.println("Start store email : " + e.getSubject() + " sessionId : " + id
					+ " to database...");
			Tag tag = new Tag();
			tag.setSubject(e.getSubject());
			tag.setMessage_uuid(e.getMessageID());
			tag.setSession_uuid(tmp.getSessionID());
			tag.setContent(sb2.toString());
			td.insertTag(tag);

		}
	}

	public static void main(String args[]) {
		DataSet ds = new DataSet();
		ds.selectSessions();
		ds.writeEmails();
	}

}
