package com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.view;

import com.github.aldebaranoro.apollonmusicresourceserver.api.dict.model.view.MusicServiceSourceRead;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class TrackUpdate {

    @URL
    @NotBlank
    private String url;

    @NotBlank
    private String label;

    @Valid
    @NotNull
    private MusicServiceSourceRead musicService;
}
