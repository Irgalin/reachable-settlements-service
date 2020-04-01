package com.github.irgalin.reachablesettlements.controller;

import com.github.irgalin.reachablesettlements.cache.ReachableSettlementsCache;
import com.github.irgalin.reachablesettlements.service.SettlementsService;
import com.github.irgalin.reachablesettlements.service.SettlementsServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Set;

@RestController
@Validated
@RequestMapping("/")
public class SettlementsController {

    @Autowired
    private SettlementsService settlementsService;


    @PostMapping("/reachable-settlements")
    public Set<String> getReachableSettlements(@Valid @RequestBody ReachableSettlementsRequest req) throws SettlementsServiceException {
        Set<String> result = ReachableSettlementsCache.getResultFromCache(req);
        if (result == null) {
            result = settlementsService.getReachableSettlements(req.getStartingPointName(), req.getCommuteTimeMin());
            ReachableSettlementsCache.putResultInCache(req, result);
        }
        return result;
    }

}
