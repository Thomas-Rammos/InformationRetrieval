package indexDocuments;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class IndexCreation {
	 private Directory indexDirectory;

	    public IndexCreation(String indexPath) throws IOException {
	        this.indexDirectory = FSDirectory.open(Paths.get(indexPath));
	    }

	    public void buildIndex(List<Article> articles) {
	        try {
	            Analyzer analyzer = new StandardAnalyzer();
	            IndexWriterConfig config = new IndexWriterConfig(analyzer);

	            try (IndexWriter writer = new IndexWriter(indexDirectory, config)) {
	                for (Article article : articles) {
	                    Document doc = new Document();
	                    doc.add(new TextField("sourceId", article.getSourceId(), Field.Store.YES));
	                    doc.add(new TextField("year", article.getYear(), Field.Store.YES));
	                    doc.add(new TextField("title", article.getTitle(), Field.Store.YES));
	                    doc.add(new TextField("abstract", article.getAbstractColumn(), Field.Store.YES));
	                    doc.add(new TextField("fullText", article.getFullText(), Field.Store.YES));
	                    writer.addDocument(doc);
	                }
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	}

