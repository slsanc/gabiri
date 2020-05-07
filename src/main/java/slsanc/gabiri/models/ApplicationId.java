package slsanc.gabiri.models;


import java.io.Serializable;

/* This class describes the composite ID of the Application class. */

public class ApplicationId implements Serializable {

    private int applicantId;
    private int positionId;

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

    public ApplicationId() {}
}
