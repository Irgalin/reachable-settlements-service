package com.github.irgalin.reachablesettlements;


import com.github.irgalin.reachablesettlements.service.ReachableSettlementsService;
import com.github.irgalin.reachablesettlements.service.ReachableSettlementsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@TestPropertySource(locations = "/test-application.properties")
public class ReachableSettlementsServiceTest {

    @TestConfiguration
    static class ReachableSettlementsServiceImplTestContextConfiguration {

        @Bean
        public ReachableSettlementsService employeeService() {
            return new ReachableSettlementsServiceImpl();
        }
    }

    @Autowired
    private ReachableSettlementsService reachableSettlementsService;

    @Test
    public void testGetListOfReachableSettlementsWithCorrectInputParameters() {
        Set<String> result = reachableSettlementsService.getListOfReachableSettlements("city1", 10);
        assertThat(result).isNotNull().isEmpty();

        Set<String> result2 = reachableSettlementsService.getListOfReachableSettlements("city3", 15);
        assertThat(result2).isNotNull().isNotEmpty();
        assertThat(result2).containsExactlyInAnyOrder("city4");

        Set<String> result3 = reachableSettlementsService.getListOfReachableSettlements("city4", 20);
        assertThat(result3).isNotNull().isNotEmpty();
        assertThat(result3).containsExactlyInAnyOrder("city2", "city3", "city5");

        Set<String> result4 = reachableSettlementsService.getListOfReachableSettlements("city5", 999999999);
        assertThat(result4).isNotNull().isNotEmpty();
        assertThat(result4).containsExactlyInAnyOrder("city1", "city2", "city3", "city4");
    }

}
