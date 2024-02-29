package com.cherryhouse.server.reserve;


import com.cherryhouse.server.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "reserve_tb")
public class Reserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    private User provider;

    @ManyToOne(fetch = FetchType.LAZY)
    private User receiver;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private LocalDate reservationDate;

    @Column
    private LocalDate createDate;



    @Builder
    public Reserve(Long postId, User provider, User receiver, Status status, LocalDate createDate, LocalDate reservationDate){
        this.postId = postId;
        this.provider = provider;
        this.receiver = receiver;
        this.status = status;
        this.createDate = createDate;
        this.reservationDate = reservationDate;

    }

    public void update(LocalDate reservationDate, Status status) {
        this.reservationDate = reservationDate;
        this.status = status;

    }
}
