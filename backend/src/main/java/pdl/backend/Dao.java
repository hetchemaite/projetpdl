package pdl.backend;

import java.util.Optional;

import io.scif.FormatException;

import java.io.IOException;
import java.util.List;

public interface Dao<T> {
  
  void create(final T t);

  Optional<T> retrieve(final long id);

  List<T> retrieveAll();

  void update(final T t, final String[] params) throws FormatException, IOException;

  void delete(final T t);
}
