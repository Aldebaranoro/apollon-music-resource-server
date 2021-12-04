package com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.controller;

import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.mapper.PlaylistMapper;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view.PlaylistRead;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.service.PlaylistService;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.validation.DiscordIdentity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequestMapping("public/api/v1/playlists")
@RequiredArgsConstructor
public class PublicPlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistMapper mapper = PlaylistMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<PlaylistRead>> findAllPublicPlaylists(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(required = false) List<@DiscordIdentity String> discordIdentities
    ) {
        var playlists = discordIdentities == null || discordIdentities.isEmpty()
                ? playlistService.getPublicPlaylists(pageNumber, pageSize, sortBy)
                : playlistService.getPublicPlaylistsByDiscordIds(pageNumber, pageSize, sortBy, discordIdentities);
        var result = mapper.toListViewRead(playlists);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
