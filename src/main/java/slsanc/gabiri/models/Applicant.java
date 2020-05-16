package slsanc.gabiri.models;

import javax.persistence.*;

@Entity
@Table(name="Applicants")
public class Applicant {

    @Column(name="applicant_id") @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int applicantId;
    @Column(name="first_name") private String firstName;
    @Column(name="last_name") private String lastName;
    @Column(name="email") private String email;
    @Column(name="cell_phone") private String cellPhone;
    @Column(name="work_phone") private String workPhone;
    @Column(name="home_phone") private String homePhone;
    @Column(name="address") private String address;
    @Column(name="city") private String city;
    @Column(name="state_or_provence") private String stateOrProvence;
    @Column(name="hidden_status") private Boolean hiddenStatus;
    @Column(name="owner_id") private int ownerId;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getWorkPhone() {
        return workPhone;
    }

    public void setWorkPhone(String workPhone) {
        this.workPhone = workPhone;
    }

    public String getHomePhone() {
        return homePhone;
    }

    public void setHomePhone(String homePhone) {
        this.homePhone = homePhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateOrProvence() {
        return stateOrProvence;
    }

    public void setStateOrProvence(String stateOrProvence) {
        this.stateOrProvence = stateOrProvence;
    }

    public Boolean getHiddenStatus() {
        return hiddenStatus;
    }

    public void setHiddenStatus(Boolean hiddenStatus) {
        this.hiddenStatus = hiddenStatus;
    }

    public Applicant(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public Applicant() {


    }
}