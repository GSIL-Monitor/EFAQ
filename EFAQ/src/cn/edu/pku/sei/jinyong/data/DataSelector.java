package cn.edu.pku.sei.jinyong.data;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import cn.edu.pku.sei.jinyong.dao.MessageDao;
import cn.edu.pku.sei.jinyong.entity.Email;
import cn.edu.pku.sei.jinyong.utils.WriteFile;

public class DataSelector {

	private static String	STORE_PATH	= "D:/lab/final/examples/";
	private static int		TOTAL_COUNT	= 51125;
	private static int		EXAMPLE_NUM	= 1000;

	public static void select() {
		ArrayList<Email> emails = selectEmails();

		for (int i = 0; i < emails.size(); i++) {
			String path = STORE_PATH + i + ".txt";
			Email e = emails.get(i);
			WriteFile.writeStringToFile(e.getContent(), path);
		}
	}

	public static ArrayList<Email> selectEmails() {
		ArrayList<Email> emails = new ArrayList<Email>();
		Set<Integer> tmpSet = new HashSet<Integer>();
		Random random = new Random(System.currentTimeMillis());
		MessageDao md = new MessageDao();
		while (tmpSet.size() < EXAMPLE_NUM) {
			int rdm = Math.abs(random.nextInt() % TOTAL_COUNT + 1);
			if (tmpSet.contains(rdm)) {
				continue;
			}
			else {
				tmpSet.add(rdm);
			}
			Email email = md.getEmailById(rdm);
			emails.add(email);
		}

		return emails;
	}

	public static void main(String args[]) {
		select();
	}

}
