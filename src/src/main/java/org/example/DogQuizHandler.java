package org.example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;

import java.util.HashMap;
import java.util.Map;

public class DogQuizHandler implements RequestHandler<Map<String, String>, Map<String, Object>> {

    private final DynamoDbClient dynamoDbClient;

    // Constructor initializes DynamoDbClient
    public DogQuizHandler() {
        this.dynamoDbClient = DynamoDbClient.builder()
                .region(software.amazon.awssdk.regions.Region.US_EAST_1) // Replace with your AWS region
                .build();
    }

    @Override
    public Map<String, Object> handleRequest(Map<String, String> input, Context context) {
        Map<String, Object> response = new HashMap<>();

        try {
            // Extract parameters from input
            String sizePreference = input.get("sizePreference");
            String energyPreference = input.get("energyPreference");
            String hypoallergenic = input.get("hypoallergenic");

            // DynamoDB logic to fetch the best dog breed
            GetItemRequest request = GetItemRequest.builder()
                    .tableName("DogBreeds")
                    .key(Map.of("size", AttributeValue.builder().s(sizePreference).build()))
                    .build();

            GetItemResponse dbResponse = dynamoDbClient.getItem(request);

            if (dbResponse.hasItem()) {
                Map<String, software.amazon.awssdk.services.dynamodb.model.AttributeValue> item = dbResponse.item();
                response.put("dogBreed", item.get("dogBreed").s());
                response.put("kennelClubLink", item.get("kennel_club_link").s());
            } else {
                response.put("error", "No matching breed found.");
            }
        } catch (Exception e) {
            context.getLogger().log("Error occurred: " + e.getMessage());
            response.put("error", "Internal Server Error");
        }

        return response;
    }
}
