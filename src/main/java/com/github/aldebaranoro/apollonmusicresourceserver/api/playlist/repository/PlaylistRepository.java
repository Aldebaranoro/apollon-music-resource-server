package com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.repository;

import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.entity.Playlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

    Page<Playlist> findAllByIsPrivateFalse(Pageable pageable);

    @Query("select p from playlists p where p.isPrivate = false and p.discordIdentity in :discordIdentities")
    Page<Playlist> findAllPublicPlaylistsByDiscordIdentities(
            Pageable pageable,
            @Param("discordIdentities") Iterable<String> discordIdentities
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
            "where p.isPrivate = false and p.discordIdentity in :requestedDiscordIdentities " +
            "or p.discordIdentity = :requesterDiscordIdentity and p.discordIdentity in :requestedDiscordIdentities")
    Page<Playlist> findAllPlaylistsByDiscordIdentities(
            Pageable pageable,
            @Param("requesterDiscordIdentity") String requesterDiscordIdentity,
            @Param("requestedDiscordIdentities") Iterable<String> requestedDiscordIdentities
    );

    // TODO: временное решение, лучше использовать полнотекстовый поиск с исправлением ошибок

    /**
     * Возвращает список всех плейлистов отправителя и публичных других пользователей
     *
     * @param pageable                   пагинация
     * @param requesterDiscordIdentity   Discord identity отправителя запроса
     * @param requestedDiscordIdentities Discord identities, по которым происходит фильтрация
     * @return список всех плейлистов отправителя и публичных других пользователей
     */
    @Query("select p from playlists p " +
            "where (p.isPrivate = false and p.discordIdentity in :requestedDiscordIdentities" +
            "       or p.discordIdentity = :requesterDiscordIdentity and p.discordIdentity in :requestedDiscordIdentities)" +
            "       and lower(p.name) like lower(concat('%', :playlistName, '%')) ")
    Page<Playlist> findAllPlaylistsByDiscordIdentitiesAndPlaylistName(
            Pageable pageable,
            @Param("requesterDiscordIdentity") String requesterDiscordIdentity,
            @Param("requestedDiscordIdentities") Iterable<String> requestedDiscordIdentities,
            @Param("playlistName") String playlistName
    );
}