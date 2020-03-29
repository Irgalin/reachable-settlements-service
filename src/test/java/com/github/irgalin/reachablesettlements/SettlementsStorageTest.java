package com.github.irgalin.reachablesettlements;

import com.github.irgalin.reachablesettlements.entity.Settlement;
import com.github.irgalin.reachablesettlements.storage.SettlementsStorage;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.apache.log4j.Level.ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SettlementsStorageTest {

    private final Appender mockAppender = mock(Appender.class);

    @BeforeEach
    public void setUp() {
        SettlementsStorage.clearData();
    }

    @Test
    public void testReadDataFromJsonFileWithCorrectData() {
        SettlementsStorage.readDataFromJsonFile("classpath:correct-test-data-1.json");
        assertThat(SettlementsStorage.hasData()).isTrue();
        assertThat(SettlementsStorage.settlementsCount()).isEqualTo(3);
        assertThat(SettlementsStorage.getSettlementByName("city3"))
                .isNotNull()
                .isInstanceOf(Settlement.class);
        assertThat(SettlementsStorage.getSettlementByName("city3").getCommutes())
                .isNotNull()
                .hasSize(2);
    }

    @Test
    public void testReadDataFromJsonFileWithWrongData() {
        Logger logger = Logger.getLogger(SettlementsStorage.class);
        logger.addAppender(mockAppender);

        SettlementsStorage.readDataFromJsonFile("classpath:wrong-test-data-1.json");
        assertThat(SettlementsStorage.hasData()).isFalse();

        SettlementsStorage.readDataFromJsonFile("classpath:wrong-test-data-2.json");
        assertThat(SettlementsStorage.hasData()).isFalse();

        ArgumentCaptor<LoggingEvent> eventArgumentCaptor = ArgumentCaptor.forClass(LoggingEvent.class);
        verify(mockAppender, times(2)).doAppend(eventArgumentCaptor.capture());

        LoggingEvent firstLoggingEvent = eventArgumentCaptor.getAllValues().get(0);
        assertThat(firstLoggingEvent.getLevel()).isEqualTo(ERROR);
        assertThat(firstLoggingEvent.getMessage().toString()
                .contains("The data in JSON file doesn't match to the defined schema.")).isTrue();

        LoggingEvent secondLoggingEvent = eventArgumentCaptor.getAllValues().get(0);
        assertThat(secondLoggingEvent.getLevel()).isEqualTo(ERROR);
        assertThat(secondLoggingEvent.getMessage().toString()
                .contains("The data in JSON file doesn't match to the defined schema.")).isTrue();
    }

    @AfterAll
    public static void cleanUp() {
        SettlementsStorage.clearData();
    }

}
