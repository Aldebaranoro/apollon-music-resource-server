package com.github.aldebaranoro.apollonmusicresourceserver.api.controller;

import com.github.aldebaranoro.apollonmusicresourceserver.api.model.Playlist;
import com.github.aldebaranoro.apollonmusicresourceserver.api.repository.PlaylistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("public/api/v1/playlists")
@RequiredArgsConstructor
public class PublicPlaylistController {

    private final PlaylistRepository playlistRepository;

    @GetMapping
    public List<Playlist> findAllPublicPlaylists(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Playlist> pagedResult = playlistRepository.findAllByIsPrivateFalse(paging);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    @GetMapping("/discord")
    public List<Playlist> findAllPublicPlaylistsByDiscordIdentities(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestBody Set<String> discordIdentities
    ) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Playlist> pagedResult = playlistRepository.findAllPublicPlaylistsByDiscordIdentities(
                paging,
                discordIdentities
        );
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }
}
