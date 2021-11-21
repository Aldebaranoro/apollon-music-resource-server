package com.github.aldebaranoro.apollonmusicresourceserver.api.controller;

import com.github.aldebaranoro.apollonmusicresourceserver.api.model.entity.MusicServiceSource;
import com.github.aldebaranoro.apollonmusicresourceserver.api.repository.MusicServiceSourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("public/api/v1/music-services")
@RequiredArgsConstructor
public class MusicServiceSourceController {

    private final MusicServiceSourceRepository musicServiceSourceRepository;

    @GetMapping
    private List<MusicServiceSource> findAll() {
        return musicServiceSourceRepository.findAll();
    }
}
