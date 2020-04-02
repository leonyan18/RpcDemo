package com.zust.yan.rpc.monitor.app.controller;

import com.zust.yan.rpc.monitor.app.dto.RequestDataDTO;
import com.zust.yan.rpc.monitor.app.service.RequestDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller
@Slf4j
@RestController
public class RequestDataController {
    @Autowired
    RequestDataService requestDataService;

    @GetMapping
    public List<RequestDataDTO> getRequestData(@RequestParam int pageNo,@RequestParam int pageSize) {
        return requestDataService.pagingRequestData(pageNo, pageSize);
    }
}
