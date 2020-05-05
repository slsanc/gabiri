package slsanc.gabiri.data;

import org.springframework.data.repository.CrudRepository;
import slsanc.gabiri.models.Position;

public interface PositionRepository extends CrudRepository<Position,Integer> {
}
