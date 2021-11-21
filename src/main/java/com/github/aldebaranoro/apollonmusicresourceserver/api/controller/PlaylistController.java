package com.github.aldebaranoro.apollonmusicresourceserver.api.controller;

import com.github.aldebaranoro.apollonmusicresourceserver.api.model.entity.Playlist;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.mapper.PlaylistMapper;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.view.PlaylistCreate;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.view.PlaylistRead;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.view.PlaylistUpdate;
import com.github.aldebaranoro.apollonmusicresourceserver.api.service.PlaylistService;
import com.github.aldebaranoro.apollonmusicresourceserver.api.utils.KeycloakPrincipalUtils;
import com.github.aldebaranoro.apollonmusicresourceserver.exception.dto.ForbiddenException;
import com.github.aldebaranoro.apollonmusicresourceserver.exception.dto.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.security.InvalidParameterException;
import java.util.List;

@RestController
@RequestMapping("api/v1/users/me/playlists")
@RequiredArgsConstructor
class PlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistMapper mapper = PlaylistMapper.INSTANCE;

    @GetMapping
    public List<PlaylistRead> read(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            KeycloakPrincipal<KeycloakSecurityContext> principal
    ) {
        String userId = KeycloakPrincipalUtils.getUserId(principal);
        return mapper.toListViewRead(
                playlistService.getPlaylistsByUserId(pageNumber, pageSize, sortBy, userId)
        );
    }

    @PostMapping
    public PlaylistRead create(
            @RequestBody PlaylistCreate playlistCreate,
            KeycloakPrincipal<KeycloakSecurityContext> principal
    ) {
        Playlist playlist = mapper.toEntity(playlistCreate);
        playlist.setDiscordIdentity(KeycloakPrincipalUtils.getIdentityProviderId(principal));
        playlist.setUserId(KeycloakPrincipalUtils.getUserId(principal));
        return mapper.toViewRead(
                playlistService.createPlaylist(playlist)
        );
    }

    @GetMapping("/{id}")
    public PlaylistRead read(@PathVariable Long id, KeycloakPrincipal<KeycloakSecurityContext> principal) {
        Playlist playlist = playlistService.getPlaylistById(id);

        if (!playlist.getUserId().equals(KeycloakPrincipalUtils.getUserId(principal))) {
            throw new ForbiddenException("У пользователя нету доступа к этому плейлисту!");
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
            throw new InvalidParameterException("Id сущности должен быть не null!");
        }
        if (!playlistService.playlistExistById(id)) {
            throw new ResourceNotFoundException("Не найдена сущность с заданным Id!");
        }
        Playlist playlist = mapper.toEntity(playlistUpdate);
        playlist.setId(id);
        playlist.setDiscordIdentity(KeycloakPrincipalUtils.getIdentityProviderId(principal));
        playlist.setUserId(KeycloakPrincipalUtils.getUserId(principal));
        return mapper.toViewRead(
                playlistService.updatePlaylist(playlist)
        );
    }

    @DeleteMapping("/{id}")
    public PlaylistRead delete(@PathVariable Long id, KeycloakPrincipal<KeycloakSecurityContext> principal) {
        Playlist playlist = playlistService.getPlaylistById(id);
        if (!playlist.getUserId().equals(KeycloakPrincipalUtils.getUserId(principal))) {
            throw new ForbiddenException("У пользователя нету доступа к этому плейлисту!");
        }
        return mapper.toViewRead(
                playlistService.deletePlaylistById(id)
        );
    }
}
