package com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view;

import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.view.TrackRead;
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

    private Set<TrackRead> tracks;
}
