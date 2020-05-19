package com.zust.yan.rpc.monitor.app.mapping;

import com.zust.yan.rpc.monitor.app.dto.RequestDataDTO;
import com.zust.yan.rpc.monitor.app.entity.RequestData;
import com.zust.yan.rpc.net.base.Request;
import org.mapstruct.Mapper;

import java.util.Date;
import java.util.List;

/**
 * @author yan
 */
@Mapper(componentModel = "spring")
public interface RequestDataMapping {
    RequestDataDTO toDTO(Request requestData);

    default Date toDate(Long v) {
        if (v == null) {
            return null;
        }
        return new Date(v);
    }

    RequestDataDTO toDTO(RequestData requestData);

    List<RequestDataDTO> toDTOs(List<RequestData> requestData);

    RequestData toEntity(RequestDataDTO requestData);

    List<RequestData> toEntitys(List<RequestDataDTO> requestData);
}
