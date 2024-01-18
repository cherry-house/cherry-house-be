package com.cherryhouse.server.user;


import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    public final UserRepository userRepository;
    private final EntityManager entityManager;
    public boolean existsCheckByEmail(String email) { return userRepository.existsByEmail(email); }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(
                ()-> new ApiException(ExceptionCode.USER_NOT_FOUND, "해당 사용자를 찾을 수 없습니다.")
        );
    }

    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                ()-> new ApiException(ExceptionCode.USER_NOT_FOUND, "해당 사용자를 찾을 수 없습니다.")
        );
    }

    public User getReferenceByEmail(String email) {
        // 실제 엔터티를 로드하지 않고, 프록시 객체를 반환
        return entityManager.getReference(User.class, findByEmail(email).getId());
    }



}
