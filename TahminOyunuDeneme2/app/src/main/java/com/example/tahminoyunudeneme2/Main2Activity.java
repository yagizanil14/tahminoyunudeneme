package com.example.tahminoyunudeneme2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Main2Activity extends AppCompatActivity {

    EditText Cevap;
    TextView sorumetni, sayac;
    Button Cevabi_Gonder, Anasayfaya_git;
    FirebaseDatabase database;
    DatabaseReference databaseReference;
    Odalar odalar;
    Sorular sorular;
    CountDownTimer countDownTimer;
    String sorumetin,sorucevabistr,odauid,kullaniciuid,kullanicicevap,rakipcevap,datasnapstr,datasnapstr2;
    Integer sorucevabi;
    Cevaplar cevaplar;
    ArrayList<Sorular> arysorular;
    ArrayList<String> aryodakullanicilari,arycevaplar2;
    ArrayList<Cevaplar> arycevaplar;
    int senincvp,rakipcvp,intkcvp,intcvpsnp,ksonuc,rsonuc,a,datasnapint,intkcevap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );


        //FİREBASE
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        FirebaseApp.initializeApp( this );
        a = -1;

        sorumetni = (TextView) findViewById( R.id.SoruMetni );
        sayac = (TextView)findViewById( R.id.sayac );
        Cevap = (EditText)findViewById( R.id.Cevap );
        Cevabi_Gonder = (Button)findViewById( R.id.Cevabı_Gonder );
        Anasayfaya_git = (Button)findViewById( R.id.anasayfa );


        arycevaplar2 = new ArrayList<>(  );

        aryodakullanicilari = new ArrayList<>(  );

        cevaplar = new Cevaplar();
        cevaplar.setCevaplar1( "1" );
        cevaplar.setCevaplar2( "1" );

        arycevaplar = new ArrayList<>(  );
        arycevaplar.add( cevaplar );




        arysorular = new ArrayList<Sorular>(  );
        arysorular.add( sorular );







        Intent intent = getIntent();
        odauid = intent.getStringExtra("odauid");
        kullaniciuid = intent.getStringExtra( "kullaniciuid" );
        aryodakullanicilari = intent.getStringArrayListExtra( "odakullanicilari" );

        databaseReference.child( "Odalar" ).child(odauid).child( "cevaplar" ).setValue( arycevaplar );

       // cevaplar.setCevaplar1( null );

        //databaseReference.child( odauid ).child( "cevaplar" ).setValue( cevaplar );





        countDownTimer=new CountDownTimer(20000,1000)//30000 kısmı kaç ms saniye olacağını gösterir.
        {
            @Override
            public void onTick(long millisUntilFinished) {
                sayac.setText(String.valueOf(millisUntilFinished/1000));
                //Milisaniyeyi 1000 e böldüğümüzde saniyeyi buluyoruz.Yani 30 saniye.
            }

            @Override
            //onFinish metodu süre bittiğinde olacak olaylar için kullanılır.
            public void onFinish() {

                databaseReference.child( "Odalar" ).child( odauid ).child( "cevaplar" ).child( "cevaplar" ).addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot cevapsnap:dataSnapshot.getChildren()){
                             intcvpsnp = Integer.valueOf( cevapsnap.getValue().toString() );
                             intkcvp =Integer.valueOf( kullanicicevap );
                            if (intcvpsnp == intkcvp ){
                                intkcvp = senincvp;
                                Log.e( "kullanici cevap",kullanicicevap );

                            }else {
                                rakipcevap = cevapsnap.getValue().toString();
                                rakipcvp = Integer.valueOf( rakipcevap );
                                Log.e( "rakip cevap",rakipcevap );
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

                databaseReference.child( "Odalar" ).child( odauid ).child( "cevaplar" ).child( "cevaplar1" ).addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot cevapsnap:dataSnapshot.getChildren()){
                            intcvpsnp = Integer.valueOf( cevapsnap.getValue().toString() );
                            intkcvp =Integer.valueOf( kullanicicevap );
                            if (intcvpsnp == intkcvp ){
                                intkcvp = senincvp;
                                Log.e( "kullanici cevap",kullanicicevap );

                            }else {
                                rakipcevap = cevapsnap.getValue().toString();
                                rakipcvp = Integer.valueOf( rakipcevap );
                                Log.e( "rakip cevap",rakipcevap );
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );



                databaseReference.child( "Odalar" ).child(odauid).child( "odadakisorular" ).addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot sorusnap:dataSnapshot.getChildren()){
                            sorumetin= (String) sorusnap.child( "sorumetni" ).getValue();
                            Log.e( "sorular yazılıyor","sorular yazılıyor "+sorumetin);
                           sorucevabistr = (String) sorusnap.child( "sorucevap" ).getValue();
                            sorucevabi = Integer.parseInt( sorucevabistr );
                            Log.e( "sorular yazılıyor","soru cevabı yazılıyor "+sorucevabi);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );
            }
        }.start();
        //start metodu ile ise sayacımızı başlatıyoruz.


        //FİREBASEDEN ODA BİLGİLERİ OKUNACAK SORULAR ALINACAK
        //CEVAPLAR İŞLENECEK ARADAKİ FARK FİREBASEYE GÖNDERİLECEK
        //İKİ FARKTA ALINACAK CEVABA EN YAKIN OLAN OYUNU KAZANDI YAZISI EKRANA ÇIKACAK
        //BU DÖNGÜ ŞEKLİNDE HER DÖNGÜYE SÜRE TANIMLANACAK 10 SORU BİTENE KADAR DEVAM EDECEK


        Cevabi_Gonder.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cevapgonder();

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

    private void oyunhesapla() {
        ksonuc = (intkcvp-sorucevabi);
        rsonuc = (rakipcvp-sorucevabi);


        if (ksonuc<0){
           ksonuc= ksonuc*a;
        }else if (rsonuc<0){
            rsonuc = rsonuc*a;
        }

        if (ksonuc<rsonuc){
            Log.e( "Sen Kazandın!",kullanicicevap );
        }else {
            Log.e( "Rakip Kazandı",rakipcevap );
        }

    }

    private void cevapgonder() {

        kullanicicevap = Cevap.getText().toString();
        intkcevap = Integer.valueOf( kullanicicevap );


        databaseReference.child( "Odalar" ).child(odauid).child( "cevaplar" ).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot cevapsnaps:dataSnapshot.getChildren()){
                    datasnapstr= (String) cevapsnaps.child( "cevaplar1" ).getValue();
                    datasnapint = Integer.valueOf( datasnapstr );
                    Log.e( "cevaplar yazılıyor","cevaplar yazılıyor "+datasnapstr);

                    if (datasnapint == 1){
                        arycevaplar = new ArrayList<>(  );
                        cevaplar.setCevaplar1( kullanicicevap );
                        arycevaplar.add( cevaplar );
                        databaseReference.child( "Odalar" ).child(odauid).child( "cevaplar" ).setValue( arycevaplar );
                    }else {
                        arycevaplar = new ArrayList<>(  );
                        cevaplar.setCevaplar2( kullanicicevap );
                        cevaplar.setCevaplar1( datasnapstr );
                        arycevaplar.add( cevaplar );
                        databaseReference.child( "Odalar" ).child(odauid).child( "cevaplar" ).setValue( arycevaplar );
                    }
                  /*  Log.e( "sorular yazılıyor","sorular yazılıyor "+datasnapstr);
                    datasnapstr2 = (String) cevapsnaps.child( "cevaplar2" ).getValue();
                    //sorucevabi = Integer.parseInt( sorucevabistr );
                    Log.e( "sorular yazılıyor","soru cevabı yazılıyor "+datasnapstr2);*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );



    }
}

