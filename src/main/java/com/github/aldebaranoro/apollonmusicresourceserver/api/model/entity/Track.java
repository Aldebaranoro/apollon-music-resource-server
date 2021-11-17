package com.github.aldebaranoro.apollonmusicresourceserver.api.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "tracks")
public class Track extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String url;

    @Column(nullable = false)
    private String label;

    @ManyToOne
    @JoinColumn(name = "music_service_id", nullable = false)
    private MusicServiceSource musicService;
}