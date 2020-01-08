package com.example.tahminoyunudeneme2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1000 ;
    Button btn_sign_out;
    List<AuthUI.IdpConfig> providers;
    Button OyunaBasla,skrtblbtn,marketegit;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    TextView textskor,textseninskor,AltinText;
    public static Kullanicilar kullanicilar;
     ArrayList<String> odakullanicilari,kullaniciadi;
     ArrayList<Kullanicilar> skorlarary;
   public Odalar odalar;
     ArrayList<Sorular> sorularList,sorularlist2;
     ArrayList<Cevaplar> cevaplar2;
    Sorular sorular;
    String sorumetni,kullaniciuid;
    Integer sorucevap1;
    Integer skorun =0,index,Altin;
    Cevaplar cevaplar;
    HashMap<Integer,Sorular> hashsorular;
    Random randomsayi;
    ArrayList<Integer> rndsayilar, listskorlar;

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


        kullaniciadi = new ArrayList<>(  );

        skrtblbtn = (Button) findViewById( R.id.skorbtn );
       // textskor = (TextView) findViewById( R.id.textskortbl );
        textseninskor = (TextView) findViewById( R.id.skortext );
        btn_sign_out = findViewById( R.id.btn_sign_out );
        OyunaBasla = (Button)findViewById( R.id.OyunaBasla );
        marketegit = (Button)findViewById( R.id.AnasayfaGit );
        AltinText = (TextView)findViewById( R.id.AltinText );
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

                kullanicilar = new Kullanicilar( user.getUid(),kullaniciadi,null );
                String useradi = user.getDisplayName();
                kullaniciuid= user.getUid();

                Altinekleyaz();

                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "KullaniciAdi" ).setValue( useradi );

                toplamskor();
               // skortbl();

                //SET BUTTON SİGN OUT
                btn_sign_out.setEnabled( true );

                //Markete Git Butonu
                marketegit.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent( MainActivity.this,MarketActivity.class );
                        intent.putExtra( "kullaniciuid", kullaniciuid );
                        startActivity( intent );
                    }
                } );

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
                                                    anasayfagec.putExtra( "kullaniciuid", kullaniciuid);
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

    private void Altinekleyaz(){
        databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount()!=0){
                   if (dataSnapshot.getChildrenCount()!=0){
                        Altin = dataSnapshot.child( "Altin" ).getValue(Integer.class);
                        String Altinstr = String.valueOf( Altin );
                        Log.e( "Altınsnap", Altinstr );
                        AltinText.setText( Altinstr );
                    }
                }else {
                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Altin" ).setValue( 500 );
                    AltinText.setText( "500" );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }

    private void toplamskor(){
        databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Skorlar" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.getChildrenCount()!=0){
                    skorun = dataSnapshot.getValue(Integer.class);
                    String strskor = String.valueOf( skorun );
                    Log.e( "Skorun",strskor );
                    textseninskor.setText( strskor );

                }else {
                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Skorlar" ).setValue( 0 );
                    textseninskor.setText( "0" );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
       // databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Skorlar" ).setValue( skorun );



    }

    private void skortbl(){
        listskorlar = new ArrayList<>(  );
        kullanicilar = new Kullanicilar( kullaniciuid,kullaniciadi,listskorlar );
        databaseReference.child( "Kullanicilar" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            for (DataSnapshot skorlarsnap:dataSnapshot.getChildren()){

                int skrtint = 0;
                Integer skrtbl = (Integer) skorlarsnap.child( "Skorlar" ).getValue(Integer.class);
                String kllaniciadi = String.valueOf(skorlarsnap.child( "KullaniciAdi" ).getValue());

                kullanicilar = new Kullanicilar( kullaniciuid,kullaniciadi,listskorlar );

                kullanicilar.getKullaniciAdi().add( skrtint,kllaniciadi );
                kullanicilar.getSkor().add( skrtint,skrtbl );

              // skorlarary.add( kullanicilar );

                skrtint ++;
                Log.e( "Kullaniciadi", kllaniciadi );

            }

           // for (int a = 0; a == skorlarary.size(); a++){
                //int skrtint = 0;
              //
               // skrtint ++;
           // }


            textskor.setText("Skorlar:"+ "\n" +kullanicilar.getKullaniciAdi().get( 0 )+":"+kullanicilar.getSkor().get( 0 )+ "\n"+kullanicilar.getKullaniciAdi().get( 1 )+":"+kullanicilar.getSkor().get( 1 )+ "\n" );
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
                        sorucevap1 = (Integer) sorumetnisnap.child( "Cevap" ).getValue( Integer.class );
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
