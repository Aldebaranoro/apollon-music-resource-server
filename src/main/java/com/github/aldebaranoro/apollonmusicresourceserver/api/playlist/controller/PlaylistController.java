package com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.controller;

import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.entity.Playlist;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.mapper.PlaylistMapper;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view.PlaylistCreate;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view.PlaylistRead;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view.PlaylistReadById;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view.PlaylistUpdate;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.service.PlaylistService;
import com.github.aldebaranoro.apollonmusicresourceserver.api.utils.KeycloakPrincipalUtils;
import com.github.aldebaranoro.apollonmusicresourceserver.exception.dto.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/users/me/playlists")
@RequiredArgsConstructor
public class PlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistMapper mapper = PlaylistMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<PlaylistRead>> read(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            KeycloakPrincipal<KeycloakSecurityContext> principal
    ) {
        String userId = KeycloakPrincipalUtils.getUserId(principal);
        var result = mapper.toListViewRead(
                playlistService.getPlaylistsByUserId(pageNumber, pageSize, sortBy, userId)
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PlaylistReadById> create(
            @RequestBody PlaylistCreate playlistCreate,
            KeycloakPrincipal<KeycloakSecurityContext> principal
    ) {
        Playlist playlist = mapper.toEntity(playlistCreate);
        playlist.setDiscordIdentity(KeycloakPrincipalUtils.getIdentityProviderId(principal));
        playlist.setUserId(KeycloakPrincipalUtils.getUserId(principal));
        var result = mapper.toViewReadById(
                playlistService.createPlaylist(playlist)
        );
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistReadById> read(
            @PathVariable Long id,
            KeycloakPrincipal<KeycloakSecurityContext> principal
    ) {
        var result = mapper.toViewReadById(getPlaylistById(id, principal));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * Обновляет плейлист по-заданному id. Если плейлиста с таким id не существует, то создает новый.
     *
     * @param id             Идентификатор плейлиста
     * @param playlistUpdate Тело запроса с данными плейлиста для обновления
     * @param principal      Данные авторизованного пользователя
     * @return измененный или созданный плейлист
     */
    @PutMapping("/{id}")
    public ResponseEntity<PlaylistReadById> update(
            @PathVariable Long id,
            @RequestBody PlaylistUpdate playlistUpdate,
            KeycloakPrincipal<KeycloakSecurityContext> principal
    ) {
        Playlist playlist = mapper.toEntity(playlistUpdate);
        playlist.setDiscordIdentity(KeycloakPrincipalUtils.getIdentityProviderId(principal));
        playlist.setUserId(KeycloakPrincipalUtils.getUserId(principal));

        Playlist updatedPlaylist;
        HttpStatus httpStatus;
        if (playlistService.playlistExistById(id)) {
            playlist.setId(id);
            updatedPlaylist = playlistService.updatePlaylist(playlist);
            httpStatus = HttpStatus.OK;
        } else {
            updatedPlaylist = playlistService.createPlaylist(playlist);
            httpStatus = HttpStatus.CREATED;
        }

        var result = mapper.toViewReadById(updatedPlaylist);

        return new ResponseEntity<>(result, httpStatus);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id, KeycloakPrincipal<KeycloakSecurityContext> principal) {
        Playlist playlist = playlistService.getPlaylistById(id);
        if (!playlist.getUserId().equals(KeycloakPrincipalUtils.getUserId(principal))) {
            throw new ForbiddenException("У пользователя нету доступа к этому плейлисту!");
        }
        playlistService.deletePlaylistById(id);
    }

    public Playlist getPlaylistById(Long id, KeycloakPrincipal<KeycloakSecurityContext> principal) {
        Playlist playlist = playlistService.getPlaylistById(id);

        if (!playlist.getUserId().equals(KeycloakPrincipalUtils.getUserId(principal))) {
            throw new ForbiddenException("У пользователя нету доступа к этому плейлисту!");
        }
        return playlist;
    }
}
