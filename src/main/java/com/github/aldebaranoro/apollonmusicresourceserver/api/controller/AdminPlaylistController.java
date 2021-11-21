package com.github.aldebaranoro.apollonmusicresourceserver.api.controller;

import com.github.aldebaranoro.apollonmusicresourceserver.api.model.entity.Playlist;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.mapper.PlaylistMapper;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.view.PlaylistCreate;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.view.PlaylistRead;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.view.PlaylistUpdate;
import com.github.aldebaranoro.apollonmusicresourceserver.api.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("admin/api/v1/playlists")
@RequiredArgsConstructor
class AdminPlaylistController {

    private final PlaylistService playlistService;
    private final PlaylistMapper mapper = PlaylistMapper.INSTANCE;

    @GetMapping
    public List<PlaylistRead> read(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return mapper.toListViewRead(
                playlistService.getPlaylists(pageNumber, pageSize, sortBy)
        );
    }

    @PostMapping("/discord")
    public List<PlaylistRead> readByDiscordIdentities(
            @RequestParam(defaultValue = "0") Integer pageNumber,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestBody Set<String> discordIdentities
    ) {
        return mapper.toListViewRead(
                playlistService.getPlaylistsByDiscordIds(pageNumber, pageSize, sortBy, discordIdentities)
        );
    }

    @PostMapping
    public PlaylistRead create(
            @RequestParam String userId,
            @RequestParam String discordIdentity,
            @RequestBody PlaylistCreate playlistCreate
    ) {
        if (userId == null) {
            throw new RuntimeException("Идентификатор пользователя должен быть заполнен!");
        }

        Playlist playlist = mapper.toEntity(playlistCreate);
        playlist.setUserId(userId);
        playlist.setDiscordIdentity(discordIdentity);

        return mapper.toViewRead(
                playlistService.createPlaylist(playlist)
        );
    }

    @GetMapping("/{id}")
    public PlaylistRead read(@PathVariable Long id) {
        return mapper.toViewRead(
                playlistService.getPlaylistById(id)
        );
    }

    @PutMapping("/{id}")
    public PlaylistRead update(
            @PathVariable Long id,
            @RequestParam String userId,
            @RequestParam String discordIdentity,
            @RequestBody PlaylistUpdate playlistUpdate
    ) {
        if (id == null) {
            throw new RuntimeException("Id сущности должен быть не null!");
        }
        if (!playlistService.playlistExistById(id)) {
            throw new RuntimeException("Не найдена сущность с заданным Id!");
        }
        Playlist playlist = mapper.toEntity(playlistUpdate);
        playlist.setId(id);
        playlist.setUserId(userId);
        playlist.setDiscordIdentity(discordIdentity);

        return mapper.toViewRead(
                playlistService.updatePlaylist(playlist)
        );
    }

    @DeleteMapping("/{id}")
    public PlaylistRead delete(@PathVariable Long id) {
        return mapper.toViewRead(
                playlistService.deletePlaylistById(id)
        );
    }
}
