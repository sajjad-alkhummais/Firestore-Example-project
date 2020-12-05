package com.example.android.firestoreexampleproject;

import androidx.annotation.NonNull;



public class Note {

    //the names of the variables and the getters should be the same,
    // so that firestore recognize it
   private String title;
   private String description;

   public  Note(){

       //firestore always need an empty constructor

       //public no-arg constructor needed
   }

   public Note(String title, String description){

       this.title = title;
       this.description = description;
   }

   public String getTitle(){

       return title;
   }

   public String getDescription(){

       return description;
   }
}
