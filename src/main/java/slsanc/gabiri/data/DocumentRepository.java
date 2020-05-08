package slsanc.gabiri.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import slsanc.gabiri.models.Document;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

}
