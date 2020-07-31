package com.severo.demospring.repository;

import com.severo.demospring.domain.DemoSpringUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemoSpringUserRepository extends JpaRepository<DemoSpringUser, Integer> {

    DemoSpringUser findByUserName(String userName);
}
