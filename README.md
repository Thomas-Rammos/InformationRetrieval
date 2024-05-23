# InformationRetrieval
Design and implementation of a system for searching information from scientific articles
# Intro 
Lucene πρόγραμμα σε Java , ειδικά σχεδιασμένο για αναζήτηση πληροφορίας από επιστημονικά άρθρα από CSV αρχείο.
# How it works 
Το πρόγραμμα διαβάζει επιστημονικά άρθρα από ένα αρχείο CSV, ευρετηριάζει τα δεδομένα αυτά με τη χρήση της Lucene βιβλιοθήκης και δίνει τη δυνατότητα στους χρήστες να αναζητήσουν τα ευρετηριασμένα αυτά δεδομένα χρησιμοποιώντας λέξεις-κλειδιά, αναζήτηση πεδίου δηλαδή μπορούν να φιλτράρουν την αναζήτησή τους με βάση πεδία όπως ο τίτλος, abstract, year και full text. Το σύστημα διατηρεί επίσης ένα ιστορικό των ερωτημάτων των χρηστών και παρέχει προτάσεις για μελλοντικά ερωτήματα. 
## How to use
Εκτελώντας το πρόγραμμα θα σας εμφανιστεί ένα Web view page.

Πληκτρολογήστε το query σας στο search bar επιλέγοντας keyword και πατήστε το κουμπί "Search".
Επιπλέον επιλέγοντας στα checkbox το field μπορείτε να κάνετε αναζήτηση με βάση τα πεδία title,full_text,abstract και year καθώς επίσης να τα ταξινομήσετε με φθίνουσα σειρά με βάση την χρονολογία τους και Θα εμφανιστούν τα αποτελέσματα με βάση την συνάφεια.

Πατώντας πάνω στον τίτλο ενός εκ των αποτελεσμάτων, μεταφέρεστε σε ένα παράθυρο όπου μπορείτε να δείτε το πλήρες κείμενο του άρθρου.

Περαιτέρω μπορείτε να περιηγηθείτε στην επόμενη και στην προηγούμενη σελίδα πατώντας τα κουμπιά "Next" και "Previous" αντίστοιχα.
