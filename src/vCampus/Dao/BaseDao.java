package vCampus.Dao;
/*
* 提供基础增删改查功能
 */
public interface BaseDao<T> {
    boolean add(T entity);
    boolean update(T entity);
    boolean delete(String id);
    T find(String id);  // 返回找到的实体对象
}
