package com.github.irgalin.reachablesettlements.controller;

import com.github.irgalin.reachablesettlements.service.ReachableSettlementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class ReachableSettlementsController {

    @Autowired
    private ReachableSettlementsService reachableSettlementsService;


    @GetMapping("/reachable/settlements")
    public Set<String> getListOfReachableSettlements(@RequestParam(value = "startingPoint") String startingPoint,
                                                     @RequestParam(value = "commuteTime") int commuteTime) {
        return reachableSettlementsService.getReachableSettlements(startingPoint, commuteTime);
    }
}
