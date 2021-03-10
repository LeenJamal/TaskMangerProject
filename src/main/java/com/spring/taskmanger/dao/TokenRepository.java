package com.spring.taskmanger.dao;

import com.spring.taskmanger.model.Token;
import com.spring.taskmanger.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface TokenRepository extends CrudRepository<Token, Long> {

     Boolean existsByJwtAndUserId(String jwt , long userId);

    @Transactional
    @Modifying
    @Query("delete from Token t where t.jwt=:jwt and t.user=:user")
    void deleteByJwtandUserId(@Param("jwt") String jwt, @Param("user")User user);

    @Transactional
    @Modifying
    @Query("delete from Token t where t.user=:user")
    void deleteByUserId(@Param("user")User user);
}
