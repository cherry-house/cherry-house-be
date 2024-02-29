package com.cherryhouse.server.reserve;

import com.cherryhouse.server._core.exception.ApiException;
import com.cherryhouse.server._core.exception.ExceptionCode;
import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server.post.Post;
import com.cherryhouse.server.post.PostRepository;
import com.cherryhouse.server.post.PostService;
import com.cherryhouse.server.user.User;
import com.cherryhouse.server.user.UserRepository;
import com.cherryhouse.server.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

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
                .postId(post.getId())
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
}
