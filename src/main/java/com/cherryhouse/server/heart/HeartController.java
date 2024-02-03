package com.cherryhouse.server.heart;


import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.util.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/hearts")
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;


    @PostMapping("/{postId}")
    public ResponseEntity<?> toggleHeart(@AuthenticationPrincipal UserPrincipal user,
                                         @PathVariable("postId")Long postId){
        boolean HeartAdded = heartService.toggleHeart(user.getEmail(), postId);

        String msg = HeartAdded ? "좋아요 추가" : "좋아요 취소";
        return ResponseEntity.ok().body(ApiResponse.success(msg));
    }

    @GetMapping
    public ResponseEntity<?> getHearts(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @PageableDefault(size = 6) Pageable pageable){
        HeartResponse.HeartDto response = heartService.getHearts(userPrincipal.getEmail(), pageable);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }


}
