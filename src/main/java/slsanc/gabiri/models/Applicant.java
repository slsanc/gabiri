package slsanc.gabiri.models;

import javax.persistence.*;

@Entity
@Table(name="Applicants")
public class Applicant {

    @Column(name="applicant_id") @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int applicantId;
    @Column(name="first_name") private String firstName;
    @Column(name="last_name") private String lastName;

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Applicant() {

    }
}