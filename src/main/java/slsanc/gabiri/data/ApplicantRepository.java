package slsanc.gabiri.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import slsanc.gabiri.models.Applicant;

import java.util.List;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant,Integer> {

    /* Given a positionId, the following method returns a list of people who have applied and have not yet
    * been hired to another position. */
    @Query(value = "SELECT * FROM Applicants WHERE " +
            "applicant_id NOT IN (SELECT applicant_id FROM Applications WHERE status = 3) " +
            "AND applicant_id IN (SELECT applicant_id FROM Applications WHERE position_id = :positionId)"
            , nativeQuery = true)
    List<Applicant> findApplicantsByPositionAppliedfor(@Param("positionId")int positionId);


    /* Given a positionId, the following method returns a list of people who have not applied to that position and
    * have not yet been hired to another position. */
    @Query(value = "SELECT * FROM Applicants WHERE " +
            "applicant_id NOT IN (SELECT applicant_id FROM Applications WHERE status = 3) " +
            "AND applicant_id NOT IN (SELECT applicant_id FROM Applications WHERE position_id = :positionId)"
            , nativeQuery = true)
    List<Applicant> applicantsNotYetConsidered(@Param("positionId")int positionId);

    /* Given a PositionId, the following method returns the applicant who was hired for that position*/
    @Query(value = "SELECT * FROM Applicants WHERE " +
            " applicant_id = (SELECT applicant_id FROM Applications WHERE status = 3 AND position_id = :positionId)"
            , nativeQuery = true)
    Applicant applicantHiredFor(@Param("positionId")int positionId);

    @Query(value="SELECT * FROM Applicants WHERE applicant_id NOT IN " +
            "(SELECT applicant_id FROM Applications WHERE status = 3)" , nativeQuery = true)
    List<Applicant> availableApplicants();

}
