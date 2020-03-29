package com.github.irgalin.reachablesettlements.storage;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.irgalin.reachablesettlements.entity.Settlement;
import org.apache.log4j.Logger;
import org.springframework.util.ResourceUtils;

import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SettlementsStorage {

    private static final Logger LOGGER = Logger.getLogger(SettlementsStorage.class);

    private static Map<String, Settlement> nameSettlementMap = new ConcurrentHashMap<>();

    public synchronized static void readDataFromJsonFile(@NotNull String jsonDataFilePath) {
        try {
            File jsonDataFile = ResourceUtils.getFile(jsonDataFilePath);
            if (jsonDataFile.isFile()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(jsonDataFile);
                if (isJsonDataValid(jsonNode)) {
                    Settlement[] settlements = mapper.readValue(jsonNode.toString(), Settlement[].class);
                    for (Settlement settlement : settlements) {
                        nameSettlementMap.put(settlement.getName(), settlement);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to read data from JSON file!", e);
        }
    }

    private static boolean isJsonDataValid(@NotNull JsonNode jsonNode) throws IOException, ProcessingException {
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        JsonSchema schema = factory.getJsonSchema(JsonLoader.fromFile(ResourceUtils.getFile("classpath:data-schema.json")));
        ProcessingReport report = schema.validate(jsonNode);
        if (!report.isSuccess()) {
            LOGGER.error("The data in JSON file doesn't match to the defined schema. Report:\n" + report);
            return false;
        }
        return true;
    }

    public static Settlement getSettlementByName(@NotNull String settlementName) {
        return nameSettlementMap.get(settlementName);
    }

    public static int settlementsCount() {
        return nameSettlementMap.size();
    }

    public static boolean hasData() {
        return !nameSettlementMap.isEmpty();
    }

    public static void clearData() {
        nameSettlementMap.clear();
    }


}