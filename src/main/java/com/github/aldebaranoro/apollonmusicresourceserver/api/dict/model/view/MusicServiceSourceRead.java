package com.github.aldebaranoro.apollonmusicresourceserver.api.dict.model.view;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class MusicServiceSourceRead {

    @NotNull
    private Integer id;

    private String name;
}
