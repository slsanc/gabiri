package slsanc.gabiri.data;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import slsanc.gabiri.models.Applicant;

import java.util.List;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant,Integer> {

    /* Given a positionId, the following method returns a list of people who have not applied to that position and
    * have not yet been hired to another position and who were created by the same user as the given position. */
    @Query(value = "SELECT A.* FROM Applications N1 " +
            "LEFT JOIN Applications N2 " +
            "ON (N1.status=3 OR N1.position_id = :positionId)  AND N1.applicant_id = N2.applicant_id " +
            "RIGHT JOIN Applicants A " +
            "ON A.applicant_id = N2.applicant_id " +
            "RIGHT JOIN Positions P " +
            "ON P.position_id=:positionId AND P.owner_id = A.owner_id " +
            "WHERE N2.applicant_id IS NULL AND A.applicant_id IS NOT NULL"
            , nativeQuery = true)
    List<Applicant> applicantsNotYetConsidered(@Param("positionId") int positionId);

    /* Given a PositionId, the following method returns the applicant who was hired for that position*/
    @Query(value = "SELECT * FROM Applicants WHERE applicant_id = " +
            "(SELECT applicant_id FROM Applications WHERE status BETWEEN 3 AND 4 AND position_id = :positionId)"
            , nativeQuery = true)
    Applicant applicantHiredFor(@Param("positionId")int positionId);

    @Query(value = "SELECT A.* FROM Applications N1 " +
            "LEFT JOIN Applications N2 " +
            "ON N1.status = 3 AND N1.applicant_id = N2.applicant_id " +
            "RIGHT JOIN Applicants A " +
            "ON A.applicant_id = N2.applicant_id " +
            "WHERE N2.applicant_id IS NULL AND A.applicant_id IS NOT NULL"
            , nativeQuery = true)
    List<Applicant> allAvailableApplicants();

    @Query(value = "SELECT A.* FROM Applications N1 " +
            "LEFT JOIN Applications N2 " +
            "ON N1.status = 3 AND N1.applicant_id = N2.applicant_id " +
            "RIGHT JOIN Applicants A " +
            "ON A.applicant_id = N2.applicant_id " +
            "RIGHT JOIN USERS U " +
            "ON U.username = :username AND U.user_id = A.owner_id " +
            "WHERE N2.applicant_id IS NULL AND A.applicant_id IS NOT NULL"
            , nativeQuery = true)
    List<Applicant> availableApplicantsThisUserCreated(@Param("username") String username);


    /*The following returns 1 if the user owns the applicant, and 0 if the user does not own the applicant.*/
    @Query(value="SELECT COUNT(*) FROM Users U INNER JOIN Applicants A " +
            "ON U.username = :username AND A.applicant_id = :applicantId AND U.user_id = A.owner_id"
            , nativeQuery = true)
    int userOwnsThisApplicant(@Param("username") String username, @Param("applicantId") int applicantId);

}
