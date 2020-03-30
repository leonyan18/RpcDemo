package com.zust.yan.rpc.monitor.app.mapping;

import com.zust.yan.rpc.monitor.app.dto.RequestDataDTO;
import com.zust.yan.rpc.monitor.app.entity.RequestData;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestDataMapping {
    RequestDataDTO toDTO(RequestData requestData);

    List<RequestDataDTO> toDTOs(List<RequestData> requestData);
}
