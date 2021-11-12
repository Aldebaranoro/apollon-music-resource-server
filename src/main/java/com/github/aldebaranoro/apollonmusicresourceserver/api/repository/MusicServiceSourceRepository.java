package com.github.aldebaranoro.apollonmusicresourceserver.api.repository;

import com.github.aldebaranoro.apollonmusicresourceserver.api.model.MusicServiceSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicServiceSourceRepository extends JpaRepository<MusicServiceSource, Integer> {
}