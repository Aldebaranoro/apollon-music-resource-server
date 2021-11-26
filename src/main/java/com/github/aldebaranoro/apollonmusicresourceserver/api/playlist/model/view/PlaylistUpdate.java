package com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaylistUpdate {

    private String name;

    private String description;

    private Boolean isPrivate;
}
