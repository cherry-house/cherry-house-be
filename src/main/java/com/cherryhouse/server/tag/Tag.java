package com.cherryhouse.server.tag;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "tag_tb")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String createdDate;

    @Builder
    public Tag(String name) {
        this.name = name;
        this.createdDate = String.valueOf(LocalDateTime.now());
    }
}
