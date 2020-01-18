package com.example.tahminoyunudeneme2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MarketActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference databaseReference;
    Button AnasayfaGec,satinal,karakterler,stadyumlar;
    String kullaniciuid;
    Integer Altin, karakter1,karakter2,karakter3,karakter4,karakter5,stadyum1,stadyum2,stadyum3,stadyum4,stadyum5,kullanimda,kullanimdastd;
    TextView AltinText,FiyatAltin,karakterad;
    ImageView imageana,image1,image2,image3,image4,image5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_market );

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        FirebaseApp.initializeApp( this );

        AltinText = (TextView)findViewById( R.id.AltinText );
        FiyatAltin = (TextView)findViewById( R.id.AltinText2 );
        karakterad = (TextView)findViewById( R.id.tvkarakterad );

        AnasayfaGec = (Button)findViewById( R.id.AnasayfaGit );
        satinal = (Button)findViewById( R.id.satinal );
        karakterler = (Button)findViewById( R.id.Karakterler );
        stadyumlar = (Button)findViewById( R.id.stadyumlar );

        imageana = (ImageView)findViewById( R.id.imageana );
        image1 = (ImageView)findViewById( R.id.image1 );
        image2 = (ImageView)findViewById( R.id.image2 );
        image3 = (ImageView)findViewById( R.id.image3 );
        image4 = (ImageView)findViewById( R.id.image4 );
        image5 = (ImageView)findViewById( R.id.image5 );

        kullanimda = 0;
        kullanimdastd = 0;


        AnasayfaGec.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MarketActivity.this,MainActivity.class );
                startActivity( intent );
            }
        } );

        Intent intent = getIntent();
        kullaniciuid = intent.getStringExtra("kullaniciuid");

        Altinekleyaz();
        karakterler();

        stadyumlar.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stadyumlar();
            }
        } );

        karakterler.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                karakterler();
            }
        } );

    }

    private void Altinekleyaz(){
        databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Altin" ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue()!=null){
                        Altin = dataSnapshot.getValue(Integer.class);
                        String Altinstr = String.valueOf( Altin );
                        Log.e( "Altınsnap", Altinstr );
                        AltinText.setText( Altinstr );

                    }

