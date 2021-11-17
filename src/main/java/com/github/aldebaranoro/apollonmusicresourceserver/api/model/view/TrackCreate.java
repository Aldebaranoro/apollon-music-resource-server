package com.github.aldebaranoro.apollonmusicresourceserver.api.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TrackCreate {

    private String url;

    private String label;

    private IdIntView musicService;
}
