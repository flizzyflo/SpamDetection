package wordCounter;

import java.util.Arrays;
import java.util.List;

public class StopWords {

    public static final List<String> GERMAN_STOPWORDS = Arrays.asList(
            "aber", "alle", "allem", "allen", "aller", "alles", "als", "also", "am", "an", "ander",
            "andere", "anderem", "anderen", "anderer", "anderes", "anderm", "andern", "anderr", "anders",
            "auch", "auf", "aus", "bei", "bin", "bis", "bist", "da", "damit", "dann", "das", "dass", "dasselbe",
            "dazu", "daß", "dein", "deine", "deinem", "deiner", "deines", "dem", "den", "der", "des", "desselben",
            "dessen", "dich", "die", "dies", "diese", "dieselbe", "dieselben", "diesem", "diesen", "dieser", "dieses",
            "dir", "doch", "dort", "du", "durch", "ein", "eine", "einem", "einen", "einer", "eines", "einig", "einige",
            "einigem", "einigen", "einiger", "einiges", "einmal", "er", "es", "etwas", "euch", "euer", "eure", "eurem",
            "euren", "eurer", "eures", "für", "gegen", "gewesen", "hab", "habe", "haben", "hat", "hatte", "hatten",
            "hier", "hin", "hinter", "ich", "ihm", "ihn", "ihnen", "ihr", "ihre", "ihrem", "ihren", "ihrer", "ihres",
            "im", "in", "indem", "ins", "ist", "jede", "jedem", "jeden", "jeder", "jedes", "jene", "jenem", "jenen",
            "jener", "jenes", "jetzt", "kann", "kein", "keine", "keinem", "keinen", "keiner", "keines", "können", "könnte",
            "machen", "man", "manche", "manchem", "manchen", "mancher", "manches", "mein", "meine", "meinem", "meinen",
            "meiner", "meines", "mehr", "meinige", "meinem", "meiner", "meinigem", "meinigen", "meiniger", "meiniges",
            "mit", "muss", "musste", "nach", "nicht", "nichts", "noch", "nun", "nur", "ob", "oder", "ohne", "sehr",
            "sein", "seine", "seinem", "seinen", "seiner", "seines", "selbst", "sich", "sie", "sind", "so", "solche",
            "solchem", "solchen", "solcher", "solches", "soll", "sollte", "sondern", "sonst", "über", "um", "und",
            "uns", "unse", "unsem", "unsen", "unser", "unses", "unter", "viel", "vom", "von", "vor", "während",
            "war", "waren", "warst", "was", "weg", "weil", "weiter", "welche", "welchem", "welchen", "welcher",
            "welches", "wenn", "werde", "werden", "wie", "wieder", "will", "wir", "wird", "wirst", "wo", "wollen",
            "wollte", "würde", "würden", "zu", "zum", "zur", "zwar", "zwischen"
    );

    public static boolean isStopword(String word) {
        return GERMAN_STOPWORDS.contains(word.toLowerCase());
    }
}