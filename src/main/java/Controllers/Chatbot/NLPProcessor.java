package Controllers.Chatbot;

import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.postag.POSModel;
//import org.apache.commons.text.similarity.LevenshteinDistance;
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
            InputStream sentenceModelStream = getClass().getClassLoader().getResourceAsStream("models/fr-sentence.bin");
            SentenceModel sentenceModel = new SentenceModel(sentenceModelStream);
            sentenceDetector = new SentenceDetectorME(sentenceModel);

            InputStream tokenizerModelStream = getClass().getClassLoader().getResourceAsStream("models/fr-tokens.bin");
            TokenizerModel tokenizerModel = new TokenizerModel(tokenizerModelStream);
            tokenizer = new TokenizerME(tokenizerModel);

            InputStream posModelStream = getClass().getClassLoader().getResourceAsStream("models/fr-pos.bin");
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
                "C'est merveilleux à entendre ! Qu'est-ce qui vous rend heureux/heureuse ?",
                "C'est formidable de se concentrer sur les choses positives dans nos vies.",
                "Partager des moments heureux les rend encore plus spéciaux."
        ));
        keywordResponses.put("joy", keywordResponses.get("happy"));  // Reuse happy responses for joy

        keywordResponses.put("sad", Arrays.asList(
                "Je suis désolé(e) d'apprendre que vous êtes triste. Pouvez-vous m'en dire plus sur ce qui vous dérange ?",
                "Bien sûr, dites-moi comment je peux vous aider.",
                "Veuillez demander de l'aide en contactant notre ligne verte au 9152987821."
        ));
        keywordResponses.put("depressed", keywordResponses.get("sad"));  // Reuse sad responses

        keywordResponses.put("angry", Arrays.asList(
                "Il semble que vous soyez en colère ou frustré(e). Parlons de ce qui se passe."
        ));
        keywordResponses.put("frustrated", keywordResponses.get("angry"));  // Reuse angry responses

        keywordResponses.put("anxieuse", Arrays.asList(
                "Ressentir de l'anxiété ou de l'inquiétude est difficile. Pouvez-vous partager ce qui vous préoccupe ?"
        ));
        keywordResponses.put("salut", keywordResponses.get("anxieuse"));

        keywordResponses.put("worried", keywordResponses.get("anxious"));  // Reuse anxious responses

        keywordResponses.put("lonely", Arrays.asList(
                "Se sentir seul(e) peut être difficile. Je suis ici pour discuter avec vous si vous avez besoin de parler à quelqu'un."
        ));

        keywordResponses.put("suicide", Arrays.asList(
                "Je suis vraiment désolé(e) d'entendre ça, mais vous avez tant de choses à attendre avec impatience. Veuillez contacter notre ligne verte au 9152987821 immédiatement."
        ));
        keywordResponses.put("kill", keywordResponses.get("suicide"));  // Reuse suicide responses

        keywordResponses.put("aide", Arrays.asList(
                "Bien sûr, dites-moi comment je peux vous aider.",
                "Veuillez demander de l'aide en contactant notre ligne verte au 9152987821."
        ));

        keywordResponses.put("bonjour", Arrays.asList(
                "Salut. Qu'est-ce qui vous amène ici aujourd'hui ?",
                "Bonjour. Comment vous sentez-vous aujourd'hui ?"
        ));
        keywordResponses.put("salut", keywordResponses.get("bonjour"));  // Reuse hello responses
        keywordResponses.put("hey", keywordResponses.get("bonjour"));  // Reuse hello responses
        keywordResponses.put("coucou", keywordResponses.get("bonjour"));  // Reuse hello responses

        keywordResponses.put("merci", Arrays.asList(
                "Je vous en prie !",
                "À tout moment !"
        ));

        keywordResponses.put("rendez-vous", Arrays.asList(
                "Consultez notre site web pour plus d'informations."
        ));
        keywordResponses.put("appointment", keywordResponses.get("rendez-vous"));
        keywordResponses.put("rendez vous", keywordResponses.get("rendez-vous"));
        // Reuse rendez-vous responses
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

