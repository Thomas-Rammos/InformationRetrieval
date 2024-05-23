package presentation_results;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;
import org.apache.lucene.document.Document;
import searchDocuments.DocumentsSearcher;
import indexDocuments.Article;
import indexDocuments.ArticleParser;
import indexDocuments.IndexCreation;

public class Presentation extends JFrame {
    private DocumentsSearcher searcher;
    private JTextField searchField;
    private JComboBox<String> fieldChoiceBox;
    private JRadioButton keywordSearchRadio;
    private JRadioButton fieldSearchRadio;
    private JRadioButton yearSearchRadio;
    private JTextField yearSearchField;
    private JCheckBox sortByYearCheckBox;
    private JButton searchButton;
    private DefaultListModel<String> listModel;
    private JList<String> resultsList;
    private List<Document> currentResults;
    private int currentPage = 0;
    private JPopupMenu historyPopupMenu;

    public Presentation() {
        setTitle("Document Searcher");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        try {
            searcher = new DocumentsSearcher("indexFiles/");
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        searchField = new JTextField(30);
        searchButton = new JButton("Search");

        keywordSearchRadio = new JRadioButton("Keyword");
        fieldSearchRadio = new JRadioButton("Field");
        yearSearchRadio = new JRadioButton("Year");
        ButtonGroup searchTypeGroup = new ButtonGroup();
        searchTypeGroup.add(keywordSearchRadio);
        searchTypeGroup.add(fieldSearchRadio);
        searchTypeGroup.add(yearSearchRadio);
        keywordSearchRadio.setSelected(true);

        fieldChoiceBox = new JComboBox<>(new String[]{"title", "full_text", "abstract", "year"});
        fieldChoiceBox.setEnabled(false);

        yearSearchField = new JTextField(10);
        yearSearchField.setEnabled(false);

        keywordSearchRadio.addActionListener(e -> {
            fieldChoiceBox.setEnabled(false);
            yearSearchField.setEnabled(false);
        });

        fieldSearchRadio.addActionListener(e -> {
            fieldChoiceBox.setEnabled(true);
            yearSearchField.setEnabled(false);
        });

        yearSearchRadio.addActionListener(e -> {
            fieldChoiceBox.setEnabled(false);
            yearSearchField.setEnabled(true);
        });

        sortByYearCheckBox = new JCheckBox("Sort by Year");

        JPanel searchPanel = new JPanel();
        searchPanel.add(searchField);
        searchPanel.add(keywordSearchRadio);
        searchPanel.add(fieldSearchRadio);
        searchPanel.add(fieldChoiceBox);
        searchPanel.add(yearSearchRadio);
        searchPanel.add(yearSearchField);
        searchPanel.add(searchButton);
        searchPanel.add(sortByYearCheckBox);

        listModel = new DefaultListModel<>();
        resultsList = new JList<>(listModel);
        resultsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        resultsList.addListSelectionListener(this::showFullText);

        JScrollPane resultsScrollPane = new JScrollPane(resultsList);

        JPanel paginationPanel = new JPanel();
        JButton previousButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");
        paginationPanel.add(previousButton);
        paginationPanel.add(nextButton);

        previousButton.addActionListener(e -> showPreviousPage());
        nextButton.addActionListener(e -> showNextPage());

        add(searchPanel, BorderLayout.NORTH);
        add(resultsScrollPane, BorderLayout.CENTER);
        add(paginationPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(e -> performSearch());

        historyPopupMenu = new JPopupMenu();
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                showQueryHistoryPopup();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                showQueryHistoryPopup();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showQueryHistoryPopup();
            }
        });
    }

    private void showQueryHistoryPopup() {
        historyPopupMenu.removeAll();
        for (String query : searcher.getQueryHistory()) {
            JMenuItem menuItem = new JMenuItem(query);
            menuItem.addActionListener(ev -> {
                searchField.setText(query);
                performSearch();
            });
            historyPopupMenu.add(menuItem);
        }
        if (searchField.getText().isEmpty()) {
            historyPopupMenu.setVisible(false);
        } else {
            historyPopupMenu.show(searchField, 0, searchField.getHeight());
        }
    }

    private void performSearch() {
        String queryStr = searchField.getText();
        boolean sortByYear = sortByYearCheckBox.isSelected();
        List<Document> results = null;
        try {
            if (keywordSearchRadio.isSelected()) {
                results = searcher.search(queryStr, sortByYear);
            } else if (fieldSearchRadio.isSelected()) {
                String field = (String) fieldChoiceBox.getSelectedItem();
                results = searcher.searchByField(field, queryStr, sortByYear);
            } else if (yearSearchRadio.isSelected()) {
                String year = yearSearchField.getText();
                results = searcher.searchByYear(year);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (results != null) {
            currentResults = results;
            currentPage = 0;
            displayResults();
        }
    }

    private void displayResults() {
        listModel.clear();
        int start = currentPage * 10;
        int end = Math.min(start + 10, currentResults.size());
        for (int i = start; i < end; i++) {
            Document doc = currentResults.get(i);
            String title = doc.get("title");
            String year = doc.get("year");
            String abstractText = doc.get("abstract");
            listModel.addElement(String.format("<html><b><font color='blue'>%s (%s)</font></b><br><div style='margin-left: 20px;'>%s</div><br><br></html>", title, year, abstractText));
        }
    }

    private void showFullText(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            int index = resultsList.getSelectedIndex();
            if (index != -1) {
                Document doc = currentResults.get(index);
                String fullText = doc.get("fullText");

                JTextArea textArea = new JTextArea(fullText);
                textArea.setWrapStyleWord(true);
                textArea.setLineWrap(true);
                textArea.setCaretPosition(0);
                textArea.setEditable(false);

                JScrollPane scrollPane = new JScrollPane(textArea);
                scrollPane.setPreferredSize(new Dimension(600, 400));

                JOptionPane.showMessageDialog(this, scrollPane, "Full Text", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }

    private void showPreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            displayResults();
        }
    }

    private void showNextPage() {
        if ((currentPage + 1) * 10 < currentResults.size()) {
            currentPage++;
            displayResults();
        }
    }

    public static void main(String[] args) {
        // Create index
        try {
            List<Article> articles = ArticleParser.parseCSV("inputFiles/mypapers.csv");
            IndexCreation indexCreation = new IndexCreation("indexFiles/");
            indexCreation.buildIndex(articles);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        // Launch Swing application
        SwingUtilities.invokeLater(() -> {
            Presentation app = new Presentation();
            app.setVisible(true);
        });
    }
}
