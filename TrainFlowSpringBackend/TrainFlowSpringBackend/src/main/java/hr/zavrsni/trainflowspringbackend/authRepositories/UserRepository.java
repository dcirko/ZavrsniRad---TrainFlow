package hr.zavrsni.trainflowspringbackend.authRepositories;

import hr.zavrsni.trainflowspringbackend.authDomain.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo, Integer> {
    UserInfo findByEmail(String email);
    @Query("SELECT u FROM UserInfo u JOIN FETCH u.savedPlans WHERE u.email = :email")
    UserInfo findByEmailWithPlans(@Param("email") String email);

}
