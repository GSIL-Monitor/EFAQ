package cn.edu.pku.sei.jinyong.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;

/**
 * �����ݿ��ȡ���÷����Ĺ�����
 * 
 * @author ������
 * 
 */

public class DAOUtils {

	public static Connection getConnection() throws SQLException, ClassNotFoundException {
		return JDBCPool.getConnection();
	}

	/**
	 * @author ������
	 *         select����ѯ���õ��Ľ��ֱ�Ӱ�װ��javaBean��ʽ����֤�㶨���T���������к
	 *         ���ݿ�ͬ�������getter��setter���� �μ�main����
	 * @throws ClassNotFoundException
	 */
	public static <T> List<T> getResult(Class<T> t, String sqlString, Object... params)
			throws SQLException, ClassNotFoundException {
		Connection connection2 = JDBCPool.getConnection();
		ResultSetHandler<List<T>> h = new BeanListHandler<T>(t);
		QueryRunner runner = new QueryRunner();
		List<T> result = runner.query(connection2, sqlString, h, params);
		DbUtils.close(connection2);
		return result;
	}

	public static <T> T getResult2(Class<T> t, String sqlString, Object... params)
			throws SQLException, ClassNotFoundException {
		Connection connection2 = JDBCPool.getConnection();
		ResultSetHandler<List<T>> h = new BeanListHandler<T>(t);
		QueryRunner runner = new QueryRunner();
		List<T> result = runner.query(connection2, sqlString, h, params);
		DbUtils.close(connection2);
		return result.get(0);
	}

	/**
	 * @author ������ ͬ��ִ��update��� �μ�main����
	 * @throws ClassNotFoundException
	 */
	public static int update(String sqlString, Object... params) throws SQLException,
			ClassNotFoundException {
		Connection connection2 = JDBCPool.getConnection();
		QueryRunner runner = new QueryRunner();
		int result = runner.update(connection2, sqlString, params);
		DbUtils.close(connection2);
		return result;
	}

	// /**
	// * @author ������
	// * �첽ִ��update���
	// * �д���д����ʱ����ʹ���첽����
	// */
	// public static void AsyncUpdate(Connection connection, String sqlString,
	// Object... params) {
	// ExecutorCompletionService<Integer> executor =
	// new ExecutorCompletionService<Integer>(Executors.newCachedThreadPool());
	// AsyncQueryRunner asyncRun = new AsyncQueryRunner(executor);
	//
	//
	// }

	public static void main(String[] args) {
	}
}
