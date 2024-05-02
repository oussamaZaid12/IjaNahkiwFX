package test;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.regex.Pattern;

public class ChatbotServer {

    private static final Map<String, Map<List<String>, List<String>>> knowledgeBase = createKnowledgeBase();


    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("Server is running...");
            while (true) {
                try (Socket clientSocket = serverSocket.accept();
                     PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                     BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        System.out.println("Client: " + inputLine);
                        String response = processInput(inputLine); // Your chatbot logic
                        out.println(response);
                    }
                } catch (IOException e) {
                    System.out.println("Error handling client connection: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error starting the server: " + e.getMessage());
        }
    }
    private static String lastTopic = "";

    private static String TESTprocessInput(String input) {
        // Example simple chatbot logic
        if (input.equalsIgnoreCase("hello")) {
            lastTopic = "greetings";
            return "Hi there!";
        } else if (input.equalsIgnoreCase("how are you?") && "greetings".equals(lastTopic)) {
            return "I'm still doing well, thanks!";
        } else if (input.equalsIgnoreCase("how are you?")) {
            return "I'm doing well, thank you!";
        } else {
            lastTopic = ""; // reset topic
            return "I'm just a simple chatbot. Ask me anything!";
        }
    }
    private static String TESTtttprocessInput(String input) {
        for (Map.Entry<String, Map<List<String>, List<String>>> category : knowledgeBase.entrySet()) {
            for (Map.Entry<List<String>, List<String>> patternEntry : category.getValue().entrySet()) {
                for (String pattern : patternEntry.getKey()) {
                    if (Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(input).find()) {
                    //if (patternEntry.getKey().contains(input)) {
                        List<String> responses = patternEntry.getValue();
                        return responses.get(new Random().nextInt(responses.size())); // Return a random response
                    }
                }
            }
        }
        return "I'm just a simple chatbot. Ask me anything!";
    }
    private static String processInput(String input) {
        // Normalize the input for matching.
        input = input.trim().toLowerCase();

        // Iterate over each category in the knowledge base.
        for (Map.Entry<String, Map<List<String>, List<String>>> category : knowledgeBase.entrySet()) {
            // Debug output
            System.out.println("Checking category: " + category.getKey());

            // Iterate over each pattern in the category.
            for (Map.Entry<List<String>, List<String>> patternEntry : category.getValue().entrySet()) {
                // Check if any pattern is present in the user's input.
                for (String pattern : patternEntry.getKey()) {
                    // Debug output
                    System.out.println("Checking pattern: " + pattern);

                    // Check for presence of the pattern in input.
                    if (input.contains(pattern.toLowerCase())) {
                        // If a pattern is matched, select a random response.
                        List<String> responses = patternEntry.getValue();
                        return responses.get(new Random().nextInt(responses.size()));
                    }
                }
            }
        }

        // If no pattern was matched, return the default response.
        return "I'm just a simple chatbot. Ask me anything!";
    }

    private static Map<String, Map<List<String>, List<String>>> createKnowledgeBase() {
        Map<String, Map<List<String>, List<String>>> knowledgeBase = new HashMap<>();

        // Greetings
        Map<List<String>, List<String>> greetings = new HashMap<>();
        greetings.put(
                Arrays.asList("hi", "hey", "hello"),
                Arrays.asList("Hello there. How are you feeling today?", "Hi there. What brings you here today?")
        );
        knowledgeBase.put("greeting", greetings);

        Map<List<String>, List<String>> happyPatterns = new HashMap<>();
        happyPatterns.put(
                Arrays.asList("i feel great today", "I feel happy", "I'm good"),
                Arrays.asList("That's great to hear. I'm glad you're feeling this way.", "Oh, I see. That's great.", "Did something happen which made you feel this way?")
        );
        knowledgeBase.put("happy", happyPatterns);

        Map<List<String>, List<String>> depressedPatterns = new HashMap<>();
        depressedPatterns.put(
                Arrays.asList("I can't take it anymore", "I am so depressed", "I think I'm depressed.", "I have depression"),
                Arrays.asList("You're going to be okay.","Sometimes when we are depressed, it is hard to care about anything. It can be hard to do the simplest of things. Give yourself time to heal.")
        );
        knowledgeBase.put("depressed", depressedPatterns);

        // Help
        Map<List<String>, List<String>> helpPatterns = new HashMap<>();
        helpPatterns.put(
                Arrays.asList("Can you help?", "I need a therapist", "I need help"),
                Arrays.asList("Sure, tell me how can I assist you?", "Please seek help by contacting our green phone 9152987821.")
        );
        knowledgeBase.put("help", helpPatterns);

        // Suicide
        Map<List<String>, List<String>> suicidePatterns = new HashMap<>();
        suicidePatterns.put(
                Arrays.asList("I want to kill myself", "I am going to commit suicide"),
                Arrays.asList("I'm very sorry to hear that, but you have so much to look forward to. Please contact our green phone 9152987821 immediately.")
        );
        knowledgeBase.put("suicide", suicidePatterns);

        Map<List<String>, List<String>> sadPatterns = new HashMap<>();
        sadPatterns.put(
                Arrays.asList("i am feeling lonely", "i am so lonely", "i feel down", "i feel sad", "I am sad", "I feel so lonely", "I feel empty", "I don't have anyone"),
                Arrays.asList("I'm sorry to hear that. I'm here for you. So, tell me why do you think you're feeling this way?", "I'm here for you. Could you tell me why you're feeling this way?", "Why do you think you feel this way?", "How long have you been feeling this way?")
        );
        knowledgeBase.put("sad", sadPatterns);

        Map<List<String>, List<String>> AppointmentPatterns = new HashMap<>();
        sadPatterns.put(
                Arrays.asList("i want to make an appointment", "je veux un rendez vous"),
                Arrays.asList("check our website")
        );
        knowledgeBase.put("appointment", AppointmentPatterns);

        return knowledgeBase;
    }

}
