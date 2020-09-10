import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static Document getPage() throws IOException {
        String url = "http://pogoda.spb.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    //\d{2}\.\d{2}
    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");

    private static String getDateFromString(String date) throws Exception {
        Matcher matcher = pattern.matcher(date);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Ничего не нашел!");
    }

    private static int printPartValues(Elements values, int index) {
        int iterationCount = 4;
        if (index == 0) {
            Element valuesLn = values.get(3);
            boolean ismorning = valuesLn.text().contains("Утро");
            if (ismorning) {
                iterationCount = 3;
            }
        }
        for (int i = 0; i < iterationCount; i++) {
            Element valuesLine = values.get(index + i);
            for (Element td : valuesLine.select("td")) {
                System.out.print(td.text() + "    ");
            }
            System.out.println();
        }

        return iterationCount;
    }

    public static void main(String[] args) throws Exception {


        Document page = getPage();
        //query language
        Element table = page.select("table[class=wt]").first();
        Elements names = table.select("tr[class=wth]");
        Elements values = table.select("tr[valign=top]");

        int index = 0;


        for (Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getDateFromString(dateString);
            System.out.println(date + "    Явления   Температура  Давление   Влажность   Ветер");
            int iterationCount = printPartValues(values, index);
            index = index + iterationCount;

        }

    }
}
