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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

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

        noteRef.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {   // here we add the (this) <--2
            @Override

            //this method will be called when on start is called and whenever a change happens to the data in the db
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                if (e != null) {

                    Toast.makeText(MainActivity.this, "Error while loading!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, e.toString());
                    //this return stops us from continuing if something went wrong, it is necessary
                    //because otherwise documentSnapshot will be null which will crash the app
                    return;
                }

                if (documentSnapshot.exists()) {

                    //    {
                    //String title = documentSnapshot.getString(KEY_TITLE);
                    //String description = documentSnapshot.getString(KEY_DESCRIPTION);

                    //or we can call the whole map
                    //Map<String, Object> note = documentSnapshot.getData();


                    //     }
                    //or to retrieve data from the database into out own constructor

                    Note note = documentSnapshot.toObject(Note.class);

                    String title = note.getTitle();
                    String description = note.getDescription();

                    textViewData.setText("Title: " + title + "\n" + "Description: " + description);
                 }else {

                    textViewData.setText("Title ");
                }

            }
        });
    }

    //to save data inside the firestore data base
    public void saveNote(View v) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        //we can use a hashMap to contain the data before inserting them to the database
//        Map<String, Object> note = new HashMap<>();
//        note.put(KEY_TITLE, title);
//        note.put(KEY_DESCRIPTION, description);


        //or we can just create our own constructor
        Note note = new Note(title, description);

        noteRef.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Note saved", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

                Log.d(TAG, e.toString());
            }
        });


    }


    //updating only the description
    public void updateDescription(View v){

        String description = editTextDescription.getText().toString();

        //Map<String, Object> note = new HashMap<>();

        //note.put(KEY_DESCRIPTION, description);

        //to update only specific fields, and if there isn't a document it creates one
        //noteRef.set(note, SetOptions.merge());

        //to update only specific fields,
        //and if there isn't a document it doesn't create one
        //noteRef.update(note);

        //We can also update without the hashMap, we can do this
        // (doesn't create a document if there isn't one)
        noteRef.update(KEY_DESCRIPTION, description);

    }

    public void deleteDescription(View v){

//        Map<String, Object> note = new HashMap<>();
//
//        note.put(KEY_DESCRIPTION, FieldValue.delete());
//
//        noteRef.update(note);

        //or we can just do this

        //we can also add OnSuccessListener and OnFailureListener as usual
        noteRef.update(KEY_DESCRIPTION, FieldValue.delete());

    }

    public void deleteNote(View v){

    noteRef.delete();

    }

    //to fetch data from the firestore data base when something happens(when a button is clicked in this case)

    public void loadNote(View v) {

        noteRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        //documentSnapshot contain all our data that are in the data base
                        //to check that there are data in the first place
                        if (documentSnapshot.exists()) {

                            //    {
                             //String title = documentSnapshot.getString(KEY_TITLE);
                             //String description = documentSnapshot.getString(KEY_DESCRIPTION);

                            //or we can call the whole map
                            //Map<String, Object> note = documentSnapshot.getData();


                            //     }
                            //or to retrieve data from the database into out own constructor

                            Note note = documentSnapshot.toObject(Note.class);

                            String title = note.getTitle();
                            String description = note.getDescription();

                            textViewData.setText("Title: " + title + "\n" + "Description: " + description);
                        } else {

                            Toast.makeText(MainActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                Log.d(TAG, e.toString());


            }
        });


    }
}