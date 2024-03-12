package com.cherryhouse.server.user;

import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.util.ApiResponse;
import com.cherryhouse.server.user.dto.UserRequest;
import com.cherryhouse.server.user.dto.UserResponse;
import jakarta.validation.Valid;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import java.util.List;

@Tag(name = "USER", description = "사용자 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable("userId") Long userId, @PageableDefault Pageable pageable){
        UserResponse.UserDto response = userService.getUserInfo(userId, pageable);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }

    @PutMapping("/info")
    public ResponseEntity<?> updateInfo(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @Valid @RequestBody UserRequest.UpdateInfoDto updateInfoDto, Errors errors){
        userService.updateInfo(userPrincipal.getEmail(), updateInfoDto);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @PutMapping("/profileImg")
    public ResponseEntity<?> updateImg(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                        @RequestParam("file") MultipartFile file){
        userService.updateImg(userPrincipal.getEmail(), file);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @PutMapping("/password")
    public ResponseEntity<?> updatePwd(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                       @Valid @RequestBody UserRequest.UpdatePwdDto updatePwdDto, Errors errors){
        userService.updatePwd(userPrincipal.getEmail(), updatePwdDto);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    //------------------------------------------------------

    @PostMapping("/style")
    public ResponseEntity<?> uploadStyle(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                         @RequestParam("file") List<MultipartFile> file){
        userService.uploadStyle(file, userPrincipal.getEmail());
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @DeleteMapping("/style")
    public ResponseEntity<?> deleteStyle(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                          @RequestParam("file") String filePath){
        userService.deleteStyle(filePath, userPrincipal.getEmail());
        return ResponseEntity.ok().body(ApiResponse.success());
    }
}
