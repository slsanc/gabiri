package slsanc.gabiri.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import slsanc.gabiri.models.Document;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Integer> {

    @Query(value = "SELECT document_id, file_name FROM Documents WHERE applicant_id = :applicantId", nativeQuery = true)
    List<Object[]> thisApplicantsDocumentIdsAndNames(@Param("applicantId") int applicantId);

}
