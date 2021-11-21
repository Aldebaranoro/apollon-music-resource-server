package com.github.aldebaranoro.apollonmusicresourceserver.api.repository;

import com.github.aldebaranoro.apollonmusicresourceserver.api.model.entity.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Page<Playlist> findAllByIsPrivateFalse(Pageable pageable);

    @Query("select p from playlists p where p.isPrivate = false and p.discordIdentity in :discordIdentities")
    Page<Playlist> findAllPublicPlaylistsByDiscordIdentities(
            Pageable pageable,
            @Param("discordIdentities") Set<String> discordIdentities
    );

    Page<Playlist> findAllByUserId(Pageable pageable, String userId);

    @Query("select p from playlists p where p.discordIdentity in :discordIdentities")
    Page<Playlist> findAllPlaylistsByDiscordIdentities(
            Pageable pageable,
            @Param("discordIdentities") Set<String> discordIdentities
    );
}