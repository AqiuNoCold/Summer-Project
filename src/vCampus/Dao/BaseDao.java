package vCampus.Dao;

public interface BaseDao<T> {
    boolean add(T entity);
    boolean update(T entity);
    boolean delete(int id);
    T find(int id);  // 返回找到的实体对象
}
