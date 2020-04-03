package com.github.irgalin.reachablesettlements;

import com.github.irgalin.reachablesettlements.entity.Settlement;
import com.github.irgalin.reachablesettlements.storage.SettlementsStorage;
import org.apache.log4j.Appender;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.apache.log4j.Level.ERROR;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SettlementsStorageTest {

    private final Appender mockAppender = mock(Appender.class);

    @Test
    public void testReadDataFromJsonFileWithCorrectData() {
        SettlementsStorage correctDataStorage1 = new SettlementsStorage("classpath:correct-test-data-1.json");
        assertThat(correctDataStorage1.hasData()).isTrue();
        assertThat(correctDataStorage1.settlementsCount()).isEqualTo(3);
        assertThat(correctDataStorage1.getSettlementByName("town3"))
                .isNotNull()
                .isInstanceOf(Settlement.class);
        assertThat(correctDataStorage1.getSettlementByName("town3").getCommutes())
                .isNotNull()
                .hasSize(2);

        SettlementsStorage correctDataStorage2 = new SettlementsStorage("classpath:correct-test-data-2.json");
        assertThat(correctDataStorage2.hasData()).isTrue();
        assertThat(correctDataStorage2.settlementsCount()).isEqualTo(2);
        assertThat(correctDataStorage2.getSettlementByName("town1"))
                .isNotNull()
                .isInstanceOf(Settlement.class);
    }

    @Test
    public void testReadDataFromJsonFileWithWrongData() {
        Logger logger = Logger.getLogger(SettlementsStorage.class);
        logger.addAppender(mockAppender);

        SettlementsStorage wrongDataStorage1 = new SettlementsStorage("classpath:wrong-test-data-1.json");
        assertThat(wrongDataStorage1.hasData()).isFalse();

        SettlementsStorage wrongDataStorage2 = new SettlementsStorage("classpath:wrong-test-data-2.json");
        assertThat(wrongDataStorage2.hasData()).isFalse();

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

}
