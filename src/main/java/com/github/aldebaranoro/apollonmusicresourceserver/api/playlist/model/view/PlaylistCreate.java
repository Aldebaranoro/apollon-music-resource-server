package com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class PlaylistCreate {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Boolean isPrivate;
}
