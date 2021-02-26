package link.dayang.tjutname.datasource.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class EnrollDateParser {
    public static String parse(String raw) {
        Document document = Jsoup.parse(raw);
        return document.select("#rxnj").attr("value");
    }
}
