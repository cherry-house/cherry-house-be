package com.cherryhouse.server.style;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface StyleRepository extends JpaRepository<Style, Long> {

    @Query("select s.id from Style s " +
            "where s.user.email = :email " +
            "and s.imgUrl = :filePath")
    Optional<Long> findByUserEmailAndFileName(@Param("email") String email, @Param("filePath") String filePath);
}