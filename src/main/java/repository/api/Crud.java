package repository.api;

import java.util.List;

public interface Crud<T> {
    T create(T t);

    T update(T t);

    boolean delete(Long id);

    T findById(Long id);

    List<T> findAll();

}
