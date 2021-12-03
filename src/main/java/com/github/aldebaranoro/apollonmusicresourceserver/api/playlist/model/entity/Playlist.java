package com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.entity;

import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.validation.DiscordIdentity;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.validation.UUID;
import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.entity.Track;
import com.github.aldebaranoro.apollonmusicresourceserver.basic.AbstractTimestampEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity(name = "playlists")
public class Playlist extends AbstractTimestampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @UUID
    @Column(name = "user_id", nullable = false, updatable = false)
    private String userId;

    @DiscordIdentity
    @Column(name = "discord_identity")
    private String discordIdentity;

    @Column(name = "is_private", nullable = false, columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isPrivate;

    @Valid
    @Size(max = 10)
    @OneToMany(
            mappedBy = "playlist",
            orphanRemoval = true,
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY
    )
    private List<Track> tracks = new ArrayList<>();

    public void addTrack(Track track) {
        tracks.add(track);
        track.setPlaylist(this);
    }

    public void removeTrack(Track track) {
        tracks.remove(track);
        track.setPlaylist(null);
    }
}