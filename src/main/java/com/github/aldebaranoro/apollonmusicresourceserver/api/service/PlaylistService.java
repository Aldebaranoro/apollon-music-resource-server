package com.github.aldebaranoro.apollonmusicresourceserver.api.service;

import com.github.aldebaranoro.apollonmusicresourceserver.api.model.entity.Playlist;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.entity.Track;
import com.github.aldebaranoro.apollonmusicresourceserver.api.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaylistService {

    private static final int TRACKS_MAX_COUNT = 10;

    private final PlaylistRepository playlistRepository;

    public List<Playlist> getPlaylistsByUserId(Integer pageNumber, Integer pageSize, String sortBy, String userId) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Playlist> pagedResult = playlistRepository.findAllByUserId(paging, userId);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    public Playlist createPlaylist(Playlist playlist) {
        checkTrackMaxCount(playlist);
        return playlistRepository.save(playlist);
    }

    public Playlist getPlaylistById(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не найдена сущность с заданным id!"));
    }

    public Playlist updatePlaylist(Playlist playlist) {
        checkTrackMaxCount(playlist);
        checkPlaylistTrackIds(playlist);
        return playlistRepository.save(playlist);
    }

    public Playlist deletePlaylistById(Long id) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не найдена сущность с заданным Id!"));
        playlistRepository.deleteById(id);
        return playlist;
    }

    public Boolean playlistExistById(Long id) {
        return playlistRepository.existsById(id);
    }

    public List<Playlist> getPublicPlaylists(Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Playlist> pagedResult = playlistRepository.findAllByIsPrivateFalse(paging);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    public List<Playlist> getPublicPlaylistsByDiscordIds(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            Set<String> discordIds
    ) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Playlist> pagedResult = playlistRepository.findAllPublicPlaylistsByDiscordIdentities(paging, discordIds);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    public List<Playlist> getPlaylistsByDiscordIds(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            Set<String> discordIds
    ) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Playlist> pagedResult = playlistRepository.findAllPlaylistsByDiscordIdentities(paging, discordIds);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    public List<Playlist> getPlaylists(Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Playlist> pagedResult = playlistRepository.findAll(paging);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    private void checkTrackMaxCount(Playlist playlist) {
        if (playlist.getTracks().size() > TRACKS_MAX_COUNT) {
            String message = String.format(
                    "Плейлист превышает допустимое число треков! Максимальная ёмкость плейлиста %s",
                    TRACKS_MAX_COUNT
            );
            throw new RuntimeException(message);
        }
    }

    /**
     * Проверка на наличие чужеродных треков
     *
     * @param updatedPlaylist плейлист с изменениями
     */
    private void checkPlaylistTrackIds(Playlist updatedPlaylist) {
        Playlist playlist = playlistRepository.getById(updatedPlaylist.getId());
        Set<Long> trackIds = getTrackIds(playlist);
        Set<Long> updatedTrackIds = getTrackIds(updatedPlaylist);

        updatedTrackIds.forEach(id -> {
            if (id != null && !trackIds.contains(id)) {
                String message = String.format(
                        "Нельзя добавить в плейлист трек с id = %s, так как он принадлежит другому плейлисту!",
                        id
                );
                throw new RuntimeException(message);
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
