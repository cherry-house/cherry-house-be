package com.cherryhouse.server.style;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StyleRepository extends JpaRepository<Style, Long> {

    @Query("select s.id from Style s " +
            "where s.user.email = :email " +
            "and s.accessImgUrl = :filePath")
    Optional<Long> findByUserEmailAndFileName(@Param("email") String email, @Param("filePath") String filePath);

    @Query("select s from Style s " +
            "where s.user.id = :userId " +
            "order by s.id " +
            "desc")
    List<Style> findByUserId(@Param("userId") Long userId);
}