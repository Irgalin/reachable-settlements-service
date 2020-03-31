package com.github.irgalin.reachablesettlements;


import com.github.irgalin.reachablesettlements.service.SettlementsService;
import com.github.irgalin.reachablesettlements.service.SettlementsServiceImpl;
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
public class SettlementsServiceTest {

    @TestConfiguration
    static class ReachableSettlementsServiceImplTestContextConfiguration {

        @Bean
        public SettlementsService settlementsService() {
            return new SettlementsServiceImpl();
        }
    }

    @Autowired
    private SettlementsService settlementsService;

    @Test
    public void testGetListOfReachableSettlementsWithCorrectInputParameters() {
        Set<String> result = settlementsService.getReachableSettlements("town1", 10);
        assertThat(result).isNotNull().isEmpty();

        Set<String> result2 = settlementsService.getReachableSettlements("town3", 15);
        assertThat(result2).isNotNull().isNotEmpty();
        assertThat(result2).containsExactlyInAnyOrder("town4");

        Set<String> result3 = settlementsService.getReachableSettlements("town4", 20);
        assertThat(result3).isNotNull().isNotEmpty();
        assertThat(result3).containsExactlyInAnyOrder("town2", "town3", "town5");

        Set<String> result4 = settlementsService.getReachableSettlements("town5", 999999999);
        assertThat(result4).isNotNull().isNotEmpty();
        assertThat(result4).containsExactlyInAnyOrder("town1", "town2", "town3", "town4");
    }

}
