package projekt.nieruchomosci.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import projekt.nieruchomosci.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Integer>{
    Role findRoleByName(String name);
}
