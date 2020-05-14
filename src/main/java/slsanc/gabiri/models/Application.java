package slsanc.gabiri.models;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name="Applications")
@IdClass(ApplicationId.class)
public class Application {

    @Id @Column(name="applicant_id") private int applicantId;
    @Id @Column(name="position_id") private int positionId;
    @Column(name="status") private int status;
    @Column(name="date_applied") private Date dateApplied;
    @Column(name="rating") private int rating;

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String statusAsString(){
        String[] statusTypes = {"Under Consideration", "No Longer Under Consideration"
                , "Hired", "Hired, Position Completed or Terminated"};
        return statusTypes[status-1];
    }

    public Application() {}

}