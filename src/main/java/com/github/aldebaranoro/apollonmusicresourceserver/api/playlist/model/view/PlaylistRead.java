package com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlaylistRead {

    private Long id;

    private String name;

    private String description;

    private String userId;

    private String discordIdentity;

    private Boolean isPrivate;

    private Integer tracksCount;
}
