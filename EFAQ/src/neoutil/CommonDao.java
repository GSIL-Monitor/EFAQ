/**
 * File-Name:CommonDao.java
 * 
 * Created on 2010-12-21 上午11:08:51
 * 
 * @author: Neo (neolimeng@gmail.com) Software Engineering Institute, Peking
 *          University, China
 * 
 *          Copyright (c) 2009, Peking University
 * 
 * 
 */
package neoutil;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.apache.log4j.Logger;

/**
 * Description: Zhuzixiao added following methods: deleteById,deleteByUuid;
 * updateById,UpdateByUuid,update,updateAll; Two version of
 * getBy(queryKeys, queryValues) 2011-11-27
 * 
 * @author: Neo (neolimeng@gmail.com) Software Engineering Institute, Peking
 *          University, China
 * @version 1.0 2010-12-21 11:08:51
 */

public class CommonDao<T, PK extends Serializable> {

	protected static Logger	log	= Logger.getLogger(CommonDao.class);
	private Class<T>		t;
	private Connection		connection;

	public CommonDao(Class<T> t, Connection connection) {
		if (t == null || connection == null) {
			log.error("CommonDao中传入的参数为null！！");
		}
		else {
			this.t = t;
			this.connection = connection;
		}
	}

	/**
	 * Description: insert to database。
	 * 
	 * @param keys
	 *            key to insert
	 * @param values
	 *            nalue to insert
	 * @throws SQLException
	 */
	@Deprecated
	public void save(Object[] keys, Object[] values) throws SQLException {
		StringBuffer sql = new StringBuffer();
		StringBuffer valuePart = new StringBuffer();

		sql.append("INSERT INTO " + t.getSimpleName().toLowerCase() + "(");
		valuePart.append(") VALUES(");

		if (keys.length != values.length) {
			log.error("save方法传入的Key和Value个数不相同！");
			return;
		}

		for (int i = 0; i < keys.length; i++) {
			sql.append(keys[i]);
			valuePart.append("?");

			if (i < keys.length - 1) {
				sql.append(", ");
				valuePart.append(", ");
			}
			else {
				sql.append(valuePart.toString() + ")");
			}
		}

		execute(sql.toString(), values);
	}

	/**
	 * Description: getBy:QueryKeys=QueryValues
	 * 
	 * @author:  (sei_zzx@126.com)
	 * 
	 * @param queryKeys
	 * @param queryValues
	 * @return List<T>
	 */
	public List<T> getBy(Object[] queryKeys, Object[] queryValues) {
		String sql = generateSqlForGetBy(queryKeys, queryValues);

		return find(sql, queryValues);
	}

	public List<T> getBy(String queryKey, Object queryValue) {
		String[] queryKeys = new String[] { queryKey };
		Object[] queryValues = new Object[] { queryValue };

		return getBy(queryKeys, queryValues);
	}

	public Page<T> getBy(final Page<T> page, Object[] queryKeys, Object[] queryValues) {
		String sql = generateSqlForGetBy(queryKeys, queryValues);
		return find(page, sql, queryValues);
	}

	public Page<T> getBy(final Page<T> page, String queryKey, Object queryValue) {
		String[] queryKeys = new String[] { queryKey };
		Object[] queryValues = new Object[] { queryValue };

		return getBy(page, queryKeys, queryValues);
	}

	public T getById(Long id) {
		List<T> ts = getBy("id", id);
		if (ts == null) {
			return null;
		}
		else if (ts.size() == 1) {
			return ts.get(0);
		}
		else {
			log.error("查询到的个数不唯一，无法得到唯一确定值！");
			return null;
		}
	}

	public T getByUuid(String uuid) {
		List<T> ts = getBy("uuid", uuid);
		if (ts == null) {
			return null;
		}
		else if (ts.size() == 1) {
			return ts.get(0);
		}
		else if (ts.size() <= 0) {
			log.error("未查询到结果！");
			return null;
		}
		else if (ts.size() > 1) {
			log.error("查询到的个数大于1，无法得到唯一确定值！");
			return null;
		}

		return null;
	}

	public List<T> getAll() {
		String sql = "SELECT * FROM " + t.getSimpleName();
		return find(sql);
	}

	public Page<T> getAll(final Page<T> page) {
		String sql = "SELECT * FROM " + t.getSimpleName();
		return find(page, sql);
	}

