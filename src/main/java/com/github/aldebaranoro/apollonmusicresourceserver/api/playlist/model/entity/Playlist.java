package com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.entity;

import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.entity.Track;
import com.github.aldebaranoro.apollonmusicresourceserver.basic.AbstractTimestampEntity;
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

    @OneToMany(
            mappedBy = "playlist",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Set<Track> tracks;
}