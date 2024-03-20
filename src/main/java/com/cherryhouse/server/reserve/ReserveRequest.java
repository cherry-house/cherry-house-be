package com.cherryhouse.server.reserve;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class ReserveRequest {

    public record MakeReserveDto(

            @NotBlank
            String receiver,

            @NotNull
            Long postId,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
            LocalDate reservationDate
    ){}

    public record ChangeReserveDto(

            @NotBlank
            String receiver,

            @NotNull
            Long postId,

            Status status,

            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
            LocalDate reservationDate
    ){}
}
