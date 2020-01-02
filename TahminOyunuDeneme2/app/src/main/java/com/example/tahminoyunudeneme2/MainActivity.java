package com.example.tahminoyunudeneme2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1000 ;
    Button btn_sign_out;
    List<AuthUI.IdpConfig> providers;
    Button OyunaBasla,skrtblbtn;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    TextView textskor,textseninskor;
    public static Kullanicilar kullanicilar;
     ArrayList<String> odakullanicilari;
     ArrayList<Kullanicilar> skorlarary;
   public Odalar odalar;
     ArrayList<Sorular> sorularList,sorularlist2;
     ArrayList<Cevaplar> cevaplar2;
    Sorular sorular;
    String sorumetni;
    int sorucevap1, index;
    Integer skorun = 0;
    Cevaplar cevaplar;
    HashMap<Integer,Sorular> hashsorular;
    Random randomsayi;
    ArrayList<Integer> rndsayilar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //FİREBASE
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        FirebaseApp.initializeApp( this );



        skorlarary = new ArrayList<>(  );
        odakullanicilari = new ArrayList<String>();
        cevaplar = new Cevaplar();

        index = 0;



        skrtblbtn = (Button) findViewById( R.id.skorbtn );
        textskor = (TextView) findViewById( R.id.textskortbl );
        textseninskor = (TextView) findViewById( R.id.skortext );
        btn_sign_out = findViewById( R.id.btn_sign_out );
        OyunaBasla = (Button)findViewById( R.id.OyunaBasla );
        hashsorular = new HashMap<>(  );

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

                kullanicilar = new Kullanicilar( user.getUid(),user.getDisplayName(),null );

                databaseReference.child( "Kullanicilar" ).child( kullanicilar.getUid() ).child( "KullaniciAdi" ).setValue( kullanicilar.getKullaniciAdi() );

                toplamskor();
                skortbl();


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
                                                    anasayfagec.putExtra( "odauid",odalar.getOdauid());
                                                    anasayfagec.putExtra( "kullaniciuid", kullanicilar.getUid());
                                                    anasayfagec.putExtra( "odalar",Odalar.class );
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

    private void toplamskor(){
        databaseReference.child( "Kullanicilar" ).child( kullanicilar.getUid() ).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot skorsnap:dataSnapshot.getChildren()){
                    skorun = ( Integer ) skorsnap.child( "Skorlar" ).getValue();

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
        databaseReference.child( "Kullanicilar" ).child( kullanicilar.getUid() ).child( "Skorlar" ).setValue( skorun );

        String strskor = String.valueOf( skorun );

        textseninskor.setText( strskor );

    }

    private void skortbl(){
        databaseReference.child( "Kullanicilar" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot skorlarsnap:dataSnapshot.getChildren()){
                int skrtint = 0;
                String skrtbl = String.valueOf( skorlarsnap.child( "Skorlar" ).getValue() );
                String kllaniciadi = String.valueOf(skorlarsnap.child( "KullaniciAdi" ).getValue());

                kullanicilar = new Kullanicilar( null,kllaniciadi,skrtbl );

                skorlarary.add( kullanicilar );

               // skorlarary.get( skrtint ).setKullaniciAdi(  );
               // skorlarary.get( skrtint ).setSkor( skrtbl );

                Log.e( "Skorlarary", skrtbl );
                Log.e( "Kullaniciadi", kllaniciadi );
                skrtint ++;
            }


                Collections.reverse( skorlarary );
            textskor.setText("Skorlar:"+ "\n" +skorlarary.get( 0 ).getKullaniciAdi()+":"+skorlarary.get( 0 ).getSkor()+ "\n"+skorlarary.get( 1 ).getKullaniciAdi()+":"+skorlarary.get( 1 ).getSkor()+ "\n" );
            }

           // + skorlarary.get( 1 )+ "\n"+ skorlarary.get( 2 )+ "\n"+ skorlarary.get( 3 )

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }


    private void oyunabasla() {

        //Yeni Oda Yarat

        odalar = new Odalar( UUID.randomUUID().toString(),true,odakullanicilari,sorularList,cevaplar2);

        odakullanicilari = new ArrayList<String>();
        odakullanicilari.add( kullanicilar.getUid());
        odalar.setKullaniciuid( odakullanicilari );

        //odadakicevaplar = new ArrayList<>(  );
        //odadakicevaplar.add( 0,"0" );

        rndsayilar = new ArrayList<>(  );

        cevaplar2 = new ArrayList<>(  );
        cevaplar2.add( cevaplar );
       // cevaplar.setCevaplar1( odadakicevaplar );

       // cevaplar2 = new ArrayList<Cevaplar>();
        sorularList = new ArrayList<Sorular>(  );
        sorularlist2 = new ArrayList<>(  );
       // cevaplar2.add( cevaplar );
        odalar.setCevaplar( cevaplar2 );
        //DB'DEN SORULARI AL;



        databaseReference.child( "Sorular" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //SORULARI SORU METNİ VE CEVAP OLARAK AYRI AYRI AMA AYNI SORUNUN CEVABINI ALMASI LAZIM

                for (DataSnapshot sorumetnisnap:dataSnapshot.getChildren()){//ve ekleyip sayaç eklicez sayaç 10 olduğunda fordan çıkacak
                        index++;
                        sorular = new Sorular( null, null );
                        sorumetni = (String) sorumetnisnap.child( "SoruMetni" ).getValue();
                        sorular.setSorumetni( sorumetni );
                        Log.e( "sorular alınıyor", "+++++ " + sorumetni );
                        sorucevap1 = (int) sorumetnisnap.child( "Cevap" ).getValue( Integer.class );
                        sorular.setSorucevap( sorucevap1 );
                        Log.e( "sorular cevap alınıyor", "+++++ " + sorucevap1 );
                        sorularList.add( sorular );
                }

                randomsayi = new Random(  );

                for (int b = sorularlist2.size(); b<10; b = sorularlist2.size()){
                 int randomsayi1 = randomsayi.nextInt(index);
                 if (!rndsayilar.contains( randomsayi1 )){
                     sorularlist2.add( sorularList.get( randomsayi1 ) );
                 }
                    rndsayilar.add( randomsayi1 );
                }

                odalar.setOdadakisorular( sorularlist2 );
                Log.e("Oda Açılıyor","----");
                databaseReference.child( "Odalar" ).child( odalar.getOdauid() ).setValue( odalar );
                Intent anasayfagec = new Intent( MainActivity.this, Main2Activity.class );
                anasayfagec.putExtra( "odauid",odalar.getOdauid());
                startActivity( anasayfagec );
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }




}
