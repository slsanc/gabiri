package slsanc.gabiri.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import slsanc.gabiri.models.Application;

import java.sql.Date;
import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    @Transactional
    @Modifying
    @Query( value= "UPDATE Applications SET status = :newStatus WHERE " +
            "applicant_id = :applicantId AND position_id = :positionId ; " , nativeQuery = true)
    void changeStatus( @Param("positionId") int positionId, @Param("applicantId") int applicantId
            , @Param("newStatus") int newStatus);


    /* This query takes in a position ID and the applicant ID of the person who was hired for the position.
    * It sets the status of the application of all applicants who weren't hired to 2
    * (meaning 'no longer under consideration). */
    @Transactional
    @Modifying
    @Query(value="UPDATE Applications SET status = 2 WHERE " +
            "position_id = :positionId AND applicant_id != :applicantId" ,nativeQuery = true)
    void rejectRunnersUp(@Param("positionId") int positionId , @Param("applicantId") int applicantId);


    /*Given the position and whether or not it is still open, this query returns a list of applications to that
    * position. If the position is still open, it excludes any applicants who have already been hired for
    * another position.*/
    @Query(value = "SELECT * FROM Applications WHERE position_id = :positionId AND " +
            "applicant_id NOT IN (SELECT applicant_id FROM Applications WHERE status = (3 * :isStillOpen)) " +
            "ORDER BY status ASC" , nativeQuery = true)
    List<Application> findApplicationsByPosition(@Param("positionId") int positionId
            , @Param("isStillOpen") boolean isStillOpen);

    @Query(value = "SELECT * FROM APPLICATIONS WHERE position_id=:positionId AND applicant_id=:applicantId"
            , nativeQuery = true)
    Application findApplicationByPositionAndApplicant(@Param("positionId") int positionId
        , @Param("applicantId") int applicantId);



}
