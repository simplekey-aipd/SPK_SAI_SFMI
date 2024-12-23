package com.simplekey.spk_sai_sfmi.api.sfmi.controller;

import com.simplekey.spk_sai_sfmi.api.sfmi.service.SfmiService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sfmi/v1")
@RequiredArgsConstructor
public class SfmiController {

    private final SfmiService sfmiService;

    @PostMapping("/fetchData")
    public String fetchData(HttpServletRequest request) {
        return sfmiService.fetchData(request);
    }

    @PostMapping("/fetchData2/{sid}")
    public String fetchData2(@PathVariable(name = "sid") String sid) {
        return sfmiService.fetchData2(sid);
    }
}
