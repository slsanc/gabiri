package slsanc.gabiri.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import slsanc.gabiri.models.Application;

import java.sql.Date;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    @Transactional
    @Modifying
    @Query( value= "UPDATE Applications SET status = :newStatus WHERE " +
            "applicant_id = :applicantId AND position_id = :positionId ; " , nativeQuery = true)
    void changeStatus(@Param("applicantId") int applicantId , @Param("positionId") int positionId
            , @Param("newStatus") int newStatus);

}
