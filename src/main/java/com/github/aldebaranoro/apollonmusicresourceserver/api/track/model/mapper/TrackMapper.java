package com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.mapper;

import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.entity.Track;
import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.view.TrackCreate;
import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.view.TrackRead;
import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.view.TrackReadById;
import com.github.aldebaranoro.apollonmusicresourceserver.api.track.model.view.TrackUpdate;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import java.util.List;

@Mapper
public interface TrackMapper {

    TrackMapper INSTANCE = Mappers.getMapper(TrackMapper.class);

    Track toEntity(TrackCreate view);

    Track toEntity(TrackUpdate view);

    TrackReadById toViewReadById(Track entity);

    TrackRead toViewRead(Track entity);

    List<TrackRead> toListViewRead(List<Track> entities);
}
