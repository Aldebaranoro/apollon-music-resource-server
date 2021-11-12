package com.github.aldebaranoro.apollonmusicresourceserver.api.playlists.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity(name = "playlists")
public class Playlist extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId;

    @Column(name = "discord_identity")
    private String discordIdentity;

    @Column(name = "is_private", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isPrivate;

    @ManyToOne
    @JoinColumn(name = "music_service_id", nullable = false)
    private MusicServiceSource musicService;

    @OneToMany(
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    @JoinColumn(name = "playlist_id")
    private Set<Track> tracks;
}