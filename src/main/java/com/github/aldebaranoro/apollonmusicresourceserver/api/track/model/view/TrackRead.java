package com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.view;

import com.github.aldebaranoro.apollonmusicresourceserver.api.dict.model.view.MusicServiceSourceRead;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrackRead {

    private Long id;

    private String url;

    private String label;

    private MusicServiceSourceRead musicService;
}
