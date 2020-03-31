package com.github.irgalin.reachablesettlements.controller;

import com.github.irgalin.reachablesettlements.service.SettlementsService;
import com.github.irgalin.reachablesettlements.service.SettlementsServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@RestController
@Validated
@RequestMapping("/")
public class SettlementsController {

    @Autowired
    private SettlementsService settlementsService;


    @GetMapping("/reachable-settlements")
    public Set<String> getReachableSettlements(@RequestParam(value = "startingPointName") @NotBlank String startingPointName,
                                               @RequestParam(value = "commuteTimeMin") @Min(1) int commuteTimeMin) throws SettlementsServiceException {
        return settlementsService.getReachableSettlements(startingPointName, commuteTimeMin);

    }
}
