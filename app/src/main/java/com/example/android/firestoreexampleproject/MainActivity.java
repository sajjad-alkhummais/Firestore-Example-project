package com.example.android.firestoreexampleproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    public static final String TAG = "MainActivity";
    public static final String KEY_TITLE = "title";
    public static final String KEY_DESCRIPTION = "description";

    private EditText editTextTitle;
    private EditText editTextDescription;
    private TextView textViewData;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("NoteBook");
    //we create a reference for the document
    private DocumentReference noteRef = db.document("NoteBook/My First Note");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        textViewData = findViewById(R.id.text_view_data);


    }

    //to fetch data from the firestore on real time so we see a real time updates

    //we should detach(stop) this cyclic checking in the right time or the app will consumes the app resources
    // to do this we add (this) as a second parameter to addSnapshotListener method --> 2

    @Override
    protected void onStart() {
        super.onStart();

        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                if (e != null){

                    return;


                }

                String data = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                    Note note = documentSnapshot.toObject(Note.class);
                    note.setDocumentId(documentSnapshot.getId());


                    String documentId = note.getDocumentId();
                    String title = note.getTitle();
                    String description = note.getDescription();

                    data += "ID: "+ documentId
                            + "\nTitle: " + title + "\nDescription: " + description + "\n\n";

                    //if you want to get a reference for a document
                    notebookRef.document(documentId);
                }


                textViewData.setText(data);

            }
        });


    }

    //to save data inside the firestore data base
    public void addNote(View v) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        //we can use a hashMap to contain the data before inserting them to the database
//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_TITLE, title);
//        note.put(KEY_DESCRIPTION, description);


        //or we can just create our own constructor
        Note note = new Note(title, description);

        //it is preferable to add onSuccess listener on a real app so that you know what happens
        notebookRef.add(note);


    }


    //to fetch data from the Firestore data base when something happens(when a button is clicked in this case)
    public void loadNotes(View v) {

        notebookRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                //queryDocumentSnapshots contains all the documents(the whole collection)

                String data = "";
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                    Note note = documentSnapshot.toObject((Note.class));
                    //in a real app you should add the documents information into an arraylist
                    note.setDocumentId(documentSnapshot.getId());

                    String documentId = note.getDocumentId();
                    String title = note.getTitle();
                    String description  = note.getDescription();

                    data += "ID: "+ documentId
                            + "\nTitle: " + title + "\nDescription: " + description + "\n\n";

                }

                textViewData.setText(data);
            }
        });


    }
}