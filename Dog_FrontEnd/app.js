// app.js

// Base URL of your API Gateway
const BASE_URL = "https://a3cha3mfzd.execute-api.us-east-1.amazonaws.com/dev";

// DOM Elements POST
const quizForm = document.getElementById("quiz-form");
const resultDiv = document.getElementById("result");
const dogBreedEl = document.getElementById("dog-breed");
const kennelClubLinkEl = document.getElementById("kennel-club-link");
const quizIdEl = document.getElementById("quiz-id");
// DOM Elements for Get
const lookupForm = document.getElementById("lookup-form");
const lookupQuizIdInput = document.getElementById("lookupQuizId");
const lookupResultDiv = document.getElementById("lookup-result");
const lookupDogBreedEl = document.getElementById("lookup-dog-breed");
const lookupKennelLinkEl = document.getElementById("lookup-kennel-link");
const copyIdBtn = document.getElementById("copy-id-btn");

// Copy Quiz ID to Clipboard
copyIdBtn.addEventListener("click", () => {
    const quizId = document.getElementById("quiz-id").textContent;
    navigator.clipboard.writeText(quizId).then(() => {
        alert("Quiz ID copied to clipboard!");
    }).catch(err => {
        console.error("Could not copy text: ", err);
    });
});

// Look Up Quiz Results
lookupForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    const quizId = lookupQuizIdInput.value.trim();
    if (!quizId) {
        alert("Please enter a valid Quiz ID.");
        return;
    }

    try {
        // Make GET request to API
        const response = await axios.get(`${BASE_URL}/get-quiz/${quizId}`);
        const { dogBreed, kennelClubLink } = response.data.quizResults;

        // Update the UI with the lookup result
        lookupDogBreedEl.textContent = dogBreed;
        lookupKennelLinkEl.textContent = "Learn more about this breed";
        lookupKennelLinkEl.href = kennelClubLink;

        lookupResultDiv.classList.remove("hidden");
    } catch (error) {
        console.error("Error looking up quiz results:", error);
        alert("Could not retrieve results. Please check your Quiz ID.");
    }
});



// Form Submission Event
quizForm.addEventListener("submit", async (event) => {
    event.preventDefault();

    // Get user inputs
    const sizePreference = document.getElementById("sizePreference").value;
    const energyPreference = document.getElementById("energyPreference").value;
    const hypoallergenic = document.getElementById("hypoallergenic").value;

try {
        // Make POST request to your API
        const response = await axios.post(
            `${BASE_URL}/quiz/recommend/${sizePreference}/${energyPreference}/${hypoallergenic}`
        );

        // Extract data
        const { quizId, dogBreed, kennelClubLink } = response.data;

        // Update the UI
        dogBreedEl.textContent = dogBreed;
        kennelClubLinkEl.textContent = "Learn more about this breed";
        kennelClubLinkEl.href = kennelClubLink;

        // Display Quiz ID
        quizIdEl.textContent = quizId;

        resultDiv.classList.remove("hidden");
    } catch (error) {
        console.error("Error fetching recommendation:", error);
        alert("Something went wrong! Please try again.");
    }
});