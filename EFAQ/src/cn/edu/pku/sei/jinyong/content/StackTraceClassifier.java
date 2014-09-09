package cn.edu.pku.sei.jinyong.content;

import java.util.ArrayList;

import cn.edu.pku.sei.jinyong.content.stacktrace.StackJudge;
import cn.edu.pku.sei.jinyong.entity.Email;
import cn.edu.pku.sei.jinyong.entity.Segment;

public class StackTraceClassifier implements CommonClassifier {

	@Override
	public void getClassificationType(Email e) {
		// TODO Auto-generated method stub
		ArrayList<Segment> segments = e.getEmailContent().getSegments();
		for (Segment segment : segments) {
			if (segment.getContentType() == Segment.NORMAL_CONTENT) {
				if (StackJudge.isStackTrace(segment)) {
					segment.setContentType(Segment.STACK_CONTENT);
				//	segment = StackJudge.shortStackTrace(segment);
				}
			}
		}
	}

}
