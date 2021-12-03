package com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.service;

import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.entity.Playlist;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.repository.PlaylistRepository;
import com.github.aldebaranoro.apollonmusicresourceserver.exception.dto.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
@RequiredArgsConstructor
public class PlaylistService {

    private final PlaylistRepository playlistRepository;

    public List<Playlist> getPlaylistsByUserId(Integer pageNumber, Integer pageSize, String sortBy, String userId) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Playlist> pagedResult = playlistRepository.findAllByUserId(paging, userId);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    public Playlist createPlaylist(@Valid Playlist playlist) {
        return playlistRepository.save(playlist);
    }

    public Playlist getPlaylistById(Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Не найден плейлист с заданным id!"));
    }

    public Playlist updatePlaylist(@Valid Playlist playlist) {
        // FIXME: Добавляю треки из бд, чтобы не жаловалось на каскад.
        //  Необходимо пофиксить, чтобы без этой херни работало
        var tracks = getPlaylistById(playlist.getId()).getTracks();
        playlist.setTracks(tracks);

        return playlistRepository.save(playlist);
    }

    public void deletePlaylistById(Long id) {
        if (!playlistExistById(id)) {
            throw new ResourceNotFoundException("Не найден плейлист с заданным Id!");
        }
        playlistRepository.deleteById(id);
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
            List<String> discordIds
    ) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Playlist> pagedResult = playlistRepository.findAllPublicPlaylistsByDiscordIdentities(paging, discordIds);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    public List<Playlist> getPlaylistsByDiscordIds(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            String requesterDiscordIdentity,
            List<String> requestedDiscordIdentities,
            String playlistName
    ) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Playlist> pagedResult;

        if (playlistName == null) {
            pagedResult = playlistRepository.findAllPlaylistsByDiscordIdentities(
                    paging,
                    requesterDiscordIdentity,
                    requestedDiscordIdentities
            );
        } else {
            pagedResult = playlistRepository.findAllPlaylistsByDiscordIdentitiesAndPlaylistName(
                    paging,
                    requesterDiscordIdentity,
                    requestedDiscordIdentities,
                    playlistName.trim()
            );
        }

        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    public List<Playlist> getPlaylists(Integer pageNumber, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Playlist> pagedResult = playlistRepository.findAll(paging);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }
}
