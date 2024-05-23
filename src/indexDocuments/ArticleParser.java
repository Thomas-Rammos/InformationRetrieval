package indexDocuments;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;


import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class ArticleParser {
    public static List<Article> parseCSV(String csvPath) throws IOException {
        List<Article> articlesList = new ArrayList<>();
        try (Reader fileReader = new FileReader(csvPath);
             CSVParser parser = CSVParser.parse(fileReader, CSVFormat.DEFAULT.withFirstRecordAsHeader())) {

            for (CSVRecord record : parser) {
                Article article = new Article();
                article.setSourceId(record.get(0));
                article.setYear(record.get(1));
                article.setTitle(record.get(2));
                article.setAbstractColumn(record.get(3));
                article.setFullText(record.get(4));

                articlesList.add(article);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return articlesList;
    }
}
