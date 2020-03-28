package com.github.irgalin.reachablesettlements;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "/test-application.properties")
class ReachableSettlementsApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	void get() {
	}

}
