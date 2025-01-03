package br.com.alugamais.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public abstract class AbstractDao<T, PK extends Serializable> {

    @SuppressWarnings("unchecked")
    private final Class<T> entityClass =
            (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];

    @PersistenceContext
    private EntityManager entityManager;

    protected EntityManager getEntityManager() {
        return entityManager;
    }

    public void save(T entity) {

        entityManager.persist(entity);
    }

    public void update(T entity) {

        entityManager.merge(entity);
    }

    public void delete(PK id) {

        entityManager.remove(entityManager.getReference(entityClass, id));
    }

    public T findById(PK id) {

        return entityManager.find(entityClass, id);
    }

    public List<T> findAll() {

        return entityManager
                .createQuery("from " + entityClass.getSimpleName(), entityClass)
                .getResultList();
    }

    protected List<T> createQuery(String jpql, Object... params) {
        TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getResultList();
    }

    protected List<Object[]> createNativeQuery(String jpql, Object... params) {
        Query query = entityManager.createNativeQuery(jpql);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getResultList();
    }



    protected T createQuerySingleResult(String jpql, Object... params) {
        TypedQuery<T> query = entityManager.createQuery(jpql, entityClass);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getSingleResult();
    }

    protected String createQuerySingleResultString(String jpql, Object... params) {
        TypedQuery<String> query = entityManager.createQuery(jpql, String.class);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getSingleResult();
    }

    protected long createQuerySingleResultInt(String jpql, Object... params) {
        TypedQuery<Long> query = entityManager.createQuery(jpql, Long.class);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getSingleResult();
    }

    protected BigDecimal createQuerySingleResulBigDecimal(String jpql, Object... params) {
        TypedQuery<BigDecimal> query = entityManager.createQuery(jpql, BigDecimal.class);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getSingleResult();
    }


    protected boolean createBooleanQuery(String jpql, Object... params) {
        TypedQuery<Boolean> query = entityManager.createQuery(jpql, Boolean.class);
        for (int i = 0; i < params.length; i++) {
            query.setParameter(i + 1, params[i]);
        }
        return query.getSingleResult();
    }

}
