package com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.controller;

import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.entity.Playlist;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.mapper.PlaylistMapper;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view.PlaylistCreate;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view.PlaylistRead;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view.PlaylistReadById;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view.PlaylistUpdate;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.service.PlaylistService;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.validation.DiscordIdentity;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.validation.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("admin/api/v1/playlists")
@RequiredArgsConstructor
class AdminPlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistMapper mapper = PlaylistMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<PlaylistRead>> read(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        var result = mapper.toListViewRead(
                playlistService.getPlaylists(pageNumber, pageSize, sortBy)
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/discord")
    public ResponseEntity<List<PlaylistRead>> readByDiscordIdentities(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @DiscordIdentity @RequestParam String requesterDiscordIdentity,
            @RequestParam List<@DiscordIdentity String> requestedDiscordIdentities,
            @RequestParam(required = false) String playlistName
    ) {
        var result = mapper.toListViewRead(
                playlistService.getPlaylistsByDiscordIds(
                        pageNumber,
                        pageSize,
                        sortBy,
                        requesterDiscordIdentity,
                        requestedDiscordIdentities,
                        playlistName
                )
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PlaylistReadById> create(
            @UUID @RequestParam String userId,
            @DiscordIdentity @RequestParam(required = false) String discordIdentity,
            @Valid @RequestBody PlaylistCreate playlistCreate
    ) {
        Playlist playlist = mapper.toEntity(playlistCreate);
        playlist.setUserId(userId);
        playlist.setDiscordIdentity(discordIdentity);
        var result = mapper.toViewReadById(
                playlistService.createPlaylist(playlist)
        );
        return new ResponseEntity<>(result, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistReadById> read(@PathVariable Long id) {
        var result = mapper.toViewReadById(
                playlistService.getPlaylistById(id)
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaylistReadById> update(
            @PathVariable Long id,
            @UUID @RequestParam String userId,
            @DiscordIdentity @RequestParam(required = false) String discordIdentity,
            @Valid @RequestBody PlaylistUpdate playlistUpdate
    ) {
        Playlist playlist = mapper.toEntity(playlistUpdate);
        playlist.setUserId(userId);
        playlist.setDiscordIdentity(discordIdentity);

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
    public void delete(@PathVariable Long id) {
        playlistService.deletePlaylistById(id);
    }
}
