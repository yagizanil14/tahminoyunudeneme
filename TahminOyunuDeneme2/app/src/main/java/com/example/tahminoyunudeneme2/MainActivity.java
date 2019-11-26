package com.example.tahminoyunudeneme2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int MY_REQUEST_CODE = 1000 ;
    Button btn_sign_out;
    List<AuthUI.IdpConfig> providers;
    Button OyunaBasla;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    Oda oda;
    OnlineKullanici onlineKullanici;
    ArrayList<String> odaKullanicilari;
    ArrayList<String> kullanicilar;


    ArrayList<Soru> sorular;

    private List<OnlineKullanici> list_Online=new ArrayList<>(  );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();


        FirebaseApp.initializeApp( this );

        btn_sign_out = findViewById( R.id.btn_sign_out );
        OyunaBasla = (Button)findViewById( R.id.OyunaBasla );

        databaseReference.child( "Kullanıcılar" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (list_Online.size()>0)
                    list_Online.clear();

                for (DataSnapshot postSnapShot:dataSnapshot.getChildren()){
                    OnlineKullanici online = postSnapShot.getValue(OnlineKullanici.class);
                    list_Online.add( online );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

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

    private void kullaniciEkle() {
         onlineKullanici = new OnlineKullanici( UUID.randomUUID().toString(),OyunaBasla.getText().toString());

        //Veri Tabanına Gönderme

        databaseReference.child( "Kullanıcılar" ).child( onlineKullanici.getUid() ).setValue( onlineKullanici );

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
                        kullaniciEkle();
                        OyunaBasla();

                            Intent anasayfagec = new Intent( MainActivity.this, Main2Activity.class );
                            startActivity( anasayfagec );

                    }
                } );

            }
            else {
                Toast.makeText( this, ""+response.getError().getMessage(), Toast.LENGTH_SHORT ).show();
            }
        }
    }

    private void musaitOdaBul(){
        oda = null;
        // Query ile musiatmi değeri true olan oda arayıp ilk sonucu vericek.
        Query query = databaseReference.child("Odalar").orderByChild("musaitmi").equalTo( true );
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot odaSnapshot: dataSnapshot.getChildren()) {
                    // Odada kendi kullanıcım olmasın
                     odaKullanicilari = new ArrayList<String>();
                    for(DataSnapshot kullanici : odaSnapshot.child("kullanicilar").getChildren()){
                        odaKullanicilari.add(kullanici.getValue( String.class ));
                    }
                    if(odaKullanicilari.contains( onlineKullanici.getUid() )){
                        oda = odaSnapshot.getValue( Oda.class );
                    }
                }

                if( oda != null  ){

                    odayiDoldur();
                } else {
                    sorulariGuncelle();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Hata", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        });

    };

    private void odaYarat(){
        // Firebase'den soruları alıp, yeni bir oda instance'ı yaratacak

        oda = new Oda( UUID.randomUUID().toString());
        oda.setSorular( sorular );
        oda.setMusaitmi( true );

        kullanicilar = new ArrayList<String>();
        kullanicilar.add( onlineKullanici.getUid() );//(Fazladan uid ekliyor)
        oda.setKullanicilar( kullanicilar );


        // Firebase'e odayı kaydedecek
        databaseReference.child( "Odalar" ).child( oda.getOdauid() ).setValue( oda );

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                if(!dataSnapshot.child( "musaitmi" ).getValue(Boolean.class)){
                    odayaGir( oda );
                }
                // ...
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Hata", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        databaseReference.child( "Odalar" ).child( oda.getOdauid() ).addValueEventListener(postListener);
    };

    private List<Soru> rastgeleSoruGetir(){
        // Rastgele Soru Seçip Getirecek (Şu An Boş)
        return null;
    }


    private void odayiDoldur(){
            oda.setMusaitmi( false );
            oda.getKullanicilar().add( onlineKullanici.getUid() );
            databaseReference.child( "Odalar" ).child( oda.getOdauid() ).setValue( oda );
            odayaGir( oda );
    }

    private void odayaGir(Oda oda){
        // FireBaseden Soruları Alıp Devam Edecek (Şu An Boş Doldur)
        Log.v("ODAYA GIR", oda.getOdauid());

    };

    private void sorulariGuncelle(){

        sorular = new ArrayList<Soru>();
        databaseReference.child( "Sorular" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot soruSnapshot: dataSnapshot.getChildren()) {
                    Soru soru = soruSnapshot.getValue(Soru.class);
                    Log.v("Soru", soru.getSoruMetni());
                    Log.v("Cevap", soru.getCevap().toString());
                    sorular.add( soru );
                }


            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
        odaYarat();
    };

    private void OyunaBasla(){
        musaitOdaBul();

    }
}
