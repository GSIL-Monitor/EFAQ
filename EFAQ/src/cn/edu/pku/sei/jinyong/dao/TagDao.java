package cn.edu.pku.sei.jinyong.dao;

import java.util.ArrayList;

import cn.edu.pku.sei.jinyong.entity.Tag;

public class TagDao {

	public void insertTag(Tag tag) {
		String insertSql = "insert into tag (session_uuid,session_id,message_id,message_uuid,raw_text,tagged_text,subject,content) values "
				+ "(?,?,?,?,?,?,?,?)";
		try {
			DAOUtils.update(insertSql, tag.getSession_uuid(), tag.getSession_id(),
					tag.getMessage_id(), tag.getMessage_uuid(), tag.getRaw_text(),
					tag.getTagged_text(), tag.getSubject(), tag.getContent());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateTag(Tag tag) {
		String updateSql = "update tag set tagged_text = ? where session_uuid = ?";
		try {
			DAOUtils.update(updateSql, tag.getTagged_text(), tag.getSession_uuid());

		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<Tag> getAllTags() {
		String sql = "select * from tag";
		ArrayList<Tag> tagList = new ArrayList<Tag>();
		try {
			tagList = (ArrayList<Tag>) DAOUtils.getResult(Tag.class, sql);
		}
		catch (Exception e) {

		}
		return tagList;
	}

	public Tag getTagById(int id) {
		String sql = "select * from tag where id = ?";
		Tag tag = new Tag();

		try {
			tag = (Tag) DAOUtils.getResult2(Tag.class, sql, id);
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return tag;

	}

	public static void main(String args[]) {
		Tag tag = new Tag();

		TagDao td = new TagDao();
		tag = td.getTagById(1);
		tag.setRaw_text("this is a test");
		td.updateTag(tag);

	}

}
