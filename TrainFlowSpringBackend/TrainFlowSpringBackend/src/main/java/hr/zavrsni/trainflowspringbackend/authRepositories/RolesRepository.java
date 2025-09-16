package hr.zavrsni.trainflowspringbackend.authRepositories;

import hr.zavrsni.trainflowspringbackend.authDomain.RolesUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolesRepository extends JpaRepository<RolesUser, Integer> {
    RolesUser findByName(String name);
}
