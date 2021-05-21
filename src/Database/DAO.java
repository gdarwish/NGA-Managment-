package Database;

import java.util.List;
import java.util.Optional;

/**
 *
 * @param <T>
 * @author Ali Dali
 */
public interface DAO<T> {

    Optional<? extends T> get(int id);
    Optional<? extends T> get(String name);

    List<? extends T> getAll();

    int create(T t);

    int update(T t);

    int delete(T t);

    void updateList();

    int getLastInsertedId();
}
