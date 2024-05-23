package indexDocuments;

public class Article {
    private String sourceId;
    private String year;
    private String title;
    private String abstractColumn;
    private String fullText;
    private String authorsNames;

    // Getters and Setters

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractColumn() {
        return abstractColumn;
    }

    public void setAbstractColumn(String abstractColumn) {
        this.abstractColumn = abstractColumn;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getAuthorsNames() {
        return authorsNames;
    }

    public void setAuthorsNames(String authorsNames) {
        this.authorsNames = authorsNames;
    }

    // toString method for debugging purposes
    @Override
    public String toString() {
        return "Article{" +
                "sourceId='" + sourceId + '\'' +
                ", year='" + year + '\'' +
                ", title='" + title + '\'' +
                ", abstractColumn='" + abstractColumn + '\'' +
                ", fullText='" + fullText + '\'' +
                ", authorsNames='" + authorsNames + '\'' +
                '}';
    }
}
