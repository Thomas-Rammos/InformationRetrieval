module InformationIntervalProject {
    requires lucene.memory;
    requires lucene.core;
    requires lucene.highlighter;
    requires lucene.suggest;
    requires lucene.queryparser;
    requires org.apache.commons.csv;
    requires org.apache.commons.codec; // Add this line
    requires jdk.unsupported;
    requires java.logging;
    requires java.desktop; // Add this line for Swing and AWT

    opens presentation_results to java.desktop;

    exports presentation_results;
}
