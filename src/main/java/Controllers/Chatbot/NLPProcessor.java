package Controllers.Chatbot;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.POSModel;
import org.apache.commons.text.similarity.LevenshteinDistance;
import me.xdrop.fuzzywuzzy.FuzzySearch;  // Import the FuzzyWuzzy Java equivalent

import java.io.InputStream;
import java.util.*;

public class NLPProcessor {
    private SentenceDetectorME sentenceDetector;
    private Tokenizer tokenizer;
    private POSTaggerME posTagger;
    private static final int FUZZY_THRESHOLD = 80;

    // Mapping keywords to responses
    private Map<String, List<String>> keywordResponses;

    public NLPProcessor() {
        loadModels();
        initializeResponses();
    }

    // Load all OpenNLP models
    private void loadModels() {
        try {
            InputStream sentenceModelStream = getClass().getClassLoader().getResourceAsStream("models/en-sentence.bin");
            SentenceModel sentenceModel = new SentenceModel(sentenceModelStream);
            sentenceDetector = new SentenceDetectorME(sentenceModel);

            InputStream tokenizerModelStream = getClass().getClassLoader().getResourceAsStream("models/en-tokens.bin");
            TokenizerModel tokenizerModel = new TokenizerModel(tokenizerModelStream);
            tokenizer = new TokenizerME(tokenizerModel);

            InputStream posModelStream = getClass().getClassLoader().getResourceAsStream("models/en-pos.bin");
            POSModel posModel = new POSModel(posModelStream);
            posTagger = new POSTaggerME(posModel);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Initialize the responses for each keyword
    private void initializeResponses() {
        keywordResponses = new HashMap<>();
        keywordResponses.put("happy", Arrays.asList(
                "That's wonderful to hear! What's making you feel happy?",
                "It's great to focus on the positive things in our lives.",
                "Sharing happy moments can make them even more special."
        ));
        keywordResponses.put("joy", keywordResponses.get("happy"));  // Reuse happy responses for joy

        keywordResponses.put("sad", Arrays.asList(
                "I'm sorry to hear that you're feeling sad. Can you tell me more about what's bothering you?",
                "Sure, tell me how can I assist you?",
                "Please seek help by contacting our green phone 9152987821."
        ));
        keywordResponses.put("depressed", keywordResponses.get("sad"));  // Reuse sad responses

        keywordResponses.put("angry", Arrays.asList(
                "It sounds like you're feeling angry or frustrated. Let's talk about what's going on."
        ));
        keywordResponses.put("frustrated", keywordResponses.get("angry"));  // Reuse angry responses

        keywordResponses.put("anxious", Arrays.asList(
                "Feeling anxious or worried is tough. Can you share what's on your mind?"
        ));
        keywordResponses.put("worried", keywordResponses.get("anxious"));  // Reuse anxious responses

        keywordResponses.put("lonely", Arrays.asList(
                "Feeling lonely can be difficult. I'm here to chat with you if you need someone to talk to."
        ));

        keywordResponses.put("suicide", Arrays.asList(
                "I'm very sorry to hear that, but you have so much to look forward to. Please contact our green phone 9152987821 immediately."
        ));
        keywordResponses.put("kill", keywordResponses.get("suicide"));  // Reuse suicide responses

        keywordResponses.put("help", Arrays.asList(
                "Sure, tell me how can I assist you?",
                "Please seek help by contacting our green phone 9152987821."
        ));

        keywordResponses.put("hello", Arrays.asList(
                "Hi there. What brings you here today?",
                "Hello there. How are you feeling today?"
        ));
        keywordResponses.put("salut", keywordResponses.get("hello"));  // Reuse hello responses
        keywordResponses.put("hey", keywordResponses.get("hello"));  // Reuse hello responses

        keywordResponses.put("thank", Arrays.asList(
                "You're welcome!",
                "Anytime!"
        ));

        keywordResponses.put("rendez-vous", Arrays.asList(
                "Check out our website for more information."
        ));
        keywordResponses.put("appointment", keywordResponses.get("rendez-vous"));  // Reuse rendez-vous responses
    }


    // Process tokens with fuzzy matching
    public String processInputWithFuzzyMatching(String[] tokens) {
        List<String> responses = new ArrayList<>();
        for (String token : tokens) {
            String matchedKeyword = fuzzyMatch(token);
            if (matchedKeyword != null) {
                responses.addAll(keywordResponses.get(matchedKeyword));
            }
        }

        if (!responses.isEmpty()) {
            Random rand = new Random();
            return responses.get(rand.nextInt(responses.size()));
        }

        return "Tell me more...";
    }

    // Perform fuzzy matching
    private String fuzzyMatch(String token) {
        String bestMatch = null;
        int highestScore = FUZZY_THRESHOLD;

        for (String keyword : keywordResponses.keySet()) {
            int score = FuzzySearch.ratio(token, keyword);
            if (score > highestScore) {
                bestMatch = keyword;
                highestScore = score;
            }
        }
        return bestMatch;
    }

    // Detect sentences
    public String[] detectSentences(String input) {
        return sentenceDetector.sentDetect(input);
    }

    // Tokenize text
    public String[] tokenize(String input) {
        return tokenizer.tokenize(input);
    }

    // Tag tokens with POS tags
    public String[] tagPOS(String[] tokens) {
        return posTagger.tag(tokens);
    }
}

