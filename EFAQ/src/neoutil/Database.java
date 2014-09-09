/**
 * File-Name:Database.java
 * 
 * Created on 2010-12-20 下午08:30:40
 * 
 * @author: Neo (neolimeng@gmail.com) Software Engineering Institute, Peking
 *          University, China
 * 
 *          Copyright (c) 2009, Peking University
 * 
 * 
 */
package neoutil;

/**
 * Description:
 * 
 * @author: Neo (neolimeng@gmail.com) Software Engineering Institute, Peking
 *          University, China
 * @version 1.0 2010-12-20 08:30:40
 */
public class Database {

	public static String	DEFAULT			= "DEFAULT";
	public static String	DEFAULT_URL		= Config.getDBUrl();
	public static String	DEFAULT_USER	= Config.getDBUser();
	public static String	DEFAULT_PASS	= Config.getDBPassword();
	private String			name;
	private String			url;
	private String			userName;
	private String			password;

	public static Database create() {
		return create(DEFAULT);
	}

	public static Database create(String dbName) {
		Database database = new Database();
		if (DEFAULT.equals(dbName)) {
			database.setName(DEFAULT);
			database.setUrl(DEFAULT_URL);
			database.setUserName(DEFAULT_USER);
			database.setPassword(DEFAULT_PASS);
			return database;
		}
		else {
			// TODO 处理其他数据库
			return null;
		}
	}

	/**
	 * Description:
	 * 
	 * @param args
	 *            void
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
