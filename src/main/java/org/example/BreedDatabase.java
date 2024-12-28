package org.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//static dataBase class that I built my initial quiz logic on
public class BreedDatabase {
    public static List<DogBreed> dogBreeds() {
        List<DogBreed> breeds = new ArrayList<>();
        breeds.add(new DogBreed("French Bulldog", "Small", "Low", Arrays.asList("Loyal", "Friendly", "Low-Maintenance"), "https://frenchbulldogclub.org"));
        breeds.add(new DogBreed("Labrador Retriever", "Large", "High", Arrays.asList("Friendly", "Active", "Family-Oriented"), "https://thelabradorclub.com"));
        breeds.add(new DogBreed("Golden Retriever", "Large", "High", Arrays.asList("Friendly", "Intelligent", "Trainable"), "https://grca.org"));
        breeds.add(new DogBreed("Poodle", "Variable", "Moderate", Arrays.asList("Hypoallergenic", "Intelligent", "Adaptable"), "https://poodleclubofamerica.org"));
        breeds.add(new DogBreed("Dachshund", "Small", "Moderate", Arrays.asList("Loyal", "Energetic", "Good for Apartments"), "https://www.dachshundclubofamerica.org"));
        breeds.add(new DogBreed("German Shepherd", "Large", "High", Arrays.asList("Loyal", "Trainable", "Protective"), "https://www.gsdca.org"));
        return breeds;
    }
}
