package io.github.lancelothuxi.mock.server.domain.mock.convert;


import io.github.lancelothuxi.mock.server.domain.mock.MockConfigEntity;
import io.github.lancelothuxi.mock.server.domain.mock.dto.MockConfigDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MockConfigConverter {

    MockConfigConverter INSTANCE = Mappers.getMapper( MockConfigConverter.class );

    MockConfigDTO toDto(MockConfigEntity car);


    List<MockConfigDTO> toDto(List<MockConfigEntity> cars);
}
