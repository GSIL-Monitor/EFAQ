/**
 * File-Name:Config.java
 * 
 * Created on 2010-12-21 下午05:15:16
 * 
 * @author: Neo (neolimeng@gmail.com) Software Engineering Institute, Peking
 *          University, China
 * 
 *          Copyright (c) 2009, Peking University
 * 
 * 
 */
package neoutil;

import java.util.ResourceBundle;

/**
 * Description:
 * 
 * @author: Neo (neolimeng@gmail.com) Software Engineering Institute, Peking
 *          University, China
 * @version 1.0 2010-12-21 05:15:16
 */
public class Config {

	// 属性文件标识符
	private static String			CONFIG_FILE_NAME	= "app";

	// 所使用的ResourceBundle
	private static ResourceBundle	bundle;

	// 静态私有方法，用于从属性文件中取得属性值
	static {
		try {
			bundle = ResourceBundle.getBundle(CONFIG_FILE_NAME);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 静态私有方法，用于从属性文件中取得属性值
	private static String getValue(String key) {
		return bundle.getString(key);
	}

	// public static String getNewsDir()
	// {
	// return getValue("newsDir");
	// }

	public static String getResourceEvidenceDir() {
		return getValue("resourceEvidenceDir");
	}

	public static String getRasDir() {
		return getValue("rasDir");
	}

	public static String getXsdDir() {
		return getValue("xsdDir");
	}

	public static String getDBUrl() {
		return getValue("jdbc.url");
	}

	public static String getDBUser() {
		return getValue("jdbc.username");
	}

	public static String getDBPassword() {
		return getValue("jdbc.password");
	}

	public static String getIndexPath() {
		return getValue("index.path");
	}

	public static String getRelatedTextIndexPath() {
		return getValue("index.path.related");
	}

	public static String getCrawlerDbUrl() {
		return getValue("crawler.db.url");
	}

	public static String getCrawlerDbUserName() {
		return getValue("crawler.db.username");
	}

	public static String getCrawlerDbPassword() {
		return getValue("crawler.db.password");
	}

	public static Integer getDefaultConnectTimeOut() {
		return Integer.parseInt(getValue("app.default.connect.time.out"));
	}

	public static Integer getDefaultGetRequestTimeOut() {
		return Integer.parseInt(getValue("app.default.get.request.time.out"));
	}

	public static boolean getUseProxyOrNot() {
		return Boolean.parseBoolean(getValue("app.proxy.useOrNot"));
	}

	public static String getProxyUrl() {
		return getValue("app.proxy.url");
	}

	public static Integer getProxyPort() {
		return Integer.parseInt(getValue("app.proxy.port"));
	}

	public static String getPaperStorePathPrefix() {
		return getValue("paper.storePath.prefix");
	}

	/**
	 * Description:
	 * 
	 * @param args
	 *            void
	 */
	public static void main(String[] args) {
		System.out.println(Config.getRasDir());
	}

}
