package com.cherryhouse.server.user;


import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server.s3.S3Service;
import com.cherryhouse.server.style.Style;
import com.cherryhouse.server.style.StyleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);
    public final UserRepository userRepository;
    public final StyleRepository styleRepository;
    public final S3Service s3Service;

    public boolean existsCheckByEmail(String email){
        return userRepository.existsByEmail(email);
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
        User user = findByEmail(email);
        Long id = styleRepository.findByUserEmailAndFileName(email,filePath).orElseThrow(
                ( )->new ApiException(ExceptionCode.INVALID_REQUEST_DATA,"해당 스타일이 존재하지 않습니다.")
        );
        s3Service.delete(filePath);
        styleRepository.deleteById(id);

    }
}
