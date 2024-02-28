package com.cherryhouse.server.user;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.auth.dto.AuthRequest;
import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.post.PostRepository;
import com.cherryhouse.server.post.image.ImageMapping;
import com.cherryhouse.server.post.image.ImageService;
import com.cherryhouse.server.post.posttag.PostTagMapping;
import com.cherryhouse.server.post.tag.TagService;
import com.cherryhouse.server.s3.S3Service;
import com.cherryhouse.server.user.style.Style;
import com.cherryhouse.server.user.style.StyleRepository;
import com.cherryhouse.server.user.dto.UserRequest;
import com.cherryhouse.server.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
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
    private final ImageService imageService;
    private final RedisTemplate<String, String> redisTemplate;

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

    public UserResponse.UserDto getUserInfo(Long userId, Pageable pageable) {
        User user = findById(userId);

        List<Style> styleList = styleRepository.findByUserId(user.getId());
        Page<Post> postList = postRepository.findByUserEmail(user.getEmail(), pageable);

        PageData pageData = getPageData(postList);
        List<PostTagMapping.TagsDto> tagsDtoList = tagService.getTagsDtoList(postList.getContent());
        List<ImageMapping.UrlDto> urlDtoList = imageService.getUrlDtoList(postList.getContent());

        return UserResponse.UserDto.of(pageData, user, styleList, postList.getContent(), tagsDtoList, urlDtoList);
    }

    @Transactional
    public void updateInfo(String email, UserRequest.UpdateInfoDto updateInfoDto){
        if(!Objects.equals(email, updateInfoDto.email())){
            throw new ApiException(ExceptionCode.BAD_USER_REQUEST);
        }
        User user = findByEmail(email);
        user.updateInfo(updateInfoDto.username(), updateInfoDto.introduction());
    }

    @Transactional
    public void updatePwd(String email, UserRequest.UpdatePwdDto updatePwdDto){
        //유저 확인 & 비밀번호 검증
        User user = validatePassword(email, updatePwdDto.currentPassword());
        //로그인 상태 확인
        if (redisTemplate.opsForValue().get(email + ":refresh_token") == null){
            throw new ApiException(ExceptionCode.USER_LOGOUT);
        }
        user.updatePassword(passwordEncoder.encode(updatePwdDto.newPassword()));

    }

    @Transactional
    public void updateImg(String email, MultipartFile file){
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
            String accessImgUrl = s3Service.getAccessImgUrl(filePath);
            Style style = Style.builder()
                    .user(user)
                    .saveImgUrl(filePath)
                    .accessImgUrl(accessImgUrl)
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

    private User validatePassword(String email, String password){
        User user = findByEmail(email);
        if( !passwordEncoder.matches(password, user.getPassword()) ){
            throw new ApiException(ExceptionCode.BAD_USER_PASSWORD);
        }
        return user;

    }
}
