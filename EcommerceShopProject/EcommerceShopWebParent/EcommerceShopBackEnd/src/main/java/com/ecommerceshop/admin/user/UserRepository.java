package com.ecommerceshop.admin.user;

import com.ecommerceshop.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("Select u FROM User u WHERE u.email = :email ")
    public User getUserByEmail(@Param("email") String email);

    public Long countById(Long id);

    @Query("UPDATE User u SET u.enabled = ?2 WHERE u.id = ?1")
    @Modifying
    public void updateEnableStatus(Long id, boolean enable);
}
