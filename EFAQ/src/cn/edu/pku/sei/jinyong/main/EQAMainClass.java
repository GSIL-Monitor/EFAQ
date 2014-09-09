package cn.edu.pku.sei.jinyong.main;

import java.util.ArrayList;

import cn.edu.pku.sei.jinyong.answer.AnswerSelector;
import cn.edu.pku.sei.jinyong.content.MessageProcess;
import cn.edu.pku.sei.jinyong.dao.DocumentDao;
import cn.edu.pku.sei.jinyong.dao.MessageDao;
import cn.edu.pku.sei.jinyong.dao.SegDao;
import cn.edu.pku.sei.jinyong.dao.SessionDao;
import cn.edu.pku.sei.jinyong.entity.Document;
import cn.edu.pku.sei.jinyong.entity.Email;
import cn.edu.pku.sei.jinyong.entity.Seg;
import cn.edu.pku.sei.jinyong.entity.Segment;
import cn.edu.pku.sei.jinyong.entity.Session;
import cn.edu.pku.sei.jinyong.question.QuestionClassifier;
import cn.edu.pku.sei.jinyong.tag.Keywords;

/**
 * @ClassName: EQAMainClass
 * @Description: TODO main
 * @author: left
 * @date: 2014.4.19 7:33:10
 */

public class EQAMainClass {

	public ArrayList<Session>	sessionList			= new ArrayList<Session>();

	public SegDao				segDao				= new SegDao();

	public DocumentDao			documentDao			= new DocumentDao();

	public MessageDao			messageDao			= new MessageDao();

	public SessionDao			sessionDao			= new SessionDao();

	public MessageProcess		messageProcess		= new MessageProcess();

	public QuestionClassifier	questionClassifier	= new QuestionClassifier();

	public AnswerSelector		answerSelector		= new AnswerSelector();

	public EQAMainClass() {
	
		sessionList = sessionDao.getAllSession();
	}

	/**
	 * @Title:EQAMainClass
	 * @Description:TODO 对一个会话进行处理 流程如下 1.找出提问邮件 2.找出所有非提问作者的回复，即答复邮件
	 *                   3.对以上邮件进行内容分类(存数据库） 4.从提问邮件中抽取出问题句 5.从提问邮件中抽取关键词构造查询
	 *                   6.从答复邮件中检索最相关的段落
	 * @param session
	 */

	public void processOneSession(Session session) {
		
		Email qMail = Session.getPromoter(session);
	
		if (qMail == null || qMail.getMessageID() == null)
			return;
	
		String participants[] = session.getParticipants().split(",");
		if(participants.length <= 1) return ;
		
		ArrayList<Email> rMailList = new ArrayList<Email>();
		ArrayList<Email> allMailList = sessionDao.getEmailsOfSession(session);
		if(allMailList.size() <= 1 || allMailList.size() >= 15) return;

		for (Email e : allMailList) {
			if (e.getFromEmail() != null && !e.getFromEmail().equals(qMail.getFromEmail())) {
				rMailList.add(e);
			}
		}
		if(rMailList.size() == 0) return;

		Document doc = new Document();
		doc.setQmail_uuid(qMail.getMessageID());
		doc.setSession_uuid(session.getSessionID());

	
		messageProcess.process(qMail);
		
		String question = questionClassifier.getQuestionSentence(qMail);
		System.out.println(qMail.getContent());
		System.out.println(qMail.getEmailContent());
		doc.setQuestion(question);
		String keywords = getKeywords(qMail,question);
		doc.setKeywords(keywords);
		
	
		ArrayList<Segment> segmentList = qMail.getEmailContent().getSegments();
		for (int i = 0; i < segmentList.size(); i++) {
			Segment segment = segmentList.get(i);
			Seg seg = new Seg();
			seg.setContent(segment.getContentText());
			seg.setMessage_uuid(qMail.getMessageID());
			seg.setSession_uuid(session.getSessionID());
			seg.setSegment_type(segment.getContentType());
			seg.setSegment_no(i + 1);
			segDao.insertSegment(seg);
		
			if (seg.getSegment_type() == Segment.NORMAL_CONTENT
					&& seg.getContent().contains(question)) {
				doc.setQuestion_seg_no(i + 1);
			}
		}

	
		for (Email e : rMailList) {
			messageProcess.process(e);
			ArrayList<Segment> segList = e.getEmailContent().getSegments();
			for(int i = 0; i < segList.size(); i++) {
				Segment segment = segList.get(i);
				Seg seg = new Seg();
				seg.setContent(segment.getContentText());
				seg.setMessage_uuid(e.getMessageID());
				seg.setSession_uuid(session.getSessionID());
				seg.setSegment_type(segment.getContentType());
				seg.setSegment_no(i + 1);
				segDao.insertSegment(seg);
			}
		}

		
		Email aMail = answerSelector.getAnswerMail(qMail, question, rMailList);
		doc.setAmail_uuid(aMail.getMessageID());
	
		documentDao.insertDocument(doc);
		System.out.println(doc.getAmail_uuid());
	}
	
	/** 
	 * @Title:EQAMainClass
	 * @Description:TODO extract the key words
	 * @param qMail
	 * @return
	 */
	
	private String getKeywords(Email qMail,String question) {
		Keywords kw = new Keywords();
		String subject = qMail.getSubject();
		String str = subject;
		if(!subject.equals(question)) str += " " + question;
		String words[] = str.split(" ");
		
		String result = "";
		
		for(String word : words) {
			
			word = word.toLowerCase();
			if(!kw.isFunctionWords(word) && !kw.isStopWords(word)) {
				System.out.println(word+"###");
				result += word + " ";
				continue;
			}
			if(kw.isProjectWords(word)) {
				result += word + " ";
				continue;
			}
			
		}
		return result;
	}

	public static void main(String args[]) {
		EQAMainClass eqa = new EQAMainClass();
//		for(int i = 0 ; i < 100; i++) {
		for(int i = 0; i < eqa.sessionList.size(); i++) {
			Session s = eqa.sessionList.get(i);
			//System.out.println(s.getSubject());
			eqa.processOneSession(s);
		}
	}
}
