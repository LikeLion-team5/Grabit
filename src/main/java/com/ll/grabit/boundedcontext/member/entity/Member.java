package com.ll.grabit.boundedcontext.member.entity;


import com.ll.grabit.boundedcontext.reservation.entity.Reservation;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(unique = true)
    private Long id;

    private String nickname;

    private String username;

    private String password;

    private String email;

    private String phone;

    @OneToMany(mappedBy = "member")
    @Builder.Default
    private List<Reservation> reservations = new ArrayList<>();

    @CreatedDate
    private LocalDateTime createDate;
    private String providerTypeCode;
    @LastModifiedDate
    private LocalDateTime modifyDate;

    public List<? extends GrantedAuthority> getGrantedAuthorities() {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();

        grantedAuthorities.add(new SimpleGrantedAuthority("member"));

        if ("admin".equals(username)) {
            grantedAuthorities.add(new SimpleGrantedAuthority("admin"));
        }

        return grantedAuthorities;
    }

    public void updateProfile(String nickname, String email, String phone) {
        this.nickname = nickname;
        this.email = email;
        this.phone = phone;
    }
}