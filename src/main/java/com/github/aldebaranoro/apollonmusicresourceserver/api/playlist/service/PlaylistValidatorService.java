package com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.service;

import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.entity.Playlist;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.repository.PlaylistRepository;
import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.entity.Track;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.InvalidParameterException;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlaylistValidatorService {

    private final int tracksMaxCount;

    private final PlaylistRepository playlistRepository;

    public PlaylistValidatorService(
            @Value("${limit.track.max-count}") int tracksMaxCount,
            PlaylistRepository playlistRepository
    ) {
        this.tracksMaxCount = tracksMaxCount;
        this.playlistRepository = playlistRepository;
    }

    /**
     * Проверка на максимальное количество треков в плейлисте
     *
     * @param playlist плейлист с треками
     */
    public void checkTrackMaxCount(Playlist playlist) {
        if (playlist.getTracks().size() > tracksMaxCount) {
            String message = String.format(
                    "Плейлист превышает допустимое число треков! Максимальная ёмкость плейлиста %s",
                    tracksMaxCount
            );
            throw new InvalidParameterException(message);
        }
    }

    /**
     * Проверка на наличие чужеродных треков
     *
     * @param updatedPlaylist плейлист с изменениями
     */
    public void checkPlaylistTrackIds(Playlist updatedPlaylist) {
        Playlist playlist = playlistRepository.getById(updatedPlaylist.getId());
        Set<Long> trackIds = getTrackIds(playlist);
        Set<Long> updatedTrackIds = getTrackIds(updatedPlaylist);

        updatedTrackIds.forEach(id -> {
            if (id != null && !trackIds.contains(id)) {
                String message = String.format(
                        "Нельзя добавить в плейлист трек с id = %s, так как он принадлежит другому плейлисту!",
                        id
                );
                throw new InvalidParameterException(message);
            }
        });
    }

    private Set<Long> getTrackIds(Playlist playlist) {
        return playlist
                .getTracks()
                .stream()
                .map(Track::getId)
                .collect(Collectors.toSet());
    }
}
