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

    @Query(value = "SELECT * FROM Positions WHERE date_filled IS NULL ORDER BY date_created DESC" , nativeQuery = true)
    List<Position> openPositions();


    @Query(value = "SELECT * FROM Positions WHERE date_filled IS NOT NULL ORDER BY date_filled DESC"
            , nativeQuery = true)
    List<Position> filledPositions();


    @Transactional @Modifying @Query(value="UPDATE Positions SET date_filled = :now WHERE position_id = :positionId"
             , nativeQuery = true)
    void setDateFilled(@Param("positionId") int positionId, @Param("now") Date now);


    @Query(value = "SELECT * FROM POSITIONS WHERE position_id IN " +
            "(SELECT position_id FROM APPLICATIONS WHERE applicant_id = :applicantId)" , nativeQuery = true)
    List<Position> positionsThisApplicantHasAppliedFor(@Param("applicantId") int applicantId);


    @Transactional @Modifying @Query(value="DELETE FROM POSITIONS WHERE position_id IN " +
            "(SELECT position_id FROM APPLICATIONS WHERE applicant_id = :applicantId AND status BETWEEN 3 AND 4)"
            , nativeQuery = true)
    void deletePositionsThisApplicantWasHiredFor(@Param("applicantId") int applicantId);

}