//                else {
//                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Altin" ).setValue( 500 );
//                    AltinText.setText( "500" );
//                    AltinText2.setText( "500" );
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }

    private void karakterler(){

        imageana.setImageResource( R.drawable.maraba7 );
        image2.setImageResource( R.drawable.maraba7 );
        image3.setImageResource( R.drawable.maraba7 );
        image4.setImageResource( R.drawable.maraba7 );
        image5.setImageResource( R.drawable.maraba7 );
        image1.setImageResource( R.drawable.maraba7 );
        karakterad.setText( "Karakter A" );
        FiyatAltin.setText( "" );
        satinal.setText( "Satın Al" );
        satinal.setBackgroundResource( R.drawable.rounded_button );
        satinal.setTextColor( Color.YELLOW );

            image1.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageana.setImageResource( R.drawable.maraba7 );
                    karakterad.setText( "Karakter 1" );
                    FiyatAltin.setText( "1500" );

                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Karakterler" ).child( "Karakter1" ).addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            karakter1 = dataSnapshot.getValue( Integer.class );

                            if (karakter1 == null) {
                                satinal.setText( "Satın Al" );
                                satinal.setBackgroundResource( R.drawable.rounded_button );
                                satinal.setTextColor( Color.YELLOW );
                            } else {
                                satinal.setText( "Mevcut" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );


                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            kullanimda =  dataSnapshot.getValue(Integer.class);

//                            Log.e( "kullanımda", kullanimda.toString() );

                            if (kullanimda == null || kullanimda == 0 )  {
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).setValue( 0 );
                            }

                            if (kullanimda == 1){
                                satinal.setText( "Kullanimda" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }else if (karakter1 != null){
                                satinal.setText( "Bunu Kullan" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );

                    satinal.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (Altin > 1500 && karakter1 == null) {
                                Altin = Altin - 1500;
                                satinal.setText( "Kullanımda" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Altin" ).setValue( Altin );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Karakterler" ).child( "Karakter1" ).setValue( 1 );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).setValue( 1 );
                            } else if (Altin < 1500 && karakter1 == null) {
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }

                            if (karakter1 != null && kullanimda != 1){

                                satinal.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        satinal.setText( "Kullanımda" );
                                        satinal.setBackgroundResource( R.drawable.false_satinal );
                                        satinal.setTextColor( Color.BLACK );
                                        databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).setValue( 1 );
                                    }
                                } );

                            }

                        }
                    } );


                }
            } );

            image2.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageana.setImageResource( R.drawable.maraba7 );
                    karakterad.setText( "Karakter 2" );
                    FiyatAltin.setText( "3000" );

                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Karakterler" ).child( "Karakter2" ).addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            karakter2 = dataSnapshot.getValue( Integer.class );

                            if (karakter2 == null) {
                                satinal.setText( "Satın Al" );
                                satinal.setBackgroundResource( R.drawable.rounded_button );
                                satinal.setTextColor( Color.YELLOW );
                            } else {
                                satinal.setText( "Mevcut" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );

                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            kullanimda =  dataSnapshot.getValue(Integer.class);

                            if (kullanimda == null || kullanimda == 0 )  {
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).setValue( 0 );
                            }
                            if (kullanimda == 2){
                                satinal.setText( "Kullanimda" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }else if (karakter2 != null){
                                satinal.setText( "Bunu Kullan" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );


                    satinal.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (Altin > 3000 && karakter2 == null) {
                                Altin = Altin - 3000;
                                satinal.setText( "Kullanımda" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Altin" ).setValue( Altin );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Karakterler" ).child( "Karakter2" ).setValue( 1 );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).setValue( 2 );
                            } else if (Altin < 3000 && karakter2 == null) {
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }

                            if (karakter2 != null && kullanimda != 2){

                                satinal.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        satinal.setText( "Kullanımda" );
                                        satinal.setBackgroundResource( R.drawable.false_satinal );
                                        satinal.setTextColor( Color.BLACK );
                                        databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).setValue( 2 );
                                    }
                                } );

                            }

                        }
                    } );


                }
            } );

            image3.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageana.setImageResource( R.drawable.maraba7 );
                    karakterad.setText( "Karakter 3" );
                    FiyatAltin.setText( "5000" );

                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Karakterler" ).child( "Karakter3" ).addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            karakter3 = dataSnapshot.getValue( Integer.class );

                            if (karakter3 == null) {
                                satinal.setText( "Satın Al" );
                                satinal.setBackgroundResource( R.drawable.rounded_button );
                                satinal.setTextColor( Color.YELLOW );
                            } else {
                                satinal.setText( "Mevcut" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );

                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            kullanimda =  dataSnapshot.getValue(Integer.class);

                            if (kullanimda == null || kullanimda == 0 )  {
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).setValue( 0 );
                            }

                            if (kullanimda == 3){
                                satinal.setText( "Kullanimda" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }else if ( karakter3 != null) {
                                satinal.setText( "Bunu Kullan" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );

                    satinal.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (Altin > 5000 && karakter3 == null) {
                                Altin = Altin - 5000;
                                satinal.setText( "Mevcut" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Altin" ).setValue( Altin );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Karakterler" ).child( "Karakter3" ).setValue( 1 );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).setValue( 3 );
                            } else if (Altin < 5000 && karakter3 == null) {
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }

                            if (karakter3 != null && kullanimda != 3) {
                                satinal.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        satinal.setText( "Kullanımda" );
                                        satinal.setBackgroundResource( R.drawable.false_satinal );
                                        satinal.setTextColor( Color.BLACK );
                                        databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).setValue( 3 );
                                    }
                                } );
                            }

                        }
                    } );


                }
            } );

            image4.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageana.setImageResource( R.drawable.maraba7 );
                    karakterad.setText( "Karakter 4" );
                    FiyatAltin.setText( "9000" );

                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Karakterler" ).child( "Karakter4" ).addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            karakter4 = dataSnapshot.getValue( Integer.class );

                            if (karakter4 == null) {
                                satinal.setText( "Satın Al" );
                                satinal.setBackgroundResource( R.drawable.rounded_button );
                                satinal.setTextColor( Color.YELLOW );
                            } else {
                                satinal.setText( "Mevcut" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );

                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            kullanimda =  dataSnapshot.getValue(Integer.class);

                            if (kullanimda == null || kullanimda == 0 )  {
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).setValue( 0 );
                            }

                            if (kullanimda == 4){
                                satinal.setText( "Kullanimda" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }else if (karakter4 != null){
                                satinal.setText( "Bunu Kullan" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );

                    satinal.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (Altin > 9000 && karakter4 == null) {
                                Altin = Altin - 9000;
                                satinal.setText( "Mevcut" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Altin" ).setValue( Altin );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Karakterler" ).child( "Karakter4" ).setValue( 1 );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).setValue( 4 );
                            } else if (Altin < 9000 && karakter4 == null) {
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }

                            if (karakter4 != null && kullanimda != 4) {
                                satinal.setOnClickListener( new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        satinal.setText( "Kullanımda" );
                                        satinal.setBackgroundResource( R.drawable.false_satinal );
                                        satinal.setTextColor( Color.BLACK );
                                        databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).setValue( 4 );
                                    }
                                } );
                            }

                        }
                    } );


                }
            } );

            image5.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imageana.setImageResource( R.drawable.maraba7 );
                    karakterad.setText( "Karakter 5" );
                    FiyatAltin.setText( "15000" );

                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Karakterler" ).child( "Karakter5" ).addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            karakter5 = dataSnapshot.getValue( Integer.class );

                            if (karakter5 == null) {
                                satinal.setText( "Satın Al" );
                                satinal.setBackgroundResource( R.drawable.rounded_button );
                                satinal.setTextColor( Color.YELLOW );
                            } else {
                                satinal.setText( "Mevcut" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );

                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).addListenerForSingleValueEvent( new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            kullanimda =  dataSnapshot.getValue(Integer.class);

                            if (kullanimda == null || kullanimda == 0 )  {
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).setValue( 0 );
                            }

                            if (kullanimda == 5){
                                satinal.setText( "Kullanimda" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }else if (karakter5 != null){
                                satinal.setText( "Bunu Kullan" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    } );

                    satinal.setOnClickListener( new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (Altin > 15000 && karakter5 == null) {
                                Altin = Altin - 15000;
                                satinal.setText( "Mevcut" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Altin" ).setValue( Altin );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Karakterler" ).child( "Karakter5" ).setValue( 1 );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).setValue( 5 );
                            } else if (Altin < 15000 && karakter5 == null) {
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                            }

                            if (karakter5 != null && kullanimda != 5) {
                                satinal.setText( "Kullanımda" );
                                satinal.setBackgroundResource( R.drawable.false_satinal );
                                satinal.setTextColor( Color.BLACK );
                                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimda" ).setValue( 5 );
                            }

                        }
                    } );


                }
            } );

    }

    private void stadyumlar(){
        imageana.setImageResource( R.drawable.maraba9 );
        image2.setImageResource( R.drawable.maraba9 );
        image3.setImageResource( R.drawable.maraba9 );
        image4.setImageResource( R.drawable.maraba9 );
        image5.setImageResource( R.drawable.maraba9 );
        image1.setImageResource( R.drawable.maraba9 );
        karakterad.setText( "Stadyum A" );
        FiyatAltin.setText( "" );
        satinal.setText( "Satın Al" );
        satinal.setBackgroundResource( R.drawable.rounded_button );
        satinal.setTextColor( Color.YELLOW );


        image1.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageana.setImageResource( R.drawable.maraba9 );
                karakterad.setText( "Stadyum 1" );
                FiyatAltin.setText( "2500" );

                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Stadyumlar" ).child( "Stadyum1" ).addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        stadyum1 = dataSnapshot.getValue( Integer.class );

                        if (stadyum1 == null) {
                            satinal.setText( "Satın Al" );
                            satinal.setBackgroundResource( R.drawable.rounded_button );
                            satinal.setTextColor( Color.YELLOW );
                        } else {
                            satinal.setText( "Mevcut" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        kullanimdastd =  dataSnapshot.getValue(Integer.class);

                        if (kullanimdastd == null || kullanimdastd == 0 )  {
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).setValue( 0 );
                        }

                        if (kullanimdastd == 1){
                            satinal.setText( "Kullanimda" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                        }else if (stadyum1 != null){
                            satinal.setText( "Bunu Kullan" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

                satinal.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Altin > 2500 && stadyum1 == null) {
                            Altin = Altin - 2500;
                            satinal.setText( "Kullanımda" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Altin" ).setValue( Altin );
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Stadyumlar" ).child( "Stadyum1" ).setValue( 1 );
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).setValue( 1 );
                        } else if (Altin < 2500 && stadyum1 == null) {
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                        }

                        if (stadyum1 != null && kullanimdastd != 1){

                            satinal.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    satinal.setText( "Kullanımda" );
                                    satinal.setBackgroundResource( R.drawable.false_satinal );
                                    satinal.setTextColor( Color.BLACK );
                                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).setValue( 1 );
                                }
                            } );

                        }
                    }
                } );


            }
        } );

        image2.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageana.setImageResource( R.drawable.maraba9 );
                karakterad.setText( "Stadyum 2" );
                FiyatAltin.setText( "5000" );

                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Stadyumlar" ).child( "Stadyum2" ).addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        stadyum2 = dataSnapshot.getValue( Integer.class );

                        if (stadyum2 == null) {
                            satinal.setText( "Satın Al" );
                            satinal.setBackgroundResource( R.drawable.rounded_button );
                            satinal.setTextColor( Color.YELLOW );
                        } else {
                            satinal.setText( "Mevcut" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        kullanimdastd =  dataSnapshot.getValue(Integer.class);

                        if (kullanimdastd == null || kullanimdastd == 0 )  {
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).setValue( 0 );
                        }

                        if (kullanimdastd == 2){
                            satinal.setText( "Kullanimda" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                        }else if (stadyum2 != null){
                            satinal.setText( "Bunu Kullan" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

                satinal.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Altin > 5000 && stadyum2 == null) {
                            Altin = Altin - 5000;
                            satinal.setText( "Mevcut" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Altin" ).setValue( Altin );
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Stadyumlar" ).child( "Stadyum2" ).setValue( 1 );
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).setValue( 2 );
                        } else if (Altin < 5000 && stadyum2 == null) {
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                        }

                        if (stadyum2 != null && kullanimdastd != 2){

                            satinal.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    satinal.setText( "Kullanımda" );
                                    satinal.setBackgroundResource( R.drawable.false_satinal );
                                    satinal.setTextColor( Color.BLACK );
                                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).setValue( 2 );
                                }
                            } );

                        }
                    }
                } );


            }
        } );

        image3.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageana.setImageResource( R.drawable.maraba9 );
                karakterad.setText( "Stadyum 3" );
                FiyatAltin.setText( "7500" );

                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Stadyumlar" ).child( "Stadyum3" ).addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        stadyum3 = dataSnapshot.getValue( Integer.class );

                        if (stadyum3 == null) {
                            satinal.setText( "Satın Al" );
                            satinal.setBackgroundResource( R.drawable.rounded_button );
                            satinal.setTextColor( Color.YELLOW );
                        } else {
                            satinal.setText( "Mevcut" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        kullanimdastd =  dataSnapshot.getValue(Integer.class);

                        if (kullanimdastd == null || kullanimdastd == 0 )  {
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).setValue( 0 );
                        }

                        if (kullanimdastd == 3){
                            satinal.setText( "Kullanimda" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                        }else if (stadyum3 != null){
                            satinal.setText( "Bunu Kullan" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

                satinal.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Altin > 7500 && stadyum3 == null) {
                            Altin = Altin - 7500;
                            satinal.setText( "Mevcut" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Altin" ).setValue( Altin );
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Stadyumlar" ).child( "Stadyum3" ).setValue( 1 );
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).setValue( 3 );
                        } else if (Altin < 7500 && stadyum3 == null) {
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                        }

                        if (stadyum3 != null && kullanimdastd != 3){

                            satinal.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    satinal.setText( "Kullanımda" );
                                    satinal.setBackgroundResource( R.drawable.false_satinal );
                                    satinal.setTextColor( Color.BLACK );
                                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).setValue( 3 );
                                }
                            } );

                        }
                    }
                } );


            }
        } );

        image4.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageana.setImageResource( R.drawable.maraba9 );
                karakterad.setText( "Stadyum 4" );
                FiyatAltin.setText( "2500" );

                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Stadyumlar" ).child( "Stadyum4" ).addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        stadyum4 = dataSnapshot.getValue( Integer.class );

                        if (stadyum4 == null) {
                            satinal.setText( "Satın Al" );
                            satinal.setBackgroundResource( R.drawable.rounded_button );
                            satinal.setTextColor( Color.YELLOW );
                        } else {
                            satinal.setText( "Mevcut" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        kullanimdastd =  dataSnapshot.getValue(Integer.class);

                        if (kullanimdastd == null || kullanimdastd == 0 )  {
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).setValue( 0 );
                        }

                        if (kullanimdastd == 4){
                            satinal.setText( "Kullanimda" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                        }else if (stadyum4 != null){
                            satinal.setText( "Bunu Kullan" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

                satinal.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Altin > 10000 && stadyum4 == null) {
                            Altin = Altin - 10000;
                            satinal.setText( "Mevcut" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Altin" ).setValue( Altin );
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Stadyumlar" ).child( "Stadyum4" ).setValue( 1 );
                        } else if (Altin < 10000 && stadyum4 == null) {
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                        }

                        if (stadyum4 != null && kullanimdastd != 4){

                            satinal.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    satinal.setText( "Kullanımda" );
                                    satinal.setBackgroundResource( R.drawable.false_satinal );
                                    satinal.setTextColor( Color.BLACK );
                                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).setValue( 4 );
                                }
                            } );

                        }
                    }
                } );


            }
        } );

        image5.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageana.setImageResource( R.drawable.maraba9 );
                karakterad.setText( "Stadyum 5" );
                FiyatAltin.setText( "25000" );

                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Stadyumlar" ).child( "Stadyum5" ).addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        stadyum5 = dataSnapshot.getValue( Integer.class );

                        if (stadyum5 == null) {
                            satinal.setText( "Satın Al" );
                            satinal.setBackgroundResource( R.drawable.rounded_button );
                            satinal.setTextColor( Color.YELLOW );
                        } else {
                            satinal.setText( "Mevcut" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

                databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).addListenerForSingleValueEvent( new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        kullanimdastd =  dataSnapshot.getValue(Integer.class);

                        if (kullanimdastd == null || kullanimdastd == 0 )  {
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).setValue( 0 );
                        }

                        if (kullanimdastd == 5){
                            satinal.setText( "Kullanimda" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                        }else if (stadyum5 != null){
                            satinal.setText( "Bunu Kullan" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                } );

                satinal.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (Altin > 25000 && stadyum5 == null) {
                            Altin = Altin - 25000;
                            satinal.setText( "Mevcut" );
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Altin" ).setValue( Altin );
                            databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Stadyumlar" ).child( "Stadyum5" ).setValue( 1 );
                        } else if (Altin < 25000 && stadyum5 == null) {
                            satinal.setBackgroundResource( R.drawable.false_satinal );
                            satinal.setTextColor( Color.BLACK );
                        }

                        if (stadyum5 != null && kullanimdastd != 5) {

                            satinal.setOnClickListener( new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    satinal.setText( "Kullanımda" );
                                    satinal.setBackgroundResource( R.drawable.false_satinal );
                                    satinal.setTextColor( Color.BLACK );
                                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Kullanimdastd" ).setValue( 5 );
                                }
                            } );
                        }
                    }
                } );


            }
        } );

    }
}
