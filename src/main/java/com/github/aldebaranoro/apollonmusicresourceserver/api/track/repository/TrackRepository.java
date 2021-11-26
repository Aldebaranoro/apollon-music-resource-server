package com.github.aldebaranoro.apollonmusicresourceserver.api.track.repository;

import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.entity.Track;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TrackRepository extends JpaRepository<Track, Long> {

    @Query("select p.tracks from playlists p where p.id = :playlistId and p.userId = :userId")
    Page<Track> findPlaylistTracksByUserId(
            Pageable pageable,
            @Param("playlistId") Long playlistId,
            @Param("userId") String userId
    );
}