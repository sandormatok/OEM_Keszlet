package com.google.android.gms.oem.bolti.keszlet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setTitle("Termék információk");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        int positionExtra = i.getIntExtra("positionExtra",0);
        int itemExtra = i.getIntExtra("itemExtra",0);
        Toast.makeText(getApplicationContext(),positionExtra+","+itemExtra, Toast.LENGTH_SHORT).show();
        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();
                super.onBackPressed();
                break;
        }
        return true;
    }

}
