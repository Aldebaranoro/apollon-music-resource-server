package com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.mapper;

import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.entity.Playlist;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view.PlaylistCreate;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view.PlaylistRead;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view.PlaylistReadById;
import com.github.aldebaranoro.apollonmusicresourceserver.api.playlist.model.view.PlaylistUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper
public interface PlaylistMapper {

    PlaylistMapper INSTANCE = Mappers.getMapper(PlaylistMapper.class);

    Playlist toEntity(PlaylistCreate view);

    Playlist toEntity(PlaylistUpdate view);

    PlaylistReadById toViewReadById(Playlist entity);

    PlaylistRead toViewRead(Playlist entity);

    List<PlaylistRead> toListViewRead(List<Playlist> entities);
}
