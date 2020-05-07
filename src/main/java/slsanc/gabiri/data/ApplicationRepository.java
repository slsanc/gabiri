package slsanc.gabiri.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import slsanc.gabiri.models.Application;

@Repository
public interface ApplicationRepository extends CrudRepository<Application, Integer> {
}
