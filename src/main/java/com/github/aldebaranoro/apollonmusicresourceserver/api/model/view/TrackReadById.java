package com.github.aldebaranoro.apollonmusicresourceserver.api.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrackReadById {

    private Long id;

    private String url;

    private String label;

    private MusicServiceSourceRead musicService;
}
