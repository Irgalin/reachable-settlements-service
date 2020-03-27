package com.github.irgalin.reachablesettlements.controller;

import com.github.irgalin.reachablesettlements.entity.Settlement;
import com.github.irgalin.reachablesettlements.service.ReachableSettlementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ReachableSettlementsController {

    @Autowired
    private ReachableSettlementsService reachableSettlementsService;


    @GetMapping("/reachablesettlements")
    public List<Settlement> getListOfReachableSettlements(@RequestParam(value = "townName") String startingPoint, @RequestParam(value = "commuteTime") int commuteTime) {
        return reachableSettlementsService.getListOfReachableSettlements(startingPoint, commuteTime);
    }
}
