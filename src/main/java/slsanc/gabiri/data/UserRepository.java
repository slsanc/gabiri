package slsanc.gabiri.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import slsanc.gabiri.models.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    @Query(value="SELECT * FROM Users WHERE email = :email" , nativeQuery = true)
    User findUserByEmail(@Param("email") String email);

    @Query(value="SELECT * FROM Users WHERE username = :username" , nativeQuery = true)
    User findUserByUsername(@Param("username") String username);

    @Transactional
    @Modifying
    @Query(value="INSERT INTO User_Roles (user_id , role_id) VALUES (:userId , :roleId)" , nativeQuery = true)
    void createPermissions(@Param("userId") int userId , @Param("roleId") int roleId);

}
