/**
 * File-Name:CommonService.java
 * 
 * Created on 2011-6-2 下午03:50:21
 * 
 * @author: Neo (neolimeng@gmail.com) Software Engineering Institute, Peking
 *          University, China
 * 
 *          Copyright (c) 2009, Peking University
 * 
 * 
 */
package neoutil;

import java.sql.Connection;

import org.apache.commons.dbutils.DbUtils;

/**
 * Description:
 * 
 * @author: Neo (neolimeng@gmail.com) Software Engineering Institute, Peking
 *          University, China
 * @version 1.0 2011-6-2 03:50:21
 */
public class CommonService {

	public static final String	SUCCESS		= "success";
	public static final String	FAIL		= "fail";
	public static final String	EXCEPTION	= "exception";

	protected static void closeConnection(Connection connection) {
		try {
			DbUtils.close(connection);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
