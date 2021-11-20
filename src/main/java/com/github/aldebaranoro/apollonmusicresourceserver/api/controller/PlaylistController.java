package com.github.aldebaranoro.apollonmusicresourceserver.api.controller;

import com.github.aldebaranoro.apollonmusicresourceserver.api.model.entity.Playlist;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.entity.Track;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.mapper.PlaylistMapper;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.view.PlaylistCreate;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.view.PlaylistRead;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.view.PlaylistUpdate;
import com.github.aldebaranoro.apollonmusicresourceserver.api.repository.PlaylistRepository;
import com.github.aldebaranoro.apollonmusicresourceserver.api.utils.KeycloakPrincipalUtils;
import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/users/me/playlists")
@RequiredArgsConstructor
class PlaylistController {

    private static final int TRACKS_MAX_COUNT = 10;

    private final PlaylistRepository playlistRepository;
    private final PlaylistMapper mapper = PlaylistMapper.INSTANCE;

    @GetMapping
    public List<PlaylistRead> read(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            KeycloakPrincipal<KeycloakSecurityContext> principal
    ) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        String userId = KeycloakPrincipalUtils.getUserId(principal);
        Page<Playlist> pagedResult = playlistRepository.findAllByUserId(paging, userId);
        return pagedResult.hasContent() ? mapper.toListViewRead(pagedResult.getContent()) : new ArrayList<>();
    }

    @PostMapping
    public PlaylistRead create(
            @RequestBody PlaylistCreate playlistCreate,
            KeycloakPrincipal<KeycloakSecurityContext> principal
    ) {
        Playlist playlist = mapper.toEntity(playlistCreate);
        playlist.setDiscordIdentity(KeycloakPrincipalUtils.getIdentityProviderId(principal));
        playlist.setUserId(KeycloakPrincipalUtils.getUserId(principal));
        checkTrackMaxCount(playlist);
        return mapper.toViewRead(
                playlistRepository.save(playlist)
        );
    }

    @GetMapping("/{id}")
    public PlaylistRead read(@PathVariable Long id, KeycloakPrincipal<KeycloakSecurityContext> principal) {
        Playlist playlist = playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не найдена сущность с заданным id!"));

        if (!playlist.getUserId().equals(KeycloakPrincipalUtils.getUserId(principal))) {
            throw new RuntimeException("У пользователя нету доступа к этому плейлисту!");
        }

        return mapper.toViewRead(playlist);
    }

    @PutMapping("/{id}")
    public PlaylistRead update(
            @PathVariable Long id,
            @RequestBody PlaylistUpdate playlistUpdate,
            KeycloakPrincipal<KeycloakSecurityContext> principal
    ) {
        if (id == null) {
            throw new RuntimeException("Id сущности должен быть не null!");
        }
        if (!playlistRepository.existsById(id)) {
            throw new RuntimeException("Не найдена сущность с заданным Id!");
        }
        Playlist playlist = mapper.toEntity(playlistUpdate);
        playlist.setId(id);
        playlist.setDiscordIdentity(KeycloakPrincipalUtils.getIdentityProviderId(principal));
        playlist.setUserId(KeycloakPrincipalUtils.getUserId(principal));
        checkTrackMaxCount(playlist);
        checkPlaylistTrackIds(playlist);
        return mapper.toViewRead(
                playlistRepository.save(playlist)
        );
    }

    @DeleteMapping("/{id}")
    public PlaylistRead delete(@PathVariable Long id) {
        PlaylistRead playlist = playlistRepository.findById(id)
                .map(mapper::toViewRead)
                .orElseThrow(() -> new RuntimeException("Не найдена сущность с заданным Id!"));
        playlistRepository.deleteById(id);
        return playlist;
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

    private void checkTrackMaxCount(Playlist playlist) {
        if (playlist.getTracks().size() > TRACKS_MAX_COUNT) {
            String message = String.format(
                    "Плейлист превышает допустимое число треков! Максимальная ёмкость плейлиста %s",
                    TRACKS_MAX_COUNT
            );
            throw new RuntimeException(message);
        }
    }
}
