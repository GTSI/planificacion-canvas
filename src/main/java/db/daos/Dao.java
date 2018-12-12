package db.daos;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Dao<T> {

    Optional<T> get(long id) throws SQLException;

    List<T> getAll() throws SQLException;

    long save(T t) throws Exception;
    List<Long> saveAll(List<T> t);

    void update(T t, String[] params);

    void delete(T t);

}
