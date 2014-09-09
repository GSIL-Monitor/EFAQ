package cn.edu.pku.sei.jinyong.dao;

import java.sql.Connection;
import java.sql.SQLException;

import cn.edu.pku.sei.jinyong.utils.Config;

import com.jolbox.bonecp.BoneCP;
import com.jolbox.bonecp.BoneCPConfig;

public class JDBCPool {

	private static BoneCP	connectionPool	= null;

	private static String	dbHost			= Config.getDbUrl();
	private static String	dbUser			= Config.getDbUsr();
	private static String	dbPassword		= Config.getDbPwd();

	private JDBCPool() throws SQLException {

	}

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		// if (connectionPool == null) {
		// System.out.println("DB pool is disconnected!\n用main
		// return null;
		// //initPool();
		// }
		if (connectionPool == null) {
			initPool();
		}
		return connectionPool.getConnection();
	}

	public static void shutDown() {
		if (connectionPool != null) {
			connectionPool.shutdown();
		}
	}

	public static synchronized void initPool() throws SQLException, ClassNotFoundException {
		System.out.println("initiating db pool...");
		if (connectionPool != null) {
			return;
		}
		Class.forName("com.mysql.jdbc.Driver");
		// setup the connection pool
		BoneCPConfig config = new BoneCPConfig();
		config.setJdbcUrl(dbHost); // jdbc url specific to your
									// database, eg
									// jdbc:mysql://127.0.0.1/yourdb
		config.setUsername(dbUser);
		config.setPassword(dbPassword);
		config.setMinConnectionsPerPartition(5);
		config.setMaxConnectionsPerPartition(10);
		config.setPartitionCount(1);
		connectionPool = new BoneCP(config); // setup the connection pool
		System.out.println("db pool initiated!");
	}

	public static void main(String[] args) {
		try {
			JDBCPool.initPool();
			Connection connection = JDBCPool.getConnection();
			System.out.println(connection);
		}
		catch (SQLException e) {
			System.out.println("connection pool initiation fail!");
			e.printStackTrace();
		}
		catch (ClassNotFoundException e) {
			System.out.println("connection pool initiation fail!");
			e.printStackTrace();
		}
	}
}
