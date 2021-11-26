package com.github.aldebaranoro.apollonmusicresourceserver.api.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class PlaylistReadById {

    private Long id;

    private String name;

    private String description;

    private String userId;

    private String discordIdentity;

    private Boolean isPrivate;

    private Set<TrackReadById> tracks;
}
