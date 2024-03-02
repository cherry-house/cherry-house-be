package com.cherryhouse.server.reserve;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.util.PageData;
import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.post.PostRepository;
import com.cherryhouse.server.post.PostService;
import com.cherryhouse.server.post.image.ImageMapping;
import com.cherryhouse.server.post.image.ImageService;
import com.cherryhouse.server.post.posttag.PostTagMapping;
import com.cherryhouse.server.post.tag.TagService;
import com.cherryhouse.server.user.User;
import com.cherryhouse.server.user.UserRepository;
import com.cherryhouse.server.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReserveService {

    private final PostService postService;
    private final UserService userService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ReserveRepository reserveRepository;
    private final TagService tagService;
    private final ImageService imageService;

    @Transactional
    public void reserve(UserPrincipal user, ReserveRequest.makeReserveDto makeReserveDto) {

        Post post = postService.getPostById(makeReserveDto.postId());

        //작성자만 예약 관리 가능
        User provider = post.getUser();
        if (!(Objects.equals(user.getEmail(), provider.getEmail()))){
            throw new ApiException(ExceptionCode.RESERVE_NOT_ALLOWED);
        }

        User receiver = userService.findByEmail(makeReserveDto.receiver());
        LocalDate reservationDate = makeReserveDto.reservationDate();
        LocalDate createDate = LocalDate.from(LocalDateTime.now());

        Reserve reserve = Reserve.builder()
                .post(post)
                .provider(provider)
                .receiver(receiver)
                .reservationDate(reservationDate)
                .createDate(createDate)
                .status(Status.RESERVED)
                .build();

        if(reserveRepository.findByPostIdAndReceiver(post.getId(), receiver).isPresent()) {
            throw new ApiException(ExceptionCode.RESERVE_ALREADY_EXIST);
        }

        reserveRepository.save(reserve);

    }

    @Transactional
    public void update(UserPrincipal user, ReserveRequest.changeReserveDto changeReserveDto) {
        User provider = userService.findByEmail(user.getEmail());
        Post post = postService.getPostById(changeReserveDto.postId());

        if (!(Objects.equals(post.getUser(), provider))){
            throw new ApiException(ExceptionCode.RESERVE_NOT_ALLOWED);
        }
        User receiver = userService.findByEmail(changeReserveDto.receiver());
        Reserve reserve = reserveRepository.findByPostIdAndProviderAndReceiver(changeReserveDto.postId(), provider, receiver)
                .orElseThrow( () -> new ApiException(ExceptionCode.RESERVE_NOT_FOUND));

        reserve.update(changeReserveDto.reservationDate(),changeReserveDto.status());

    }

    public ReserveResponse.ReserveDto get(String email, Pageable pageable) {
        Page<Reserve> reserveList = reserveRepository.findAllByUser(email, pageable);
        List<Post> postList = reserveList.stream()
                .map(Reserve::getPost)
                .collect(Collectors.toList());

        PageData pageData = PageData.getPageData(reserveList);


        List<PostTagMapping.TagsDto> tagsDtoList = tagService.getTagsDtoList(postList);
        List<ImageMapping.UrlDto> urlDtoList = imageService.getUrlDtoList(postList);

        return ReserveResponse.ReserveDto.of(pageData,reserveList.getContent(),tagsDtoList,urlDtoList);

    }
}
