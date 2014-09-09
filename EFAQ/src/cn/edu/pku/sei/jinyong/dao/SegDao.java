package cn.edu.pku.sei.jinyong.dao;

import cn.edu.pku.sei.jinyong.entity.Seg;

public class SegDao {

	public void insertSegment(Seg seg) {
		String sql = "insert into segment (message_uuid,session_uuid,segment_no,segment_type,content) values (?,?,?,?,?)";

		try {
			DAOUtils.update(sql, seg.getMessage_uuid(), seg.getSession_uuid(), seg.getSegment_no(),
					seg.getSegment_type(), seg.getContent());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
