package com.itstyle.blog.common.dynamicquery;

import java.util.List;

/**
 * 扩展SpringDataJpa, 支持动态jpql/nativesql查询并支持分页查询
 * 使用方法：注入ServiceImpl
 * 创建者 小柒2012
 * 创建时间	2018年3月8日
 */
public interface DynamicQuery {

	public void save(Object entity);

	public void update(Object entity);

	/**
	 * 执行nativeSql的update,delete操作
	 * @param nativeSql
	 * @param params
	 * @return
	 */
	int nativeExecuteUpdate(String nativeSql, Object... params);

	/**
	 * 执行nativeSql查询一行
	 * @param resultClass 查询结果类型
	 * @param nativeSql
	 * @param params 占位符参数(例如?1)绑定的参数值
	 * @return
	 */
	<T> T nativeQuerySingleResult(Class<T> resultClass, String nativeSql, Object... params);

	/**
	 * 执行nativeSql查询
	 * @param nativeSql
	 * @param params 占位符参数(例如?1)绑定的参数值
	 * @return
	 */
	<T> List<T> query(String nativeSql, Object... params);

	/**
	 * 执行nativeSql查询List<Object[]>
	 * @param resultClass
	 * @param nativeSql
	 * @param params 占位符参数(例如?1)绑定的参数值
	 * @return
	 */
	<T> List<T> query(Class<T> resultClass, String nativeSql, Object... params);

}
