package com.cherryhouse.server.reserve;

import com.cherryhouse.server.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {


    Optional<Reserve> findByPostIdAndReceiver(Long id, User receiver);

    Optional<Reserve> findByPostIdAndProviderAndReceiver(Long id, User provider, User receiver);
}
