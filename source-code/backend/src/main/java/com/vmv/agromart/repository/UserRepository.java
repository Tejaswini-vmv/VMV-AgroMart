package com.vmv.agromart.repository;

import com.vmv.agromart.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByPhoneNumber(String phoneNumber);
    List<User> findByRoleOrderByFullNameAsc(User.UserRole role);
    List<User> findByRoleAndCityIgnoreCaseOrderByFullNameAsc(User.UserRole role, String city);
    boolean existsByRole(User.UserRole role);
}