	private String generateSqlForGetBy(Object[] queryKeys, Object[] queryValues) {
		StringBuilder sql = new StringBuilder("SELECT * FROM " + t.getSimpleName().toLowerCase());
		StringBuilder whereSubSql = new StringBuilder(" WHERE ");

		if (queryKeys.length != queryValues.length) {
			log.error("selectQuery（getBy方法）的参数中Key和Value个数不相同！");
			return null;
		}

		for (int i = 0; i < queryKeys.length; i++) {

			if (i < queryKeys.length - 1) {
				whereSubSql.append(queryKeys[i] + "=? and ");
			}
			else {
				whereSubSql.append(queryKeys[i] + "=? ");
			}
		}
		sql.append(whereSubSql);

		return sql.toString();
	}

	public void execute(String sql, Object... params) throws SQLException {
		Long start = System.currentTimeMillis();
		QueryRunner queryRunner = new QueryRunner();
		log.info("执行查询:" + sql);
		queryRunner.update(connection, sql, params);
		log.debug("execute方法执行的时间为：" + (System.currentTimeMillis() - start) + "ms。SQL语句为" + sql);
	}

	/**
	 * Description: delete uuid
	 * 
	 * @author: (sei_zzx@126.com)
	 * 
	 * @param uuid
	 * @throws SQLException
	 *             void
	 */
	public void deleteByUuid(String uuid) throws SQLException {
		String sql = "DELETE FROM " + t.getSimpleName().toLowerCase() + " WHERE uuid = ?";
		execute(sql, uuid);
	}

	/**
	 * 
	 * @Title: countALl
	 * 
	 * @Description: statistic
	 * 
	 * @param @return
	 * 
	 * @return int
	 * 
	 * @author jinyong
	 */

	public int countAll() {
		String sql = "SELECT COUNT(*) FROM " + t.getSimpleName().toLowerCase();
		return count(sql);
	}

	public int countAllForFakeDeletion() {
		String sql = "SELECT COUNT(*) FROM " + t.getSimpleName().toLowerCase()
				+ " WHERE entityStatus = 1";
		return count(sql);
	}

