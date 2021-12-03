package com.github.aldebaranoro.apollonmusicresourceserver.api.track.service;

import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.entity.Playlist;
import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.entity.Track;
import com.github.aldebaranoro.apollonmusicresourceserver.api.track.repository.TrackRepository;
import com.github.aldebaranoro.apollonmusicresourceserver.exception.dto.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TrackService {

    private final TrackRepository trackRepository;
    private final int tracksMaxCount;

    public TrackService(TrackRepository trackRepository, @Value("${limit.track.max-count}") int tracksMaxCount) {
        this.trackRepository = trackRepository;
        this.tracksMaxCount = tracksMaxCount;
    }

    public List<Track> getPlaylistTracksByUserId(
            Integer pageNumber,
            Integer pageSize,
            String sortBy,
            Long playlistId,
            String userId
    ) {
        Pageable paging = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy));
        Page<Track> pagedResult = trackRepository.findPlaylistTracksByUserId(paging, playlistId, userId);
        return pagedResult.hasContent() ? pagedResult.getContent() : new ArrayList<>();
    }

    public Track createTrack(Track track) {
        checkTrackMaxCount(track.getPlaylist());
        return trackRepository.save(track);
    }

    public Track getTrackById(Long id) {
        return trackRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Не найден трек с заданным id!"));
    }

    public void deleteTrackById(Long id) {
        if (!trackExistById(id)) {
            throw new ResourceNotFoundException("Не найден трек с заданным Id!");
        }
        trackRepository.deleteById(id);
    }

    public Boolean trackExistById(Long id) {
        return trackRepository.existsById(id);
    }

    private void checkTrackMaxCount(Playlist playlist) {
        if (playlist.getTracks() != null && playlist.getTracks().size() > tracksMaxCount) {
            String message = String.format(
                    "Невозможно добавить трек в плейлист, максимальная ёмкость плейлиста равна %s",
                    tracksMaxCount
            );
            throw new InvalidParameterException(message);
        }
    }
}
