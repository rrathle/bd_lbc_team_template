package org.example;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

import java.util.List;
@DynamoDbBean
public class DogBreed {
    private String dogBreed; // Partition Key
    private String size;
    private String energyLevel;
    private List<String> traits;
    private String kennelClubLink;

    // Default constructor (required for DynamoDB Enhanced Client)
    public DogBreed() {}

    // Parameterized constructor
    public DogBreed(String dogBreed, String size, String energyLevel, List<String> traits, String kennelClubLink) {
        this.dogBreed = dogBreed;
        this.size = size;
        this.energyLevel = energyLevel;
        this.traits = traits;
        this.kennelClubLink = kennelClubLink;
    }
    @DynamoDbPartitionKey
    public String getDogBreed() {
        return dogBreed;
    }

    public void setDogBreed(String dogBreed) {
        this.dogBreed = dogBreed;
    }

    @DynamoDbAttribute("size")
    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @DynamoDbAttribute("energy")
    public String getEnergyLevel() {
        return energyLevel;
    }

    public void setEnergyLevel(String energyLevel) {
        this.energyLevel = energyLevel;
    }

    @DynamoDbAttribute("traits")
    public List<String> getTraits() {
        return traits;
    }

    public void setTraits(List<String> traits) {
        this.traits = traits;
    }

    @DynamoDbAttribute("kennel_club_link")
    public String getKennelClubLink() {
        return kennelClubLink;
    }

    public void setKennelClubLink(String kennelClubLink) {
        this.kennelClubLink = kennelClubLink;

}

    @Override
    public String toString() {
        return "DogBreed{" +
                "dogBreed='" + dogBreed + '\'' +
                ", size='" + size + '\'' +
                ", energyLevel='" + energyLevel + '\'' +
                ", traits=" + traits +
                '}';
    }
}

