package slsanc.gabiri.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import slsanc.gabiri.models.Position;

@Repository
public interface PositionRepository extends CrudRepository<Position,Integer> {
}