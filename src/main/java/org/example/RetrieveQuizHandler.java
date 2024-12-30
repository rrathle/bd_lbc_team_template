package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import java.util.HashMap;
import java.util.Map;

public class  RetrieveQuizHandler implements RequestHandler<Map<String, String>, Map<String, Object>> {

    private final DynamoDbClient dynamoDbClient;

    public RetrieveQuizHandler() {
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(software.amazon.awssdk.regions.Region.US_EAST_1)
                .build();
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, String> input, Context context) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate input: Check if quizId is present
            String quizId = input.get("quizId");
            if (quizId == null || quizId.isEmpty()) {
                response.put("statusCode", 400);
                response.put("error", "Missing or invalid quizId parameter.");
                return response;
            }

            // Retrieve quiz results from DynamoDB
            GetItemRequest getItemRequest = GetItemRequest.builder()
                    .tableName("DogQuizResults") // DynamoDB table name
                    .key(Map.of("quizId", AttributeValue.builder().s(quizId).build()))
                    .build();

            GetItemResponse getItemResponse = dynamoDbClient.getItem(getItemRequest);

            // Check if item exists
            if (getItemResponse.hasItem()) {
                Map<String, AttributeValue> item = getItemResponse.item();

                // Build the quiz result response
                response.put("statusCode", 200);
                response.put("quizResults", Map.of(
                        "quizId", quizId,
                        "sizePreference", item.get("sizePreference").s(),
                        "energyPreference", item.get("energyPreference").s(),
                        "hypoallergenic", item.get("hypoallergenic").s(),
                        "dogBreed", item.get("dogBreed").s(),
                        "kennelClubLink", item.get("kennelClubLink").s(),
                        "timestamp", item.get("timestamp").s()
                ));
            } else {
                // No item found for the given quizId
                response.put("statusCode", 404);
                response.put("error", "No quiz results found for the given quizId.");
            }
        } catch (Exception e) {
            context.getLogger().log("Error occurred: " + e.getMessage());
            response.put("statusCode", 500);
            response.put("error", "Internal Server Error.");
        }

        return response;
    }
}
