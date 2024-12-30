package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DogQuizHandler implements RequestHandler<Map<String, String>, Map<String, Object>> {

    private final DynamoDbClient dynamoDbClient;

    public DogQuizHandler() {
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(software.amazon.awssdk.regions.Region.US_EAST_1)
                .build();
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, String> input, Context context) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate input
            String sizePreference = input.get("sizePreference");
            String energyPreference = input.get("energyPreference");
            String hypoallergenic = input.get("hypoallergenic");

            if (sizePreference == null || energyPreference == null || hypoallergenic == null) {
                response.put("statusCode", 400);
                response.put("error", "Invalid input: Missing required parameters.");
                return response;
            }

            // Generate a unique quizId
            String quizId = UUID.randomUUID().toString();

            // Determine the best and partial matches
            DogBreed bestMatch = determineBestMatch(sizePreference, energyPreference, hypoallergenic);
            DogBreed partialMatch = determinePartialMatch(sizePreference, energyPreference, hypoallergenic);

            // If no perfect match is found, fall back to the partial match
            String dogBreed = (bestMatch != null) ? bestMatch.getDogBreed() :
                    (partialMatch != null ? partialMatch.getDogBreed() : "No match found");
            String kennelClubLink = (bestMatch != null) ? bestMatch.getKennelClubLink() :
                    (partialMatch != null ? partialMatch.getKennelClubLink() : "N/A");

            // Save the quiz results to DynamoDB
            saveQuizResultsToDynamoDB(quizId, sizePreference, energyPreference, hypoallergenic, dogBreed, kennelClubLink);

            // Prepare the response
            response.put("statusCode", 200);
            response.put("quizId", quizId);
            response.put("dogBreed", dogBreed);
            response.put("kennelClubLink", kennelClubLink);
            response.put("matchType", (bestMatch != null) ? "Perfect Match" :
                    (partialMatch != null ? "Partial Match" : "No Match"));

        } catch (Exception e) {
            context.getLogger().log("Error occurred: " + e.getMessage());
            for (StackTraceElement element : e.getStackTrace()) {
                context.getLogger().log(element.toString());
            }
            response.put("statusCode", 500);
            response.put("error", "Internal Server Error");
        }

        return response;
    }

    private DogBreed determineBestMatch(String sizePreference, String energyPreference, String hypoallergenic) {
        // Scan the table for matching items
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName("DogBreeds")
                .filterExpression("size = :size AND energy = :energy AND hypoallergenic = :hypoallergenic")
                .expressionAttributeValues(Map.of(
                        ":size", AttributeValue.builder().s(sizePreference).build(),
                        ":energy", AttributeValue.builder().s(energyPreference).build(),
                        ":hypoallergenic", AttributeValue.builder().s(hypoallergenic).build()
                ))
                .build();

        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

        if (scanResponse.count() > 0) {
            Map<String, AttributeValue> item = scanResponse.items().get(0);
            return mapToDogBreed(item);
        }
        return null;
    }



    private DogBreed determinePartialMatch(String sizePreference, String energyPreference, String hypoallergenic) {
        ScanRequest scanRequest = ScanRequest.builder()
                .tableName("DogBreeds")
                .filterExpression("size = :size OR energy = :energy")
                .expressionAttributeValues(Map.of(
                        ":size", AttributeValue.builder().s(sizePreference).build(),
                        ":energy", AttributeValue.builder().s(energyPreference).build()
                ))
                .build();

        ScanResponse scanResponse = dynamoDbClient.scan(scanRequest);

        if (scanResponse.count() > 0) {
            Map<String, AttributeValue> item = scanResponse.items().get(0);
            return mapToDogBreed(item);
        }
        return null;
    }


    private DogBreed mapToDogBreed(Map<String, AttributeValue> item) {
        if (item == null || item.isEmpty()) return null;

        List<String> traits = item.containsKey("traits") && item.get("traits").ss() != null
                ? item.get("traits").ss()
                : List.of();

        return new DogBreed(
                item.get("dogBreed").s(),
                item.get("size").s(),
                item.get("energy").s(),
                traits,
                item.get("kennel_club_link").s()
        );
    }

    private void saveQuizResultsToDynamoDB(String quizId, String sizePreference, String energyPreference, String hypoallergenic, String dogBreed, String kennelClubLink) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("quizId", AttributeValue.builder().s(quizId).build());
        item.put("sizePreference", AttributeValue.builder().s(sizePreference).build());
        item.put("energyPreference", AttributeValue.builder().s(energyPreference).build());
        item.put("hypoallergenic", AttributeValue.builder().s(hypoallergenic).build());
        item.put("dogBreed", AttributeValue.builder().s(dogBreed).build());
        item.put("kennelClubLink", AttributeValue.builder().s(kennelClubLink).build());
        item.put("timestamp", AttributeValue.builder().s(String.valueOf(System.currentTimeMillis())).build());

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName("DogQuizResults")
                .item(item)
                .build();

        dynamoDbClient.putItem(putItemRequest);
    }
}

