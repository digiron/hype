package com.digiron.hype.data.repository;

import com.digiron.hype.data.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
    <Optional>Role findByName(String name);

}
