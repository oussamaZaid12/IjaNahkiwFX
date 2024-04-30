package Controllers.Chatbot;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.POSModel;

import java.io.InputStream;

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

    public String getResponse(String input) {
        String[] sentences = detectSentences(input);
        StringBuilder response = new StringBuilder();

        for (String sentence : sentences) {
            String[] tokens = tokenize(sentence);
            String[] tags = tagPOS(tokens);
            response.append(processTokens(tokens, tags));
        }

        return response.toString();
    }

    private String processTokens(String[] tokens, String[] tags) {
        // Example: respond differently if a noun is mentioned

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].toLowerCase();
            switch (token) {
                case "happy":
                case "joy":
                    return "That's wonderful to hear! What's making you feel happy?";
                case "sad":
                case "depressed":
                    return "I'm sorry to hear that you're feeling sad. Can you tell me more about what's bothering you?";
                case "angry":
                case "frustrated":
                    return "It sounds like you're feeling angry or frustrated. Let's talk about what's going on.";
                case "anxious":
                case "worried":
                    return "Feeling anxious or worried is tough. Can you share what's on your mind?";
                case "lonely":
                    return "Feeling lonely can be difficult. I'm here to chat with you if you need someone to talk to.";
                case "suicide":
                    return "";
                case "help":
                    return "Please seek help by contacting our green phone 9152987821.";
                case "hello":
                    return "hi";

                // Add more emotional keywords and responses as needed
            }
        }

        // If no emotional keywords are detected, return a default response
        return "Tell me more...";
    }

            // Existing methods for detectSentences, tokenize, and tagPOS...


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
