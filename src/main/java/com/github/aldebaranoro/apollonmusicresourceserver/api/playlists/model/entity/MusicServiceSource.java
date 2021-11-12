package com.github.aldebaranoro.apollonmusicresourceserver.api.playlists.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity(name = "dic_music_service_sources")
public class MusicServiceSource extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Integer id;

    @Column(nullable = false, updatable = false, unique = true)
    private String name;
}