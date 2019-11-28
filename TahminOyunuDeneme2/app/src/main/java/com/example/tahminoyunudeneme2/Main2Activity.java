package com.example.tahminoyunudeneme2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    EditText Cevap;
    TextView sorumetni;
    Button Cevabi_Gonder, Anasayfaya_git;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    Odalar odalar;
    Cevaplar cevaplar1;
    Kullanicilar kullanicilar;
    private ArrayList<String> cevaplar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );

        //FİREBASE
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        FirebaseApp.initializeApp( this );

        sorumetni = (TextView) findViewById( R.id.SoruMetni );
        Cevap = (EditText)findViewById( R.id.Cevap );
        Cevabi_Gonder = (Button)findViewById( R.id.Cevabı_Gonder );
        Anasayfaya_git = (Button)findViewById( R.id.anasayfa );

        cevaplar1 = new Cevaplar( null );


        //FİREBASEDEN ODA BİLGİLERİ OKUNACAK SORULAR ALINACAK
        //CEVAPLAR İŞLENECEK ARADAKİ FARK FİREBASEYE GÖNDERİLECEK
        //İKİ FARKTA ALINACAK CEVABA EN YAKIN OLAN OYUNU KAZANDI YAZISI EKRANA ÇIKACAK
        //BU DÖNGÜ ŞEKLİNDE HER DÖNGÜYE SÜRE TANIMLANACAK 10 SORU BİTENE KADAR DEVAM EDECEK


        Cevabi_Gonder.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Firebaseye Cevapları Göndermek İçin

                cevaplar = new ArrayList<String>();
                cevaplar.add( Cevap.getText().toString() );
                cevaplar1.setCevaplar( cevaplar );



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
