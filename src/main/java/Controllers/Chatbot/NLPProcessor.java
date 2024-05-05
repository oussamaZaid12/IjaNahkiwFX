package Controllers.Chatbot;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.POSModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NLPProcessor {
            private SentenceDetectorME sentenceDetector;
            private Tokenizer tokenizer;
            private POSTaggerME posTagger;

            public NLPProcessor() {
                try {
                    // Load Sentence Model
                    InputStream sentenceModelStream = getClass().getClassLoader().getResourceAsStream("models/en-sentence.bin");
                    if (sentenceModelStream == null) {
                        throw new IllegalArgumentException("Sentence model file not found in resources.");
                    }
                    SentenceModel sentenceModel = new SentenceModel(sentenceModelStream);
                    sentenceDetector = new SentenceDetectorME(sentenceModel);

                    // Load Tokenizer Model
                    InputStream tokenizerModelStream = getClass().getClassLoader().getResourceAsStream("models/en-tokens.bin");
                    if (tokenizerModelStream == null) {
                        throw new IllegalArgumentException("Tokenizer model file not found in resources.");
                    }
                    TokenizerModel tokenizerModel = new TokenizerModel(tokenizerModelStream);
                    tokenizer = new TokenizerME(tokenizerModel);

                    // Load POS Model
                    InputStream posModelStream = getClass().getClassLoader().getResourceAsStream("models/en-pos.bin");
                    if (posModelStream == null) {
                        throw new IllegalArgumentException("POS model file not found in resources.");
                    }
                    POSModel posModel = new POSModel(posModelStream);
                    posTagger = new POSTaggerME(posModel);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

     String processTokens(String[] tokens, String[] tags) {
        Random rand = new Random();
        List<List<String>> responses = new ArrayList<>();

        for (String token : tokens) {
            token = token.toLowerCase();
            List<String> emotionResponses = new ArrayList<>();
            switch (token) {
                case "happy":
                case "joy":
                    emotionResponses.add("That's wonderful to hear! What's making you feel happy?");
                    emotionResponses.add("It's great to focus on the positive things in our lives.");
                    emotionResponses.add("Sharing happy moments can make them even more special.");
                    break;
                case "sad":
                case "depressed":
                    emotionResponses.add("I'm sorry to hear that you're feeling sad. Can you tell me more about what's bothering you?");
                    emotionResponses.add("Sure, tell me how can I assist you?");
                    emotionResponses.add("Please seek help by contacting our green phone 9152987821.");
                    break;
                case "angry":
                case "frustrated":
                    emotionResponses.add("It sounds like you're feeling angry or frustrated. Let's talk about what's going on.");
                    break;
                case "anxious":
                case "worried":
                    emotionResponses.add("Feeling anxious or worried is tough. Can you share what's on your mind?");
                    break;
                case "lonely":
                    emotionResponses.add("Feeling lonely can be difficult. I'm here to chat with you if you need someone to talk to.");
                    break;
                case "suicide":
                case "kill":
                    emotionResponses.add("I'm very sorry to hear that, but you have so much to look forward to. Please contact our green phone 9152987821 immediately.");
                    break;
                case "help":
                    emotionResponses.add("Sure, tell me how can I assist you?");
                    emotionResponses.add("Please seek help by contacting our green phone 9152987821.");
                    break;
                case "hello":
                case "salut":
                case "hey":
                    emotionResponses.add("Hi there. What brings you here today?");
                    emotionResponses.add("Hello there. How are you feeling today?");
                    break;
                case "thank":
                    emotionResponses.add("today?");
                    emotionResponses.add("y?");
                    break;
                case "rendez-vous":
                case "appointment":
                    emotionResponses.add("Check out our website");
                    break;
            }
            if (!emotionResponses.isEmpty()) {
                responses.add(emotionResponses);
            }
        }

        // Randomly select a response from the collected responses
        if (!responses.isEmpty()) {
            List<String> selectedResponses = responses.get(rand.nextInt(responses.size()));
            return selectedResponses.get(rand.nextInt(selectedResponses.size()));
        }

        return "Tell me more...";
    }




    public String[] detectSentences(String input) {
        return sentenceDetector.sentDetect(input);
    }

    public String[] tokenize(String input) {
        return tokenizer.tokenize(input);
    }

    public String[] tagPOS(String[] tokens) {
        return posTagger.tag(tokens);
    }
}
