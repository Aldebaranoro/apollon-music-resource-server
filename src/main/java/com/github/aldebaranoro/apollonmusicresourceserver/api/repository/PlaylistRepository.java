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

    /**
     * Возвращает список всех плейлистов отправителя и публичных других пользователей
     *
     * @param pageable                   пагинация
     * @param requesterDiscordIdentity   Discord identity отправителя запроса
     * @param requestedDiscordIdentities Discord identities, по которым происходит фильтрация
     * @return список всех плейлистов отправителя и публичных других пользователей
     */
    @Query("select p from playlists p " +
            "where p.isPrivate = false and p.discordIdentity in :requestedDiscordIdentities" +
            "       or p.discordIdentity = :requesterDiscordIdentity" +
            "       and (:playlistName is null or p.name like concat('%', :playlistName, '%'))")
    Page<Playlist> findAllPlaylistsByDiscordIdentities(
            Pageable pageable,
            @Param("requesterDiscordIdentity") String requesterDiscordIdentity,
            @Param("requestedDiscordIdentities") Set<String> requestedDiscordIdentities,
            @Param("playlistName") String playlistName
    );
}