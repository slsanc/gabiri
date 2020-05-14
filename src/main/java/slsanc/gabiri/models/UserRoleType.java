package slsanc.gabiri.models;

import org.springframework.stereotype.Controller;

import javax.persistence.*;

@Entity
@Table(name="User_Role_Types")
public class UserRoleType {
    @Column(name="role_id") @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private int roleId;
    @Column(name="role_name") private String roleName;
    @Column(name="description") private String description;

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public UserRoleType() {
    }
}
