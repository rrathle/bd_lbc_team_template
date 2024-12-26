package org.example;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BreedService {

    private final DynamoDbClient dynamoDbClient;

    public BreedService(DynamoDbClient dynamoDbClient) {
        this.dynamoDbClient = dynamoDbClient;
    }

    public List<DogBreed> fetchAllBreeds() {
        List<DogBreed> breeds = new ArrayList<>();

        // Create a ScanRequest for the DogBreeds table
        ScanRequest request = ScanRequest.builder()
                .tableName("DogBreeds")
                .build();

        // Execute the scan and process results
        ScanResponse response = dynamoDbClient.scan(request);

        for (Map<String, software.amazon.awssdk.services.dynamodb.model.AttributeValue> item : response.items()) {
            String dogBreed = item.get("dogBreed").s();
            String size = item.get("size").s();
            String energy = item.get("energy").s();

            List<String> traits = item.get("traits") != null && item.get("traits").hasSs()
                    ? item.get("traits").ss()
                    : new ArrayList<>();


            String kennelClubLink = item.get("kennel_club_link").s();

            breeds.add(new DogBreed(dogBreed, size, energy, traits, kennelClubLink));
        }

        return breeds;
    }
}
