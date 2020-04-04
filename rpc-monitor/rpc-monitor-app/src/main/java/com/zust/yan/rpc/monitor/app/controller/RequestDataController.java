package com.zust.yan.rpc.monitor.app.controller;

import com.zust.yan.rpc.monitor.app.dto.RequestDataDTO;
import com.zust.yan.rpc.monitor.app.dto.RpcData;
import com.zust.yan.rpc.monitor.app.dto.RpcPath;
import com.zust.yan.rpc.monitor.app.service.CallPathService;
import com.zust.yan.rpc.monitor.app.service.RequestDataService;
import com.zust.yan.rpc.monitor.app.utils.Paging;
import com.zust.yan.rpc.monitor.app.utils.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/request")
public class RequestDataController {
    @Autowired
    RequestDataService requestDataService;

    @Autowired
    CallPathService callPathService;

    @GetMapping("")
    public Response<Paging<RequestDataDTO>> getRequestData(@RequestParam(required = false) int pageNo,
                                                           @RequestParam(required = false) int pageSize,
                                                           @RequestParam(required = false) String clazz,
                                                           @RequestParam(required = false) String method,
                                                           @RequestParam(required = false) String fromAddress,
                                                           @RequestParam(required = false) String toAddress,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") Date startDate,
                                                           @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'") Date endDate) {
        Map<String, Object> query = new HashMap<>();
        try {
            if (clazz != null) {
                query.put("clazz", '%' + clazz + '%');
            }
            if (method != null) {
                query.put("method", '%' + method + '%');
            }
            if (fromAddress != null) {
                query.put("fromAddress", '%' + fromAddress + '%');
            }
            if (toAddress != null) {
                query.put("toAddress", '%' + toAddress + '%');
            }
            if (startDate != null && endDate != null) {
                query.put("startDate", startDate);
                query.put("endDate", endDate);
            }
            Paging<RequestDataDTO> requestDataDTOPaging = requestDataService.pagingRequestData(pageNo, pageSize, query);
            return Response.yes(requestDataDTOPaging);
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return Response.no(e.getMessage());
        }
    }

    @GetMapping("/spend")
    public Response<List<RpcData>> getTopSpendTime(@RequestParam int type) {
        try {
            if (type == 0) {
                return Response.yes(requestDataService.getTopSpendTimeIp());
            } else {
                return Response.yes(requestDataService.getTopSpendTimeMethod());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return Response.no(e.getMessage());
        }
    }

    @GetMapping("/call")
    public Response<List<RpcData>> getTopCallTime(@RequestParam int type) {
        try {
            if (type == 0) {
                return Response.yes(requestDataService.getTopCallTimeIp());
            } else {
                return Response.yes(requestDataService.getTopCallTimeMethod());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return Response.no(e.getMessage());
        }
    }

    @GetMapping("/path")
    public Response<RpcPath> getCallPath(@RequestParam Long requestId) {
        try {
            return Response.yes(callPathService.getCallPath(requestId));
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return Response.no(e.getMessage());
        }
    }
}
