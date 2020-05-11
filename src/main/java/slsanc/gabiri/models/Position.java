package slsanc.gabiri.models;


import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "Positions")
public class Position {

    @Column(name="position_id") @Id @GeneratedValue(strategy = GenerationType.IDENTITY) int positionId;
    @Column(name="position_title") private String positionTitle;
    @Column(name="employment_type") private int employmentType;
    @Column(name="max_salary") private int maxSalary;
    @Column(name="max_wage") private BigDecimal maxWage;
    @Column(name="city") private String city;
    @Column(name="state_or_provence") private String stateOrProvence;
    @Column(name="start_date") private Date startDate;
    @Column(name="date_created") private Date dateCreated;
    @Column(name="date_filled") private Date dateFilled;

    public int getPositionId() {
        return positionId;
    }

    public void setPositionId(int positionId) {
        this.positionId = positionId;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public int getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(int employmentType) {
        this.employmentType = employmentType;
    }

    public int getMaxSalary() {
        return maxSalary;
    }

    public void setMaxSalary(int maxSalary) {
        this.maxSalary = maxSalary;
    }

    public BigDecimal getMaxWage() {
        return maxWage;
    }

    public void setMaxWage(BigDecimal maxWage) {
        this.maxWage = maxWage;
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

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getDatePosted() {
        return dateCreated;
    }

    public void setDateCreated(Date datePosted) {
        this.dateCreated = datePosted;
    }

    public Date getDateFilled() {
        return dateFilled;
    }

    public void setDateFilled(Date dateFilled) {
        this.dateFilled = dateFilled;
    }

    public boolean isStillOpen(){
        if(dateFilled==null){
            return true;
        }
        else{
            return false;
        }
    }

    public String employmentTypeAsString(){
        String[] employmentTypes={"Full Time Salaried", "Full Time Wage-Based", "Part Time Wage-Based"
                , "Independent Contractor", "Freelance", "Other"};
        return employmentTypes[(employmentType-1)];
    }

    public Position() {};
    }
