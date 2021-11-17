package com.github.aldebaranoro.apollonmusicresourceserver.api.controller;

import com.github.aldebaranoro.apollonmusicresourceserver.api.model.entity.Playlist;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.mapper.PlaylistMapper;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.view.PlaylistRead;
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
    private PlaylistMapper mapper = PlaylistMapper.INSTANCE;

    @GetMapping
    public List<PlaylistRead> findAllPublicPlaylists(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Playlist> pagedResult = playlistRepository.findAllByIsPrivateFalse(paging);
        return pagedResult.hasContent() ? mapper.toListViewRead(pagedResult.getContent()) : new ArrayList<>();
    }

    @GetMapping("/discord")
    public List<PlaylistRead> findAllPublicPlaylistsByDiscordIdentities(
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
        return pagedResult.hasContent() ? mapper.toListViewRead(pagedResult.getContent()) : new ArrayList<>();
    }
}
