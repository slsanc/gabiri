package slsanc.gabiri.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import slsanc.gabiri.models.Position;

import java.sql.Date;
import java.util.List;

@Repository
public interface PositionRepository extends JpaRepository<Position,Integer> {

    @Query(value = "SELECT * FROM Positions WHERE date_filled IS NULL", nativeQuery = true)
    List<Position> allOpenPositions();

    @Query(value = "SELECT P.* FROM Positions P INNER JOIN Users U " +
            "ON U.username = :username AND  P.owner_id=U.user_id "+
            "WHERE date_filled IS NULL ORDER BY date_created DESC"
            , nativeQuery = true)
    List<Position> openPositionsThisUserCreated(@Param("username") String username);


    @Query(value = "SELECT * FROM Positions WHERE date_filled IS NOT NULL ORDER BY date_filled DESC"
            , nativeQuery = true)
    List<Position> allFilledPositions();

    @Query(value = "SELECT P.* FROM Positions P INNER JOIN Users U " +
            "ON U.username = :username AND  P.owner_id=U.user_id "+
            "WHERE date_filled IS NOT NULL ORDER BY date_created DESC"
            , nativeQuery = true)
    List<Position> filledPositionsThisUserCreated(@Param("username") String username);


    @Transactional @Modifying @Query(value="UPDATE Positions SET date_filled = :now WHERE position_id = :positionId"
             , nativeQuery = true)
    void setDateFilled(@Param("positionId") int positionId, @Param("now") Date now);


    @Query(value = "SELECT * FROM POSITIONS WHERE position_id IN " +
            "(SELECT position_id FROM APPLICATIONS WHERE applicant_id = :applicantId)" , nativeQuery = true)
    List<Position> positionsAppliedTo(@Param("applicantId") int applicantId);


    @Transactional @Modifying @Query(value="DELETE FROM POSITIONS WHERE position_id IN " +
            "(SELECT position_id FROM APPLICATIONS WHERE applicant_id = :applicantId AND status BETWEEN 3 AND 4)"
            , nativeQuery = true)
    void deletePositionsThisApplicantWasHiredFor(@Param("applicantId") int applicantId);

    @Query(value="SELECT P1.* FROM Applications N RIGHT JOIN Positions P1 " +
            "ON N.applicant_id = :applicantId AND N.position_id = P1.position_id " +
            "RIGHT JOIN Applicants A " +
            "ON A.applicant_id = :applicantId AND P1.owner_id = A.owner_id " +
            "WHERE N.position_id IS NULL AND P1.position_id IS NOT NULL"
            , nativeQuery = true)
    List<Position> positionsNotYetAppliedTo(@Param("applicantId") int applicantId);


    /*The following returns 1 if the user owns the position, and 0 if the user does not own the position.*/
    @Query(value="SELECT COUNT(*) FROM Users U INNER JOIN Positions P " +
            "ON U.username = :username AND P.position_id = :positionId AND U.user_id = P.owner_id" , nativeQuery = true)
    int userOwnsThisPosition(@Param("username") String username, @Param("positionId") int positionId);

}
