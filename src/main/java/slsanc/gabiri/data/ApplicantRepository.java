package slsanc.gabiri.data;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import slsanc.gabiri.models.Applicant;

@Repository
public interface ApplicantRepository extends CrudRepository<Applicant,Integer> {
}
