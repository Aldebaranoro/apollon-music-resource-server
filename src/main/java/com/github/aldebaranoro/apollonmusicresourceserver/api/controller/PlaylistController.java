package com.github.aldebaranoro.apollonmusicresourceserver.api.controller;

import com.github.aldebaranoro.apollonmusicresourceserver.api.model.entity.Playlist;
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

@RestController
@RequestMapping("api/v1/users/me/playlists")
@RequiredArgsConstructor
class PlaylistController {

    private final PlaylistRepository playlistRepository;
    private PlaylistMapper mapper = PlaylistMapper.INSTANCE;

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
    public PlaylistRead create(@RequestBody PlaylistCreate playlist) {
        return mapper.toViewRead(
                playlistRepository.save(
                        mapper.toEntity(playlist)
                )
        );
    }

    @GetMapping("/{id}")
    public PlaylistRead read(@PathVariable Long id) {
        return playlistRepository.findById(id)
                .map(mapper::toViewRead)
                .orElseThrow(() -> new RuntimeException("Не найдена сущность с заданным id!"));
    }

    @PutMapping("/{id}")
    public PlaylistRead update(@PathVariable Long id, @RequestBody PlaylistUpdate playlistUpdate) {
        if (id == null) {
            throw new RuntimeException("Id сущности должен быть не null!");
        }
        if (!playlistRepository.existsById(id)) {
            throw new RuntimeException("Не найдена сущность с заданным Id!");
        }
        Playlist playlist = mapper.toEntity(playlistUpdate);
        playlist.setId(id);
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
}
