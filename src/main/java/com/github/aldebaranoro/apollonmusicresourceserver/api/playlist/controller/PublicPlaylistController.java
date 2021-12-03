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
    private PlaylistMapper mapper = PlaylistMapper.INSTANCE;

    @GetMapping
    public ResponseEntity<List<PlaylistRead>> findAllPublicPlaylists(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        var result = mapper.toListViewRead(
                playlistService.getPublicPlaylists(pageNumber, pageSize, sortBy)
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/discord")
    public ResponseEntity<List<PlaylistRead>> findAllPublicPlaylistsByDiscordIdentities(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam List<@DiscordIdentity String> discordIdentities
    ) {
        var result = mapper.toListViewRead(
                playlistService.getPublicPlaylistsByDiscordIds(pageNumber, pageSize, sortBy, discordIdentities)
        );
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
