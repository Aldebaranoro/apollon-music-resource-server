package com.github.aldebaranoro.apollonmusicresourceserver.api.model.mapper;

import com.github.aldebaranoro.apollonmusicresourceserver.api.model.entity.Playlist;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.view.PlaylistCreate;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.view.PlaylistRead;
import com.github.aldebaranoro.apollonmusicresourceserver.api.model.view.PlaylistUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper
public interface PlaylistMapper {

    PlaylistMapper INSTANCE = Mappers.getMapper(PlaylistMapper.class);

    Playlist toEntity(PlaylistCreate view);

    Playlist toEntity(PlaylistUpdate view);

    PlaylistRead toViewRead(Playlist entity);

    List<PlaylistRead> toListViewRead(List<Playlist> entities);
}
