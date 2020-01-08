package com.example.tahminoyunudeneme2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
    Button AnasayfaGec;
    String kullaniciuid;
    Integer Altin;
    TextView AltinText,AltinText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_market );

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        FirebaseApp.initializeApp( this );

        AltinText = (TextView)findViewById( R.id.AltinText );
        AltinText2 = (TextView)findViewById( R.id.AltinText2 );

        AnasayfaGec = (Button)findViewById( R.id.AnasayfaGit );

        AnasayfaGec.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( MarketActivity.this,MainActivity.class );
            }
        } );

        Intent intent = getIntent();
        kullaniciuid = intent.getStringExtra("kullaniciuid");

        Altinekleyaz();

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
                        AltinText2.setText( Altinstr );
                    }
                }else {
                    databaseReference.child( "Kullanicilar" ).child( kullaniciuid ).child( "Altin" ).setValue( 500 );
                    AltinText.setText( "500" );
                    AltinText2.setText( "500" );
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        } );

    }
}
