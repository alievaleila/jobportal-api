package com.example.config;

import com.example.entity.Role;
import com.example.enums.RoleType;
import com.example.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;

    @Override
    public void run(ApplicationArguments args) {
        for (RoleType type : RoleType.values()) {
            if (roleRepository.findByName(type).isEmpty()) {
                Role role = new Role();
                role.setName(type);
                roleRepository.save(role);
            }
        }
    }
}