	public int count(String sql, Object... params) {
		Long start = System.currentTimeMillis();
		Object result = null;
		ScalarHandler handler = new ScalarHandler();
		QueryRunner queryRunner = new QueryRunner();

		try {
			result = queryRunner.query(connection, sql, handler, params);
			log.debug("count查询到的Entity个数为：" + result + "，执行的时间为："
					+ (System.currentTimeMillis() - start) + "ms。SQL语句为：" + sql);
			return ((Long) result).intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public List<T> find(String sql, Object... params) {

		if (sql == null || sql.equals("")) {
			log.error("Null sql statement in method: find!!");
			return null;
		}

		Long start = System.currentTimeMillis();
		List<T> entities = null;
		ResultSetHandler<List<T>> handler = new BeanListHandler(t);
		QueryRunner queryRunner = new QueryRunner();

		try {
			log.info("执行查询：" + sql);
			entities = queryRunner.query(connection, sql, handler, params);

			return entities;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if (entities != null) {
				log.debug("find查询到的Entity个数为：" + entities.size() + "，执行的时间为："
						+ (System.currentTimeMillis() - start) + "ms。SQL语句为：" + sql);
			}
			else {
				log.info("find未查询到任何Entity!SQL语句为：" + sql);
			}
		}
	}

	/**
	 * 
	 * @Title: columnFind
	 * 
	 * @Description: query value for a coloum
	 * 
	 * @param @param sql
	 * @param @param params
	 * @param @return
	 * 
	 * @return List<T>
	 * 
	 * @author jinyong
	 */

	public List<T> columnFind(String sql, Object... params) {

		if (sql == null || sql.equals("")) {
			log.error("Null sql statement in method: find!!");
			return null;
		}

		Long start = System.currentTimeMillis();
		List<T> entities = null;
		ColumnListHandler handler = new ColumnListHandler();
		QueryRunner queryRunner = new QueryRunner();

		try {
			entities = (List<T>) queryRunner.query(connection, sql, handler, params);

			return entities;
		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Page<T> find(final Page<T> page, String sql, Object... params) {

		if (sql == null || sql.equals("")) {
			log.error("Null sql statement in method: find!!");
			return null;
		}

		Long start = System.currentTimeMillis();
		ResultSetHandler<List<T>> handler = new BeanListHandler(t);
		QueryRunner queryRunner = new QueryRunner();
		int totalNum = 0;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;

		try {
			String countSql = "SELECT count(*) " + sql.substring(sql.toLowerCase().indexOf("from"));

			preparedStatement = connection.prepareStatement(countSql);
			queryRunner.fillStatement(preparedStatement, params);
			resultSet = preparedStatement.executeQuery();
			// TODO BUG!!!执行Group语句时会出错！！Group语句会返回一组数据。
			while (resultSet.next()) {
				totalNum = resultSet.getInt(1);
			}
			resultSet.close();
			preparedStatement.close();

			if (totalNum == 0) {
				page.setResult(new ArrayList<T>());
				page.setTotalCount(0);

				return page;
			}
			else {
				page.setTotalCount(totalNum);
				int startNum = (page.getPageNo() - 1) * page.getPageSize();
				int count = page.getPageSize();
				sql += " limit " + startNum + "," + count;
				List<T> entities = queryRunner.query(connection, sql, handler, params);
				page.setResult(entities);

				return page;
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		finally {
			if (totalNum != 0) {
				log.debug("find查询到的Entity个数为：" + page.getResult().size() + "，执行的时间为："
						+ (System.currentTimeMillis() - start) + "ms。SQL语句为：" + sql);
			}
			else {
				log.info("find未查询到任何Entity!SQL语句为：" + sql);
			}
		}
	}

	/**
	 * Description: need update's property：keysToSet，value：valuesToSet
	 * updatexx=?）：queryKeys，value：queryValues;
	 * 
	 * Example： update({"name","age"}, {"myNewName",21},{"name"},{"myOldName"});
	 * 
	 * generated sql string: update xxx set name='myNewName', age=21 where
	 * name="myOldName"
	 * 
	 * @author: (sei_zzx@126.com)
	 * 
	 * @param keysToSet
	 * @param valuesToSet
	 * @param queryKeys
	 * @param queryValues
	 * @throws SQLException
	 *             void
	 */
	@Deprecated
	public void update(Object[] keysToSet, Object[] valuesToSet, Object[] queryKeys,
			Object[] queryValues) throws SQLException {

		StringBuilder sqlString = new StringBuilder("UPDATE " + t.getSimpleName().toLowerCase());
		StringBuilder setSubSql = new StringBuilder(" SET ");
		StringBuilder whereSubSql = new StringBuilder(" WHERE ");

		if ((keysToSet.length != valuesToSet.length) && (queryKeys.length != queryValues.length)) {
			log.error("update方法传入的Key和Value个数不相同！");
			return;
		}

		for (int i = 0; i < keysToSet.length; i++) {
			setSubSql.append(keysToSet[i] + " = ? ");

			if (i < keysToSet.length - 1) {
				setSubSql.append(", ");
			}

		}

		for (int i = 0; i < queryKeys.length; i++) {
			whereSubSql.append(queryKeys[i] + " = ? ");

			if (i < queryKeys.length - 1) {
				whereSubSql.append(", ");
			}
		}

		sqlString.append(setSubSql);
		sqlString.append(whereSubSql);

		Object[] params = new Object[valuesToSet.length + queryValues.length];
		System.arraycopy(valuesToSet, 0, params, 0, valuesToSet.length);
		System.arraycopy(queryValues, 0, params, valuesToSet.length, queryValues.length);

		for (int i = 0; i < params.length; i++) {
			System.out.print(params[i] + "\t");
		}
		//
		System.out.println();
		//

		execute(sqlString.toString(), params);
	}

	/**
	 * Description: call the update method.
	 * 
	 * @author:  (sei_zzx@126.com)
	 * 
	 * @param keys
	 * @param values
	 * @param id
	 * @throws SQLException
	 *             void
	 */
	@Deprecated
	public void updateById(Object[] keys, Object[] values, long id) throws SQLException {
		String[] queryKeys = new String[] { "id" };
		Object[] queryValues = new Object[] { id };

		update(keys, values, queryKeys, queryValues);
	}

	/**
	 * Description: call the update method
	 * 
	 * @author:  (sei_zzx@126.com)
	 * 
	 * @param keys
	 * @param values
	 * @param uuid
	 * @throws SQLException
	 *             void
	 */
	@Deprecated
	public void updateByUuid(Object[] keys, Object[] values, String uuid) throws SQLException {
		String[] queryKeys = new String[] { "uuid" };
		Object[] queryValues = new Object[] { uuid };

		update(keys, values, queryKeys, queryValues);
	}

	/**
	 * Description: Another version of update. no where conditions, set all
	 * records to new value.
	 * 
	 * @author:  (sei_zzx@126.com)
	 * 
	 * @param keysToSet
	 * @param valuesToSet
	 * @throws SQLException
	 *             void
	 */
	@Deprecated
	public void updateAll(Object[] keysToSet, Object[] valuesToSet) throws SQLException {

		StringBuilder sqlString = new StringBuilder("UPDATE " + t.getSimpleName().toLowerCase());
		StringBuilder setSubSql = new StringBuilder(" SET ");

		if (keysToSet.length != valuesToSet.length) {
			log.error("update方法传入的Key和Value个数不相同！");
			return;
		}

		for (int i = 0; i < keysToSet.length; i++) {
			setSubSql.append(keysToSet[i] + " = ? ");

			if (i < keysToSet.length - 1) {
				setSubSql.append(", ");
			}
		}

		sqlString.append(setSubSql);

		execute(sqlString.toString(), valuesToSet);
	}

	/*
	 * public List<T> getListByKeysInOrdersTopK(Object[] queryKeys, Object[]
	 * queryValues, Object[] orderBys, Object[] orderTypes, int topK) { String
	 * sql = generateSqlForKeysOrdersTopK(queryKeys, queryValues, orderBys,
	 * orderTypes, topK);
	 * 
	 * return find(sql, queryValues); }
	 * 
	 * public Page<T> getPageByKeysInOrdersTopK(final Page<T> page, Object[]
	 * queryKeys, Object[] queryValues, Object[] orderBys, Object[] orderTypes,
	 * int topK) { String sql = generateSqlForKeysOrdersTopK(queryKeys,
	 * queryValues, orderBys, orderTypes, topK);
	 * 
	 * return find(page, sql, queryValues); }
	 * 
	 * public List<T> getListByKeysInOrdersTopK(Object queryKey, Object
	 * queryValue, Object orderBy, Object orderType, int topK) {
	 * 
	 * String sql = generateSqlForKeysOrdersTopK(new Object[] { queryKey }, new
	 * Object[] { queryValue }, new Object[] { orderBy }, new Object[] {
	 * orderType }, topK);
	 * 
	 * return find(sql, queryValue); }
	 * 
	 * public Page<T> getPageByKeysInOrdersTopK(final Page<T> page, Object
	 * queryKey, Object queryValue, Object orderBy, Object orderType, int topK)
	 * {
	 * 
	 * String sql = generateSqlForKeysOrdersTopK(new Object[] { queryKey }, new
	 * Object[] { queryValue }, new Object[] { orderBy }, new Object[] {
	 * orderType }, topK);
	 * 
	 * return find(page, sql, queryValue); }
	 */

	/*
	 * private String generateSqlForKeysOrdersTopK(Object[] queryKeys, Object[]
	 * queryValues, Object[] orderBys, Object[] orderTypes, int topK) { String
	 * baseSql = "SELECT * FROM " + t.getSimpleName().toLowerCase();
	 * StringBuilder querySql = new StringBuilder(); StringBuilder orderSql =
	 * new StringBuilder(); StringBuilder limitSql = new StringBuilder();
	 * 
	 * if (queryKeys != null && queryValues != null) { if (queryKeys.length !=
	 * queryValues.length) { log.error("个数不匹配：queryKeys和queryValues!"); return
	 * null; }
	 * 
	 * querySql.append(" WHERE ");
	 * 
	 * for (int i = 0; i < queryKeys.length; i++) {
	 * 
	 * querySql.append(queryKeys[i] + " = ?");
	 * 
	 * if (i < queryKeys.length - 1) { querySql.append(" , "); } }
	 * 
	 * } else if (queryKeys == null && queryValues == null) { querySql = new
	 * StringBuilder(""); }
	 * 
	 * if (orderBys != null && orderTypes != null) { if (orderBys.length !=
	 * orderTypes.length) { log.error("个数不匹配：orderBys和orderTypes!"); return
	 * null; }
	 * 
	 * orderSql.append(" ORDER BY "); for (int i = 0; i < orderBys.length; i++)
	 * { String order; if (orderBys[i] == null) { orderSql = new
	 * StringBuilder(""); break; }
	 * 
	 * if (orderTypes[i] == null || orderTypes[i].equals("")) order = "ASC";
	 * else order = orderTypes[i].toString().toUpperCase();
	 * 
	 * if (!order.equals("ASC") && !order.equals("DESC")) {
	 * log.error("排序类型的描述不正确(必须是ASC/DESC之一，大小写无关)!!!"); return null; }
	 * orderSql.append(orderBys[i] + " " + order);
	 * 
	 * if (i < queryKeys.length - 1) { orderSql.append(" , "); } } } else if
	 * (orderBys == null && orderTypes == null) { orderSql = new
	 * StringBuilder(""); }
	 * 
	 * if (topK <= 0) limitSql = new StringBuilder(""); else
	 * limitSql.append(" LIMIT " + topK);
	 * 
	 * StringBuilder sql = new StringBuilder(""); sql.append(baseSql);
	 * sql.append(querySql); sql.append(orderSql); sql.append(limitSql);
	 * 
	 * log.info(sql);
	 * 
	 * return sql.toString(); }
	 */
}
