package ma.dentaluxe.repository.common;

import java.util.List;

public interface CrudRepository<T, ID> {
//T: Type d'entite et ID : type d'id
    List<T> findAll();

    T findById(ID id);

    void create(T patient);

    void update(T patient);

    void delete(T patient);

    void deleteById(ID id);
}
