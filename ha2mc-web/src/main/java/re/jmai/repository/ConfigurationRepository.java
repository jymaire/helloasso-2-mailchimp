package re.jmai.repository;

import com.sun.istack.NotNull;
import org.springframework.data.repository.CrudRepository;
import re.jmai.entity.Configuration;

import java.util.List;
import java.util.Optional;

public interface ConfigurationRepository extends CrudRepository<Configuration, String> {

    List<Configuration> findAll();

    Configuration save(@NotNull Configuration configuration);

    @Override
    Optional<Configuration> findById(@NotNull String key);
}
