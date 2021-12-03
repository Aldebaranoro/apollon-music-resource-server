package com.github.aldebaranoro.apollonmusicresourceserver.api.track.controller;

import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.controller.PlaylistController;
import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.entity.Track;
import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.mapper.TrackMapper;
import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.view.TrackCreate;
import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.view.TrackRead;
import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.view.TrackReadById;
import com.github.aldebaranoro.apollonmusicresourceserver.api.track.service.TrackService;
import com.github.aldebaranoro.apollonmusicresourceserver.api.utils.KeycloakPrincipalUtils;
import lombok.RequiredArgsConstructor;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.KeycloakSecurityContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;

@Validated
@RestController
@RequestMapping("api/v1/users/me/playlists/{playlistId}/tracks")
@RequiredArgsConstructor
class TrackController {

    private final TrackService trackService;
    private final PlaylistController playlistController;
    private final Validator validator;
    private final TrackMapper mapper = TrackMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<TrackRead>> read(
            @PathVariable Long playlistId,
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            KeycloakPrincipal<KeycloakSecurityContext> principal
    ) {
        playlistController.read(playlistId, principal);
        String userId = KeycloakPrincipalUtils.getUserId(principal);
        var result = mapper.toListViewRead(
                trackService.getPlaylistTracksByUserId(pageNumber, pageSize, sortBy, playlistId, userId)
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TrackReadById> create(
            @PathVariable Long playlistId,
            @Valid @RequestBody TrackCreate trackCreate,
            KeycloakPrincipal<KeycloakSecurityContext> principal
    ) {
        Track track = mapper.toEntity(trackCreate);
        track.setPlaylist(playlistController.getPlaylistById(playlistId, principal));
        var result = mapper.toViewReadById(
                trackService.createTrack(track)
        );
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TrackReadById> read(
            @PathVariable Long playlistId,
            @PathVariable Long id,
            KeycloakPrincipal<KeycloakSecurityContext> principal
    ) {
        playlistController.getPlaylistById(playlistId, principal);

        var result = mapper.toViewReadById(
                trackService.getTrackById(id)
        );

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable Long playlistId,
            @PathVariable Long id,
            KeycloakPrincipal<KeycloakSecurityContext> principal
    ) {
        playlistController.getPlaylistById(playlistId, principal);
        trackService.deleteTrackById(id);
    }
}
