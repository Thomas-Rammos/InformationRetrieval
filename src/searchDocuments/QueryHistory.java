package searchDocuments;

import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class QueryHistory {

    private Set<String> queries;
    private static final String FILE_NAME = "query_history.txt";

    public QueryHistory() {
        queries = new HashSet<>();
        loadQueries();
    }

    public void addQuery(String query) {
        if (queries.add(query)) {
            saveQueries();
        }
    }

    public List<String> getQueries() {
        return queries.stream().collect(Collectors.toList());
    }

    public List<String> suggestQueries(String currentQuery) {
        return queries.stream()
            .filter(query -> !query.equals(currentQuery) && query.contains(currentQuery))
            .collect(Collectors.toList());
    }

    private void loadQueries() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                queries.add(line);
            }
        } catch (IOException e) {
            // Ignore if the file doesn't exist yet
        }
    }

    private void saveQueries() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (String query : queries) {
                writer.write(query);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
