package com.github.aldebaranoro.apollonmusicresourceserver.api.controller;

import com.github.aldebaranoro.apollonmusicresourceserver.api.model.Playlist;
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

    @GetMapping
    public List<Playlist> read(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            KeycloakPrincipal<KeycloakSecurityContext> principal
    ) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        String userId = KeycloakPrincipalUtils.getUserId(principal);
        Page<Playlist> pagedResult = playlistRepository.findAllByUserId(paging, userId);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    @PostMapping
    public Playlist create(@RequestBody Playlist playlist) {
        return playlistRepository.saveAndFlush(playlist);
    }

    @GetMapping("/{id}")
    public Playlist read(@PathVariable Long id) {
        return playlistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Не найдена сущность с заданным id!"));
    }

    @PutMapping
    public Playlist update(@RequestBody Playlist playlist) {
        if (playlist.getId() == null) {
            throw new RuntimeException("Id сущности должен быть не null!");
        }
        if (!playlistRepository.existsById(playlist.getId())) {
            throw new RuntimeException("Не найдена сущность с заданным Id!");
        }
        return playlistRepository.save(playlist);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        playlistRepository.deleteById(id);
    }
}
