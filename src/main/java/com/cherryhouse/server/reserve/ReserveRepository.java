package com.cherryhouse.server.reserve;

import com.cherryhouse.server.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {

    Optional<Reserve> findByPostIdAndReceiver(Long id, User receiver);

    Optional<Reserve> findByPostIdAndProviderAndReceiver(Long id, User provider, User receiver);

    @Query("select r from Reserve r " +
            "where r.provider.email = :email " +
            "or r.receiver.email = :email " +
            "order by r.id " +
            "desc ")
    Page<Reserve> findAllByUser(String email, Pageable pageable);
}
