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
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Main2Activity extends AppCompatActivity {

    EditText Cevap;
    TextView sorumetni, sayac;
    Button Cevabi_Gonder, Anasayfaya_git;
    FirebaseDatabase database;
    DatabaseReference databaseReference;

    CountDownTimer countDownTimer;
    boolean yenisorugectf, oyunBasladi;
    String odauid,kullaniciuid;
    ArrayList<Sorular> sorular = new ArrayList<>(  );
    Integer sorunumarasi;


    @Override
   protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main2 );

        //FİREBASE
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        FirebaseApp.initializeApp( this );

        sorumetni = (TextView) findViewById( R.id.SoruMetni );
        sayac = (TextView)findViewById( R.id.sayac );
        Cevap = (EditText)findViewById( R.id.Cevap );
        Cevabi_Gonder = (Button)findViewById( R.id.Cevabı_Gonder );
        Anasayfaya_git = (Button)findViewById( R.id.anasayfa );
        kullaniciuid = MainActivity.kullanicilar.getUid();


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

        odauidal();
        sorularial();



    }

    private void cevapgonder() {
        int kcevap = Integer.parseInt(Cevap.getText().toString()) ;
        databaseReference.child( "Odalar" ).child( odauid ).child( "cevaplar" ).child( sorunumarasi.toString() ).child( kullaniciuid ).setValue( kcevap );
        soruyubekle();
    }

    private void sorularial(){
       databaseReference.child( "Odalar" ).child(odauid).child( "odadakisorular" ).addListenerForSingleValueEvent( new ValueEventListener() {
           @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot sorusnap:dataSnapshot.getChildren()){
                    Sorular soru = sorusnap.getValue(Sorular.class);

                    sorular.add( soru );
                    Log.e( "soru metin", soru.getSorumetni() );
                    Log.e( "soru cevap", soru.getSorucevap().toString() );
//                  Sorular  soru = new Sorular( null,null );
//                    sorumetin= (String) sorusnap.child( "sorumetni" ).getValue();
//                    Log.e( "sorular yazılıyor","sorular yazılıyor "+sorumetin);
//                    sorucevabi = (int) sorusnap.child( "sorucevap" ).getValue(Integer.class);
//                    Log.e( "sorular yazılıyor","soru cevabı yazılıyor "+sorucevabi);
//                    sorular.setSorumetni( sorumetin );
//                    sorular.setSorucevap( sorucevabi );
//                    arysorular.add( sorular );
                }
                oyunubekle();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    private void odauidal(){
        Intent intent = getIntent();
        odauid = intent.getStringExtra("odauid");
    }

    private void oyunubekle(){
        databaseReference.child( "Odalar" ).child(odauid).child("musaitmi").addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
             boolean musaitmi =  dataSnapshot.getValue(boolean.class);
                if (!musaitmi){
                    oyunabasla();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    private void oyunabasla(){
        if(oyunBasladi){
            return;
        }
        oyunBasladi = true;
        soruyugoster( 0 );
        Log.e( "baslıyor","baslıyor" );
    }

    private void soruyugoster(Integer index){
        yenisorugectf = false;
        Log.e( "soruno", index.toString() );
        Log.e( "sorusize", String.valueOf(sorular.size()) );
        if (sorular.size() == index){
            oyunsonunugoster();
            return;
        }
      sorumetni.setText( sorular.get( index ).getSorumetni() );
      zamanibaslat( index );
      sorunumarasi=index;
    }

    private void cevaplarial(){
        databaseReference.child( "Odalar" ).child( odauid ).child( "cevaplar" ).addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Integer benimSkor = 0;
                Integer onunSkor = 0;
                for (DataSnapshot sorusnap:dataSnapshot.getChildren()){
                    Integer cevap = sorular.get(Integer.parseInt( sorusnap.getKey() )).getSorucevap();
                    Integer benimCevap = 0, onunCevabi = 0;
                    for (DataSnapshot cevapsnap:sorusnap.getChildren()){
                        if(cevapsnap.getKey() == kullaniciuid){
                            benimCevap = Integer.parseInt( cevapsnap.getValue().toString() );
                        }else {
                            onunCevabi = Integer.parseInt( cevapsnap.getValue().toString() );
                        }
                    }
                    if(Math.abs(benimCevap - cevap) < Math.abs(onunCevabi - cevap)){
                        benimSkor++;
                    } else if(Math.abs(benimCevap - cevap) > Math.abs(onunCevabi - cevap)){
                        onunSkor++;
                    } else {

                    }
                }
                Log.e("benimSkor", benimSkor.toString());
                Log.e("onunSkor", onunSkor.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }

    private void oyunsonunugoster() {

        cevaplarial();
        sayac.setVisibility( View.INVISIBLE );
        Cevap.setVisibility( View.INVISIBLE );
        Cevabi_Gonder.setVisibility( View.INVISIBLE );

    }

    private void zamanibaslat(final Integer index){
         countDownTimer = new CountDownTimer(20000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                sayac.setText(String.valueOf(millisUntilFinished/1000));
                if (yenisorugectf){
                    Log.e( "onTick Çalıştı", "OnTick Çalıştı" );
                    countDownTimer.cancel();
                    soruyugoster( index+1 );
                }
            }

            @Override
            public void onFinish() {
                Log.e( "onFnish","OnFnish metodu çalıştı" );
                soruyugoster( index+1 );
            }
        }.start();
    }

    private void soruyubekle(){
        databaseReference.child( "Odalar" ).child( odauid ).child( "cevaplar" ).child( sorunumarasi.toString() ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

             if ( dataSnapshot.getChildrenCount() == 2){
                 yenisorugectf = true;
                 Log.e( "yenisorugectf","lalal" );
             }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );
    }


//    Odalar odalar;
//    Sorular sorular;
//    CountDownTimer countDownTimer;
//    String sorumetin,sorucevabistr,odauid,kullaniciuid,kullanicicevap,rakipcevap,datasnapstr,datasnapstr2;
//    Integer sorucevabi;
//    Cevaplar cevaplar;
//    ArrayList<Sorular> arysorular;
//    ArrayList<String> aryodakullanicilari,arycevaplar2;
//    ArrayList<Cevaplar> arycevaplar;
//    int senincvp,rakipcvp,intkcvp,intcvpsnp,ksonuc,rsonuc,datasnapint,intkcevap,hak;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate( savedInstanceState );
//        setContentView( R.layout.activity_main2 );
//
//
//        //FİREBASE
//        database = FirebaseDatabase.getInstance();
//        databaseReference = database.getReference();
//        FirebaseApp.initializeApp( this );
//        hak = 1;
//
//
//        sorumetni = (TextView) findViewById( R.id.SoruMetni );
//        sayac = (TextView)findViewById( R.id.sayac );
//        Cevap = (EditText)findViewById( R.id.Cevap );
//        Cevabi_Gonder = (Button)findViewById( R.id.Cevabı_Gonder );
//        Anasayfaya_git = (Button)findViewById( R.id.anasayfa );
//
//
//
//        arycevaplar2 = new ArrayList<>(  );
//
//        aryodakullanicilari = new ArrayList<>(  );
//
//        cevaplar = new Cevaplar();
//        cevaplar.setCevaplar1( "1" );
//        cevaplar.setCevaplar2( "1" );
//
//        arycevaplar = new ArrayList<>(  );
//        arycevaplar.add( cevaplar );
//
//        sorular = new Sorular(  );
//
//        arysorular = new ArrayList<Sorular>(  );
//        arysorular.add( sorular );
//
//
//
//        Intent intent = getIntent();
//        odauid = intent.getStringExtra("odauid");
//        kullaniciuid = intent.getStringExtra( "kullaniciuid" );
//        aryodakullanicilari = intent.getStringArrayListExtra( "odakullanicilari" );
//
//        databaseReference.child( "Odalar" ).child(odauid).child( "cevaplar" ).setValue( arycevaplar );
//
//        databaseReference.child( "Odalar" ).child(odauid).child( "odadakisorular" ).addListenerForSingleValueEvent( new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot sorusnap:dataSnapshot.getChildren()){
//                    sorular = new Sorular( null,null );
//                    sorumetin= (String) sorusnap.child( "sorumetni" ).getValue();
//                    Log.e( "sorular yazılıyor","sorular yazılıyor "+sorumetin);
//                    sorucevabi = (int) sorusnap.child( "sorucevap" ).getValue(Integer.class);
//                    Log.e( "sorular yazılıyor","soru cevabı yazılıyor "+sorucevabi);
//                    sorular.setSorumetni( sorumetin );
//                    sorular.setSorucevap( sorucevabi );
//                    arysorular.add( sorular );
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        } );
//
//       sorumetni.setText(  arysorular.get( 0 ).getSorumetni() );
//
//        countDownTimer=new CountDownTimer(20000,1000)//30000 kısmı kaç ms saniye olacağını gösterir.
//        {
//            @Override
//            public void onTick(long millisUntilFinished) {
//                sayac.setText(String.valueOf(millisUntilFinished/1000));
//                //Milisaniyeyi 1000 e böldüğümüzde saniyeyi buluyoruz.Yani 30 saniye.
//            }
//
//            @Override
//            //onFinish metodu süre bittiğinde olacak olaylar için kullanılır.
//            public void onFinish() {
//
//                hak ++;
//
//                databaseReference.child( "Odalar" ).child( odauid ).child( "cevaplar" ).addListenerForSingleValueEvent( new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for (DataSnapshot cevapsnap:dataSnapshot.getChildren()){
//                            intcvpsnp =  cevapsnap.child( "cevaplar1" ).getValue(Integer.class);
//                             intkcvp =Integer.valueOf( kullanicicevap );
//                            if (intcvpsnp == intkcvp ){
//                                intkcvp = senincvp;
//                                Log.e( "kullanici cevap",kullanicicevap );
//
//                            }else {
//                                rakipcevap = datasnapstr2;
//                                rakipcvp = Integer.valueOf( rakipcevap );
//                                Log.e( "rakip cevap",rakipcevap );
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                } );
//
//               // oyunhesapla();
//            }
//        }.start();
//        //start metodu ile ise sayacımızı başlatıyoruz.
//
//        Cevabi_Gonder.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                cevapgonder();
//
//            }
//        } );
//
//
//        Anasayfaya_git.setOnClickListener( new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent anasayfagit = new Intent( Main2Activity.this,MainActivity.class );
//                startActivity( anasayfagit );
//            }
//        } );
//
//
//
//    }
//
//    private void oyunhesapla() {
//        ksonuc = (intkcvp-sorucevabi);
//        rsonuc = (rakipcvp-sorucevabi);
//
//
//        if (ksonuc<0){
//           ksonuc= ksonuc*-1;
//        }else if (rsonuc<0){
//            rsonuc = rsonuc*-1;
//        }
//
//        if (ksonuc<rsonuc){
//            Log.e( "Sen Kazandın!",kullanicicevap );
//        }else {
//            Log.e( "Rakip Kazandı",rakipcevap );
//        }
//
//    }
//
//    private void cevapgonder() {
//        if (hak >0) {
//            hak--;
//            kullanicicevap = Cevap.getText().toString();
//            intkcevap = Integer.valueOf( kullanicicevap );
//
//
//            databaseReference.child( "Odalar" ).child( odauid ).child( "cevaplar" ).addListenerForSingleValueEvent( new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    for (DataSnapshot cevapsnaps : dataSnapshot.getChildren()) {
//                        datasnapstr = (String) cevapsnaps.child( "cevaplar1" ).getValue();
//                        datasnapint = Integer.valueOf( datasnapstr );
//                        Log.e( "cevaplar yazılıyor", "cevaplar yazılıyor " + datasnapstr );
//
//                        if (datasnapint == 1) {
//                            arycevaplar = new ArrayList<>();
//                            cevaplar.setCevaplar1( kullanicicevap );
//                            arycevaplar.add( cevaplar );
//                            databaseReference.child( "Odalar" ).child( odauid ).child( "cevaplar" ).setValue( arycevaplar );
//                        } else {
//                            arycevaplar = new ArrayList<>();
//                            cevaplar.setCevaplar2( kullanicicevap );
//                            cevaplar.setCevaplar1( datasnapstr );
//                            arycevaplar.add( cevaplar );
//                            databaseReference.child( "Odalar" ).child( odauid ).child( "cevaplar" ).setValue( arycevaplar );
//                        }
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            } );
//
//        }
//
//    }
}

