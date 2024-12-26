package org.example;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

import java.util.List;
import java.util.Scanner;

public class DogQuiz {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        // Initialize DynamoDB client and service
        DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
                .region(Region.US_EAST_1) //makes sure im in the right region
                .build();
        BreedService breedService = new BreedService(dynamoDbClient);

        // Fetch breeds from DynamoDB
        List<DogBreed> breeds = breedService.fetchAllBreeds();


        System.out.println("Welcome to the Dog Quiz! Let's find out the best dog breed for your lifestyle!");

        System.out.println("Please enter your first name!");
        String nameofUser = scanner.nextLine();

        System.out.println("Let's start by finding out what state you are in? [Florida]");
        String stateOfUser = scanner.nextLine();

        System.out.println("What size dog do you prefer? (Small/Medium/Large): ");
        String sizePreference = scanner.nextLine();

        System.out.println("What energy level do you prefer? (Low/Moderate/High): ");
        String energyPreference = scanner.nextLine();

        System.out.print("Do you need a hypoallergenic dog? (Yes/No): ");
        String hypoallergenic = scanner.nextLine();

        DogBreed bestMatch = null;
        DogBreed partialMatch = null;

        for (DogBreed breed : breeds) {
            // Perfect match: All criteria match
            if (breed.getSize().equalsIgnoreCase(sizePreference) &&
                    breed.getEnergyLevel().equalsIgnoreCase(energyPreference)) {

                if (hypoallergenic.equalsIgnoreCase("Yes") &&
                        breed.getTraits().contains("Hypoallergenic")) {
                    bestMatch = breed; // Perfect match
                    break;
                }

                if (!hypoallergenic.equalsIgnoreCase("Yes")) {
                    bestMatch = breed; // Perfect match
                    break;
                }
            }

            // Partial match: Only size or energy matches
            if (partialMatch == null &&
                    (breed.getSize().equalsIgnoreCase(sizePreference) ||
                            breed.getEnergyLevel().equalsIgnoreCase(energyPreference))) {
                partialMatch = breed; // Save a fallback option
            }
        }

// Provide a result to the user
        if (bestMatch != null) {
            System.out.println("\n" + nameofUser + ", based on your preferences, the best dog breed for you is: ");
            System.out.println("Breed: " + bestMatch.getDogBreed());
            System.out.println("Size: " + bestMatch.getSize());
            System.out.println("Energy Level: " + bestMatch.getEnergyLevel());
            System.out.println("Traits: " + (bestMatch.getTraits() != null ? String.join(", ", bestMatch.getTraits()) : "No traits available"));
            System.out.println("Kennel Club Info: " + bestMatch.getKennelClubLink());
        } else if (partialMatch != null) {
            System.out.println("\n" + nameofUser + ", we couldn't find a perfect match, but here is a close match for you: ");
            System.out.println("Breed: " + partialMatch.getDogBreed());
            System.out.println("Size: " + partialMatch.getSize());
            System.out.println("Energy Level: " + partialMatch.getEnergyLevel());
            System.out.println("Traits: " + (partialMatch.getTraits() != null ? String.join(", ", partialMatch.getTraits()) : "No traits available"));
            System.out.println("Kennel Club Info: " + partialMatch.getKennelClubLink());
        } else {
            System.out.println("\nSorry, " + nameofUser + ", we couldn't find a breed that matches your preferences. \nPlease take the quiz again and watch out for any errors that might be present.");
        }
    }
}
