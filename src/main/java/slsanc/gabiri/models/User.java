package slsanc.gabiri.models;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="Users")
public class User {

    @NotNull
    @Column(name="user_id") @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int userId;

    @NotNull @NotEmpty
    @Column(name = "first_name") private String firstName;

    @NotNull @NotEmpty
    @Column(name = "last_name") private String lastName;

    @NotNull @NotEmpty @Email(message = "Email is invalid!")
    @Column(name = "email", unique = true) private String email;

    @NotNull @NotEmpty
    @Column(name = "username", unique = true) private String username;

    @NotNull @NotEmpty
    @Column(name = "password") private String password;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User() {
    }
}
