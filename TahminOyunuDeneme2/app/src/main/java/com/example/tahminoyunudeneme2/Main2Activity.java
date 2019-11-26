package com.example.tahminoyunudeneme2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Main2Activity extends AppCompatActivity {

    EditText Cevap;
    Button Cevabı_Gonder, Anasayfaya_git;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference cevabdb = database.getReference("Alınan_Cevap");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );

        Cevap = (EditText)findViewById( R.id.Cevap );
        Cevabı_Gonder = (Button)findViewById( R.id.Cevabı_Gonder );
        Anasayfaya_git = (Button)findViewById( R.id.anasayfa );

        Cevabı_Gonder.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String veri = Cevap.getText().toString();

                cevabdb.setValue( veri );

            }
        } );

        Anasayfaya_git.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anasayfagit = new Intent( Main2Activity.this,MainActivity.class );
                startActivity( anasayfagit );
            }
        } );
    }
}
