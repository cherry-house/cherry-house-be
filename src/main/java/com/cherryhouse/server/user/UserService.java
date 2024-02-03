package com.cherryhouse.server.user;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.auth.dto.AuthRequest;
import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.post.PostRepository;
import com.cherryhouse.server.posttag.PostTagMapping;
import com.cherryhouse.server.s3.S3Service;
import com.cherryhouse.server.style.Style;
import com.cherryhouse.server.style.StyleRepository;
import com.cherryhouse.server.tag.TagService;
import com.cherryhouse.server.user.dto.UserRequest;
import com.cherryhouse.server.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;

import static com.cherryhouse.server._core.util.PageData.getPageData;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;
    private final StyleRepository styleRepository;
    private final S3Service s3Service;
    private final PasswordEncoder passwordEncoder;
    private final PostRepository postRepository;
    private final TagService tagService;


    //user 관련------------------------------------------------------
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
    public UserResponse.UserDto getUserInfo(Long userId, Pageable pageable) {
        User user = findById(userId);

        List<Style> styleList = styleRepository.findByUserId(user.getId());

        Page<Post> postList = postRepository.findByUserEmail(user.getEmail(), pageable);
        PageData pageData = getPageData(postList);
        List<PostTagMapping> postTagMappings = postList.stream() //post id 마다 tags 를 일급 클래스에 담아서 가지고 오기
                .map(post -> new PostTagMapping(post.getId(), tagService.getTags(post.getId())))
                .toList();
        return UserResponse.UserDto.of(pageData,user,styleList,postList.getContent(),postTagMappings);
    }
    @Transactional
    public void updateInfo(String email, UserRequest.updateInfoDto updateInfoDto){
        if(!Objects.equals(email, updateInfoDto.email())){
            throw new ApiException(ExceptionCode.BAD_USER_REQUEST);
        }
        User user = findByEmail(email);
        user.updateInfo(updateInfoDto.username(), updateInfoDto.introduction());


    }
    @Transactional
    public void updatePwd(String email, UserRequest.updatePwdDto updatePwdDto){
        if(!Objects.equals(email, updatePwdDto.email())){
            throw new ApiException(ExceptionCode.BAD_USER_REQUEST);
        }

    }

    @Transactional
    public void updateImg(String email, MultipartFile file){
        log.info("file: {}",file);
        User user = findByEmail(email);
        if(user.getProfileImage() != null){
            s3Service.delete(user.getProfileImage());
        }

        if(file.isEmpty()){         //비어있으면 기본 이미지로 변경
            user.updateImg(null);
        }else{
            String fileName = s3Service.uploadOne(file,"profile");
            user.updateImg(fileName);
        }

    }


    //style 관련 ------------------------------------------------------
    @Transactional
    public void uploadStyle(List<MultipartFile> file, String email) {
        User user = findByEmail(email);
        List<String> fileNames = s3Service.upload(file, "style");

        fileNames.forEach(filePath -> {
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
        Long id = styleRepository.findByUserEmailAndFileName(email, filePath).orElseThrow(
                () -> new ApiException(ExceptionCode.INVALID_REQUEST_DATA, "해당 스타일이 존재하지 않습니다.")
        );
        s3Service.delete(filePath);
        styleRepository.deleteById(id);
    }





    //------------------------------------------------------

    public void existsByEmail(String email){
        if (userRepository.existsByEmail(email)) {
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
