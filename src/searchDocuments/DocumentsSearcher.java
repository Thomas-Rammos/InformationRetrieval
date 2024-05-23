package searchDocuments;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DocumentsSearcher {

    private Directory directory;
    private DirectoryReader reader;
    private IndexSearcher searcher;
    private StandardAnalyzer analyzer;
    private QueryHistory queryHistory;

    public DocumentsSearcher(String indexDirPath) throws IOException {
        directory = FSDirectory.open(Paths.get(indexDirPath));
        reader = DirectoryReader.open(directory);
        searcher = new IndexSearcher(reader);
        analyzer = new StandardAnalyzer();
        queryHistory = new QueryHistory();
    }

    public List<Document> search(String queryStr, boolean sortByYear) throws Exception {
        if (queryStr == null || queryStr.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String[] fields = {"title", "full_text", "abstract", "year"};
        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);
        Query query = parser.parse(queryStr);
        queryHistory.addQuery(queryStr);

        Sort sort = sortByYear ? new Sort(new SortField("year", SortField.Type.STRING, true)) : Sort.INDEXORDER;
        return getSearchResults(query, sort, queryStr);
    }

    public List<Document> searchByField(String field, String queryStr, boolean sortByYear) throws Exception {
        if (queryStr == null || queryStr.trim().isEmpty() || field == null || field.trim().isEmpty()) {
            return new ArrayList<>();
        }
        QueryParser parser = new QueryParser(field, analyzer);
        Query query = parser.parse(queryStr);
        queryHistory.addQuery(queryStr);

        Sort sort = sortByYear ? new Sort(new SortField("year", SortField.Type.STRING, true)) : Sort.INDEXORDER;
        return getSearchResults(query, sort, queryStr);
    }

    public List<Document> searchByYear(String year) throws Exception {
        if (year == null || year.trim().isEmpty()) {
            return new ArrayList<>();
        }
        QueryParser parser = new QueryParser("year", analyzer);
        Query query = parser.parse(year);
        queryHistory.addQuery(year);

        Sort sort = new Sort(new SortField("year", SortField.Type.STRING, true));
        return getSearchResults(query, sort, year);
    }

    private List<Document> getSearchResults(Query query, Sort sort, String queryStr) throws IOException, InvalidTokenOffsetsException, ParseException {
        List<Document> results = new ArrayList<>();
        TopDocs topDocs = searcher.search(query, 1000, sort);
        for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
            Document doc = searcher.doc(scoreDoc.doc);
            String abstractText = doc.get("abstract");

            if (abstractText != null) {
                String highlightedText = getHighlightedText(abstractText, queryStr);
                doc.removeField("abstract");
                doc.add(new org.apache.lucene.document.TextField("abstract", highlightedText, org.apache.lucene.document.Field.Store.YES));
            }

            results.add(doc);
        }
        return results;
    }

    private String getHighlightedText(String text, String queryStr) throws IOException, InvalidTokenOffsetsException, ParseException {
        QueryScorer scorer = new QueryScorer(new QueryParser("abstract", analyzer).parse(queryStr));
        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
        SimpleHTMLFormatter formatter = new SimpleHTMLFormatter("<b><font color='red'>", "</font></b>");
        Highlighter highlighter = new Highlighter(formatter, scorer);
        highlighter.setTextFragmenter(fragmenter);

        TokenStream tokenStream = analyzer.tokenStream("abstract", text);
        TextFragment[] fragments = highlighter.getBestTextFragments(tokenStream, text, false, 2);
        StringBuilder highlightedText = new StringBuilder();

        for (TextFragment fragment : fragments) {
            if (fragment != null && fragment.getScore() > 0) {
                highlightedText.append(fragment.toString());
            }
        }

        return highlightedText.toString();
    }

    public List<String> getQueryHistory() {
        return queryHistory.getQueries();
    }

    public List<String> suggestQueries(String currentQuery) {
        return queryHistory.suggestQueries(currentQuery);
    }
}
