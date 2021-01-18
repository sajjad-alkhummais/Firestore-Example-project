package com.example.android.firestoreexampleproject;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;


public class Note {


    // the names of the variables and the getters should be the same,
    // so that firestore recognize it
    private String documentId;
    private String title;
    private String description;


    // each variable that has getId method will be shown as a field in the document in Firestore db

    // this annotation prevents documentId field from being added to the document in the Firestore db
    // for more information about this annotation: episode 8 minute 10 second 30
    @Exclude
    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Note() {

        // Firestore always need an empty constructor

        // public no-arg constructor needed
    }

    public Note(String title, String description) {

        this.title = title;
        this.description = description;
    }

    public String getTitle() {

        return title;
    }

    public String getDescription() {

        return description;
    }
}
