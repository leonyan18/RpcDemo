package com.zust.yan.rpc.monitor.app.mapping;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.monitor.app.dto.NetConfigInfoDTO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author yan
 */
@Mapper(componentModel = "spring")
public interface NetConfigInfoMapping {
    NetConfigInfoDTO toDTO(NetConfigInfo netConfigInfo);

    List<NetConfigInfoDTO> toDTOs(List<NetConfigInfo> netConfigInfo);
}
