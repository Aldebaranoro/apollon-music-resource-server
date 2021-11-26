package com.github.aldebaranoro.apollonmusicresourceserver.api.dict.repository;

import com.github.aldebaranoro.apollonmusicresourceserver.api.dict.model.entity.MusicServiceSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicServiceSourceRepository extends JpaRepository<MusicServiceSource, Integer> {
}