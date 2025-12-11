package com.system.emails.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "emails")
public class EmailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emisor_id")
    private UserEntity emisor;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "email_destinatarios",
        joinColumns = @JoinColumn(name = "email_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<UserEntity> destinatarios = new ArrayList<>();

    private String subject;

    @Column(columnDefinition = "TEXT")
    private String message;

    private String status;

    private LocalDateTime sentAt;

    
}
