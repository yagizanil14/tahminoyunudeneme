package com.example.tahminoyunudeneme2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1000 ;
    Button btn_sign_out;
    List<AuthUI.IdpConfig> providers;
    Button OyunaBasla;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    DatabaseReference dbsorular;
    Kullanicilar kullanicilar;
    private ArrayList<String> odakullanicilari;
    Odalar odalar;
    private ArrayList<Sorular> sorularList;
    private ArrayList<Integer> cevaplar2;
    Sorular sorular;
    String sorumetni;
    String sorucevap;
    int sorucevap1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //FİREBASE
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        dbsorular = database.getReference("Sorular");
        FirebaseApp.initializeApp( this );

        odakullanicilari = new ArrayList<String>();
        cevaplar2 = new ArrayList<Integer>();
        kullanicilar = new Kullanicilar( UUID.randomUUID().toString() );
        sorular = new Sorular( null,0 );


        btn_sign_out = findViewById( R.id.btn_sign_out );
        OyunaBasla = (Button)findViewById( R.id.OyunaBasla );

        btn_sign_out.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Log Out
                AuthUI.getInstance(  ).signOut( MainActivity.this ).addOnCompleteListener( new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        btn_sign_out.setEnabled( false );
                        showSignInOptions();
                    }
                } ).addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText( MainActivity.this, ""+ e.getMessage(), Toast.LENGTH_SHORT ).show();
                    }
                } );
            }
        } );

        //Init providers
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()
                );

        showSignInOptions();
    }


    private void showSignInOptions() {
        startActivityForResult( AuthUI.getInstance(  ).createSignInIntentBuilder()
        .setAvailableProviders( providers ).setTheme( R.style.MyTheme ).build(),MY_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == MY_REQUEST_CODE){
            IdpResponse response = IdpResponse.fromResultIntent( data );
            if (resultCode == RESULT_OK){
                //GET USER
                 FirebaseUser user = FirebaseAuth.getInstance(  ).getCurrentUser();


                //SHOW EMAİL İN TOAST
                Toast.makeText( this, ""+ user.getEmail(), Toast.LENGTH_SHORT ).show();

                //SET BUTTON SİGN OUT
                btn_sign_out.setEnabled( true );

                //OYUNA BAŞLA
                OyunaBasla.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //QUERY İLE ODALARIN İÇİNDE MÜSAİTMİ DEĞERİ TRUE OLANI ARA
                        //YOKSA OYUNA BAŞLA METOTUNU KULLAN
                        //VARSA ODAYIDOLDUR METOTUNU KULLAN
                        odalar = null;
                        // Query ile musiatmi değeri true olan oda arayıp ilk sonucu vericek.
                        Query query = databaseReference.child("Odalar").orderByChild("musaitmi").equalTo( true );
                        query.addListenerForSingleValueEvent( new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getChildrenCount()!=0) { // Dedik ki sonuç sıfırdan fazla yani müsait oda var
                                    for (DataSnapshot odaSnapshot: dataSnapshot.getChildren()) {
                                        for(DataSnapshot kullanici : odaSnapshot.child("kullaniciuid").getChildren()){ // burası kullanıcı uuidsine bakıyor odaların içinde altındaki if bloğuda içerde kullanıcı varsa
                                             odakullanicilari.add(kullanici.getValue( String.class ));
                                            //gelenKullaniciID = kullanici.getValue(String.class);

                                            String gelenKullaniciID = kullanici.getValue(String.class);

                                            if(odakullanicilari.contains(gelenKullaniciID)){
                                                odalar = odaSnapshot.getValue( Odalar.class );
                                                if(odalar!=null){
                                                    odalar.setMusaitmi( false );
                                                    odalar.getKullaniciuid().add( kullanicilar.getUid() );
                                                    odakullanicilari.add( kullanicilar.getUid());
                                                    databaseReference.child( "Odalar" ).child( odalar.getOdauid() ).setValue( odalar );
                                                    Intent anasayfagec = new Intent( MainActivity.this, Main2Activity.class );
                                                    startActivity( anasayfagec );
                                                }else{
                                                    oyunabasla();
                                                }
                                            }
                                        }
                                    }
                                }else{
                                    oyunabasla();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        } );
                    }
                } );
            } else {
                Toast.makeText( this, ""+response.getError().getMessage(), Toast.LENGTH_SHORT ).show();
            }
        }
    }

    @SuppressLint("UseSparseArrays")
    private void oyunabasla() {



        sorularList = new ArrayList<Sorular>(  );
        cevaplar2.add( 0,0 );
        cevaplar2.add( 1,0 );

        //Yeni Oda Yarat

        odalar = new Odalar( UUID.randomUUID().toString(),true,odakullanicilari,sorularList,cevaplar2);

        odakullanicilari = new ArrayList<String>();
        odakullanicilari.add( kullanicilar.getUid());
        odalar.setKullaniciuid( odakullanicilari );
        odalar.setCevaplar( cevaplar2 );
        //DB'DEN SORULARI AL;
        databaseReference.child( "Sorular" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //SORULARI SORU METNİ VE CEVAP OLARAK AYRI AYRI AMA AYNI SORUNUN CEVABINI ALMASI LAZIM

                for (DataSnapshot sorumetnisnap:dataSnapshot.getChildren()){
                    sorumetni= (String) sorumetnisnap.child( "SoruMetni" ).getValue();
                    Log.e( "sorular alınıyor","+++++ "+sorumetni);
                    sorucevap= (String) sorumetnisnap.child( "Cevap" ).getValue();
                    sorucevap1 = Integer.parseInt( sorucevap );
                    Log.e( "sorular alınıyor","+++++ "+sorucevap1);
                    sorular.setSorumetni( sorumetni );
                    sorular.setSorucevap( sorucevap1 );
                    sorularList.add( sorular );
                }

                odalar.setOdadakisorular( sorularList );

                Log.e("Oda Açılıyor","----");
                databaseReference.child( "Odalar" ).child( odalar.getOdauid() ).setValue( odalar );
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }




}
