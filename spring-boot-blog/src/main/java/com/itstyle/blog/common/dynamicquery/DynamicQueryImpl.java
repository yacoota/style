package com.itstyle.blog.common.dynamicquery;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
/**
 * 动态jpql/nativesql查询的实现类
 * 创建者 小柒2012
 * 创建时间	2018年3月8日
 */
@Repository
public class DynamicQueryImpl implements DynamicQuery {

	Logger logger = LoggerFactory.getLogger(DynamicQueryImpl.class);

	@PersistenceContext
	private EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	@Override
	public void save(Object entity) {
		em.persist(entity);
	}

	@Override
	public void update(Object entity) {
		em.merge(entity);
	}

	private Query createNativeQuery(String sql, Object... params) {
		Query q = em.createNativeQuery(sql);
		if (params != null && params.length > 0) {
			for (int i = 0; i < params.length; i++) {
				q.setParameter(i + 1, params[i]); // 与Hiberante不同,jpa
				// query从位置1开始
			}
		}
		return q;
	}

	@Override
	public int nativeExecuteUpdate(String nativeSql, Object... params) {
		return createNativeQuery(nativeSql, params).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T nativeQuerySingleResult(Class<T> resultClass, String nativeSql, Object... params) {
		Query q = createNativeQuery(resultClass, nativeSql, params);
		List<T> list = q.getResultList();
		if (list.isEmpty()) {
			return null;
		}
		return list.get(0);
	}


	private <T> Query createNativeQuery(Class<T> resultClass, String sql, Object... params) {
		Query q = null;
		if (resultClass == null) {
			q = em.createNativeQuery(sql);
		} else {
			q = em.createNativeQuery(sql, resultClass);
		}
		for (int i = 0; i < params.length; i++) {
			// 与Hiberante不同,jpa query从位置1开始
			q.setParameter(i + 1, params[i]);
		}
		return q;
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T> List<T> query(String nativeSql, Object... params) {
		Query q = createNativeQuery(null, nativeSql, params);
		return q.getResultList();
	}
    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> query(Class<T> resultClass, String nativeSql, Object... params) {
        Query q = createNativeQuery(resultClass, nativeSql, params);
        return q.getResultList();
    }

}
