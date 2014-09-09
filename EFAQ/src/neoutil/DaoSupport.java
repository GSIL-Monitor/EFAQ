/**
 * File-Name:DaoSupport.java
 * 
 * Created on 2010-12-20 下午08:22:55
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
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

/**
 * Description:
 * 
 * @author: Neo (neolimeng@gmail.com) Software Engineering Institute, Peking
 *          University, China
 * @version 1.0 2010-12-20 08:22:55
 */
public class DaoSupport {

	private static Logger	log				= Logger.getLogger(DaoSupport.class);
	private static BoneCP	connectionPool	= null;

	private static void initPool(Database database) {
		if (connectionPool != null) {
			return;
		}
		log.info("========== 初始化数据库连接池！ ==========");
		try {
			// load the database driver (make sure this is in your classpath!)
			Class.forName("com.mysql.jdbc.Driver");
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// setup the connection pool
			BoneCPConfig config = new BoneCPConfig();
			config.setJdbcUrl(database.getUrl()); // jdbc url specific to
			// your database, eg
			// jdbc:mysql://127.0.0.1/yourdb
			config.setUsername(database.getUserName());
			config.setPassword(database.getPassword());
			config.setMinConnectionsPerPartition(1);
			config.setMaxConnectionsPerPartition(3);
			config.setPartitionCount(1);
			config.setAcquireIncrement(1);
			config.setAcquireRetryAttempts(3);
			connectionPool = new BoneCP(config); // setup the connection pool
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void shutdownPool() {
		try {
			connectionPool.shutdown(); // shutdown connection pool.
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Description: get the connection to the default database.
	 * 
	 * @return Connection
	 */
	public static synchronized Connection getConnection() {
		Database database = Database.create(Database.DEFAULT);
		return getConnection(database);
	}

	/**
	 * 
	 * Description: get the connection to the database
	 * 
	 * @param database
	 *            
	 * @return Connection
	 */
	public static synchronized Connection getConnection2(Database database) {
		Connection connection = null;
		// 加载驱动程序以连接数据库
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection(database.getUrl(), database.getUserName(),
					database.getPassword());
		}
		// 捕获加载驱动程序异常
		catch (ClassNotFoundException cnfex) {
			System.err.println("装载 JDBC/ODBC 驱动程序失败");
			cnfex.printStackTrace();
		}
		// 捕获连接数据库异常
		catch (SQLException sqlex) {
			System.err.println("无法连接数据库");
			sqlex.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return connection;
	}

	public static synchronized Connection getConnection(Database database) {
		initPool(database);

		Connection connection = null;
		try {
			connection = connectionPool.getConnection(); // fetch a connection
			// System.err.println("TotalCreatedConnectionsNum:"
			// + connectionPool.getTotalCreatedConnections());
			return connection;
		}
		catch (Exception e) {
			e.printStackTrace();
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
		Connection conn = DaoSupport.getConnection();
		try {
			conn.close();
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
