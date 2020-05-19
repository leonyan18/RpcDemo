package com.zust.yan.rpc.monitor.app.controller;

import com.zust.yan.rpc.monitor.app.dto.ServiceData;
import com.zust.yan.rpc.monitor.app.service.ServiceDataService;
import com.zust.yan.rpc.monitor.app.utils.PageInfo;
import com.zust.yan.rpc.monitor.app.utils.Paging;
import com.zust.yan.rpc.monitor.app.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/service")
public class ServiceDataController {
    private final ServiceDataService serviceDataService;

    public ServiceDataController(ServiceDataService serviceDataService) {
        this.serviceDataService = serviceDataService;
    }

    @GetMapping("")
    public Response<Paging<ServiceData>> getRequestData(@RequestParam(required = false) Integer pageNo,
                                                        @RequestParam(required = false) Integer pageSize,
                                                        @RequestParam(required = false) String serviceName) {
        try {
            PageInfo pageInfo = new PageInfo(pageSize, pageNo);
            List<ServiceData> serviceDataList = serviceDataService.getAllServiceData();
            // 筛选服务名称
            if (!StringUtils.isEmpty(serviceName)) {
                serviceDataList = serviceDataList.stream()
                        .filter(serviceData -> serviceData.getServiceName().contains(serviceName))
                        .collect(Collectors.toList());
            }
            int pos = pageInfo.getIndex() + pageInfo.getPageSize();
            if (serviceDataList.size() <= pos) {
                pos = serviceDataList.size();
            }
            serviceDataList = serviceDataList.subList(pageInfo.getIndex(), pos);
            Paging<ServiceData> requestDataDTOPaging = new Paging<>(serviceDataList.size(), serviceDataList);
            return Response.yes(requestDataDTOPaging);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return Response.no(e.getMessage());
        }
    }
}