package com.driver.repository;
import com.driver.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer>{

    @Modifying
    @Transactional
    @Query(value = "update User u set u.password where u.id =: userId)", nativeQuery = true)
    User updatePassword(Integer userId, String password);
}
