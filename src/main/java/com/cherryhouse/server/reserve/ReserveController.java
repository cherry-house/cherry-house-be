package com.cherryhouse.server.reserve;

import com.cherryhouse.server._core.security.UserPrincipal;
import com.cherryhouse.server._core.util.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reserve")
@RequiredArgsConstructor
public class ReserveController {

    private final ReserveService reserveService;

    @Operation(summary = "예약하기", description = "사용자가 일정을 예약합니다.")
    @PostMapping
    public ResponseEntity<?> reserve(@AuthenticationPrincipal UserPrincipal user,
                                     @Valid @RequestBody ReserveRequest.MakeReserveDto makeReserveDto){
        reserveService.reserve(user, makeReserveDto);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @Operation(summary = "예약 수정", description = "사용자가 예약 상태를 변경합니다.")
    @PutMapping
    public ResponseEntity<?> updateReserve(@AuthenticationPrincipal UserPrincipal user,
                                           @Valid @RequestBody ReserveRequest.ChangeReserveDto changeReserveDto){
        reserveService.update(user, changeReserveDto);
        return ResponseEntity.ok().body(ApiResponse.success());
    }

    @Operation(summary = "예약 목록 조회", description = "사용자가 예약 목록을 조회합니다.")
    @GetMapping
    public ResponseEntity<?> getReservation(@AuthenticationPrincipal UserPrincipal user, Pageable pageable){
        ReserveResponse.ReserveDto response = reserveService.get(user.getEmail(), pageable);
        return ResponseEntity.ok().body(ApiResponse.success(response));
    }
}
