package hr.zavrsni.trainflowspringbackend.authServices;

import hr.zavrsni.trainflowspringbackend.authDomain.RolesUser;
import hr.zavrsni.trainflowspringbackend.authRepositories.RolesRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Service
public class RolesServiceImpl {
    @Autowired
    private RolesRepository rolesRepository;

    public RolesUser findByName(String name) {
        return rolesRepository.findByName(name);
    }
}
