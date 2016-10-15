package com.google.android.gms.oem.bolti.keszlet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static android.R.attr.button;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.google.android.gms.oem.bolti.keszlet.R.id.messageTextView;
import static com.google.android.gms.oem.bolti.keszlet.R.id.termekValue;

public class DetailsActivity extends AppCompatActivity {

    private TextView markaValueTextView, termekValueTextView, mennyisegValueTextView;
    private String m_Text,m_Text2 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle("Termék információk");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

/*
        intent.putExtra("positionExtra",position);
        intent.putExtra("itemExtra",item);
        intent.putExtra("barcodeExtra",barcodeArray);
        intent.putExtra("dbExtra",mennyisegArray);
        intent.putExtra("markaExtra",termekArray);
        intent.putExtra("termekExtra",mergedArray);
  */


        Intent intent  = getIntent();
        //int position = intent.getIntExtra("positionExtra",0);
        //String item = intent.getIntExtra("itemExtra");
        Bundle extras = intent.getExtras();

        String item = extras.getString("itemExtra");
        int position = extras.getInt("positionExtra");
        String[] barcodeArray = extras.getStringArray("barcodeExtra");
        String[] mennyisegArray = extras.getStringArray("dbExtra");
        String[] markaArray = extras.getStringArray("markaExtra");
        String[] termekArray = extras.getStringArray("termekExtra");


        //String darab = extras.getString("dbExtra");
        //String marka = extras.getString("markaExtra");
        //String termek = extras.getString("termekExtra");

        //Toast.makeText(getApplicationContext(),position+","+item+";"+marka+","+termek+","+darab, Toast.LENGTH_LONG).show();


        //textView.setText(mTestArray[]);



        markaValueTextView = (TextView)findViewById(R.id.markaValue);
        markaValueTextView.setText(markaArray[position]);

        termekValueTextView = (TextView)findViewById(R.id.termekValue);
        termekValueTextView.setText(termekArray[position]);

        mennyisegValueTextView = (TextView)findViewById(R.id.mennyisegValue);
        mennyisegValueTextView.setText(mennyisegArray[position]);

        //View changeButton = findViewById(R.id.enter_mennyiseg);
        //changeButton.setVisibility(View.GONE);






        }

    public void enter_mennyisegClick(View view) {
        Toast toast= Toast.makeText(getApplicationContext(),"ENTER_MENNYISEG: PRESSED!", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0); toast.show();
    }

    public void onClick(View v) {
        //MENNYISÉG JAVÍTÁSA KÉZZEL
        if (v.getId() == R.id.enter_mennyiseg) {
            AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
            builder2.setTitle("Írd be a számolt mennyiséget:");
            // Set up the input
            final EditText input2 = new EditText(this);
            // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
            input2.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder2.setView(input2);
            // Set up the buttons
            builder2.setPositiveButton("Beküldés", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    m_Text2 = input2.getText().toString();
                    Toast toast= Toast.makeText(getApplicationContext(),m_Text2, Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0,0); toast.show();

                }
            });
            builder2.setNegativeButton("Vissza", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder2.show();
        }
    }

    //ActionBar Back ne nullázza az activity-t!!
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(getApplicationContext(),"Vissza:működik!", Toast.LENGTH_SHORT).show();
                super.onBackPressed();
                break;
        }
        return true;
    }
}
