package com.github.aldebaranoro.apollonmusicresourceserver.api.model.view;

import java.util.Set;

public class PlaylistRead {

    private Long id;

    private String name;

    private String description;

    private String userId;

    private String discordIdentity;

    private boolean isPrivate;

    private Set<TrackRead> tracks;
}
