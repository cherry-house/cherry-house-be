package com.cherryhouse.server.user;


import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server.auth.dto.AuthRequest;
import com.cherryhouse.server.s3.S3Service;
import com.cherryhouse.server.style.Style;
import com.cherryhouse.server.style.StyleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    public final UserRepository userRepository;
    public final StyleRepository styleRepository;
    public final S3Service s3Service;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User save(AuthRequest.JoinDto joinDto){
        User user = User.builder()
                .username(joinDto.username())
                .email(joinDto.email())
                .password(passwordEncoder.encode(joinDto.password()))
                .build();

        userRepository.save(user);
        return user;
    }

    @Transactional
    public void uploadStyle(List<MultipartFile> file, String email) {
        User user = findByEmail(email);
        List<String> fileNames = s3Service.upload(file,"style");

        fileNames.forEach( filePath ->{
            Style style = Style.builder()
                    .user(user)
                    .imgUrl(filePath)
                    .build();
            styleRepository.save(style);
        });
    }

    @Transactional
    public void deleteStyle(String filePath, String email) {
        findByEmail(email);
        Long id = styleRepository.findByUserEmailAndFileName(email,filePath).orElseThrow(
                ( )->new ApiException(ExceptionCode.INVALID_REQUEST_DATA,"해당 스타일이 존재하지 않습니다.")
        );
        s3Service.delete(filePath);
        styleRepository.deleteById(id);
    }

    public void existsByEmail(String email){
        if(userRepository.existsByEmail(email)) {
            throw new ApiException(ExceptionCode.USER_EXISTS, "이미 회원가입된 이메일입니다.");
        }
    }

    public User findById(Long id){
        return userRepository.findById(id).orElseThrow(
                ()-> new ApiException(ExceptionCode.USER_NOT_FOUND)
        );
    }


    public User findByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(
                ()-> new ApiException(ExceptionCode.USER_NOT_FOUND)
        );
    }
}
