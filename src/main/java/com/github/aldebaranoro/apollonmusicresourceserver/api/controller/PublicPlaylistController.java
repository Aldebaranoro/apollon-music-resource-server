package com.github.aldebaranoro.apollonmusicresourceserver.api.controller;

import com.github.aldebaranoro.apollonmusicresourceserver.api.model.mapper.PlaylistMapper;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.view.PlaylistReadById;
import com.github.aldebaranoro.apollonmusicresourceserver.api.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("public/api/v1/playlists")
@RequiredArgsConstructor
public class PublicPlaylistController {

    private final PlaylistService playlistService;
    private PlaylistMapper mapper = PlaylistMapper.INSTANCE;

    @GetMapping
    public List<PlaylistReadById> findAllPublicPlaylists(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return mapper.toListViewRead(
                playlistService.getPublicPlaylists(pageNumber, pageSize, sortBy)
        );
    }

    @PostMapping("/discord")
    public List<PlaylistReadById> findAllPublicPlaylistsByDiscordIdentities(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestBody Set<String> discordIdentities
    ) {
        return mapper.toListViewRead(
                playlistService.getPublicPlaylistsByDiscordIds(pageNumber, pageSize, sortBy, discordIdentities)
        );
    }
}
