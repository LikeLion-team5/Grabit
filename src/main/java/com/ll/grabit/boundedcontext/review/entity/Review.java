package com.ll.grabit.boundedcontext.review.entity;

import com.ll.grabit.boundedcontext.member.entity.Member;
import com.ll.grabit.boundedcontext.restaurant.entity.Restaurant;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;
import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor
public class Review {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String content;

    private String nickname;

    private int rating;

    @ManyToOne(fetch = LAZY)
    private Restaurant restaurant;

    @ManyToOne
    private Member reviewer;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
