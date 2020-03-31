package com.github.irgalin.reachablesettlements;

import com.github.irgalin.reachablesettlements.controller.SettlementsController;
import com.github.irgalin.reachablesettlements.service.SettlementsService;
import com.github.irgalin.reachablesettlements.service.SettlementsServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = SettlementsController.class)
public class SettlementsControllerTest {

    @MockBean
    private SettlementsService settlementsService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetReachableSettlementsWithCorrectParameters() throws Exception {
        Set<String> serviceResult = new HashSet<>(Arrays.asList("town2", "town3"));
        given(settlementsService.getReachableSettlements("town1", 40)).willReturn(serviceResult);
        mockMvc.perform(get("/reachable-settlements")
                .param("startingPointName", "town1")
                .param("commuteTimeMin", "40")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0]", is("town2")))
                .andExpect(jsonPath("$[1]", is("town3")));
    }

    @Test
    public void testGetReachableSettlementsWhenServiceThrowsException() throws Exception {
        given(settlementsService.getReachableSettlements("town2", 30))
                .willThrow(new SettlementsServiceException("exception message"));
        mockMvc.perform(get("/reachable-settlements")
                .param("startingPointName", "town2")
                .param("commuteTimeMin", "30")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("exception message")));
    }
}
