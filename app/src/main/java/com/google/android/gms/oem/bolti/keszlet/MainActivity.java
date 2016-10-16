/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.gms.oem.bolti.keszlet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.R.attr.id;
//import static com.google.android.gms.common.api.Status.ss;
import static com.google.android.gms.oem.bolti.keszlet.BarcodeCaptureActivity.barcode3;
import static com.google.android.gms.oem.bolti.keszlet.R.id.itemListView;

//begin
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus, useFlash, fastMode;
    private TextView vonalkodTextView, messageTextView;
    String barcode = "barcode";

    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "BarcodeMain";

    String url, boltnev2;
    String boltnev, teruletnev = "Raktár";

    String deviceID, itemViewList;
    Integer itemPosition;

    private String m_Text,m_Text2 = "";
    private String manualInput = "NO";

    //Terület választó Spinner
    String teruletvalaszto[] = { "Raktár", "Csata Edit", "Csata Mária", "Kacsó Évi", "Sándor Zsuzsi", "Süle Ildikó", "Tamás Ildikó", "Zilahi Anita", "Összes üzlet" };

    //Boltcsoportok Spinner2
    String raktar[] = { "Raktár" };
    String osszesbolt[] = {"Erzsébet 1.", "Alkotás", "Bartók 71.", "Békéscsaba 1.", "Békéscsaba 2.", "Bicske", "Csepel", "Csongrád", "Damjanich", "Debrecen 1.", "Debrecen 2.", "Dékán", "Dunaújváros", "Erzsébet 2.", "Fehérvári út", "Ferenc Krt.", "Garay", "Gyula", "Harminckettesek", "Hattyú", "K?rösi", "Karcag", "Kecskemét 1.", "Kecskemét 2.", "Kisk?rös", "Környe", "Mester u.", "Mez?túr", "Nagyk?rös", "Orosháza", "Oroszlány 1.", "Oroszlány 2.", "Pablo Neruda", "Paks", "Pécs 1.", "Pécs 2", "Piliscsaba", "Püspökladány", "Szeged 1.", "Szeged 2.", "Tatabánya 1.", "Tatabánya 2.", "Tatabánya 3.", "Teréz krt.", "Tolna", "Újhegyi stny.", "Újpest" };
    String tamasildiko[] = { "Alkotás", "Bartók 71.", "Teréz krt.", "Dékán", "Hattyú", "Újpest", "Fehérvári út", "Harminckettesek", "Pablo Neruda" };
    String csatamaria[] = { "Garay", "Csepel", "Ferenc Krt.", "Kőrösi", "Újhegyi stny.", "Damjanich", "Erzsébet 1.", "Mester u.", "Erzsébet 2." };
    String csataedit[] = { "Pécs 1.", "Pécs 2", "Tolna", "Bicske", "Piliscsaba" };
    String suleildi[] = { "Debrecen 1.", "Debrecen 2.", "Karcag", "Püspökladány", "Békéscsaba 1.", "Gyula" };
    String kacsoevi[] = { "Csongrád", "Kecskemét 1.", "Kecskemét 2.", "Nagykőrös", "Kiskőrös" };
    String sandorzsuzsi[] = { "Mester u.", "Orosháza", "Békéscsaba 2.", "Szeged 1.", "Szeged 2." };
    String zilahianita[] = { "Oroszlány 1.", "Oroszlány 2.", "Tatabánya 1.", "Tatabánya 2.", "Tatabánya 3.", "Környe" };
    String spinnerSelection[]; // = { "Raktár" }
    String spinnerState, Spinner2State;
    Spinner spinner, spinner2;

    //spinner adapterek
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter2;

    //ListView array-ek
    ArrayList<String> barcodeList = new ArrayList<String>();
    ArrayList<String> markaList = new ArrayList<String>();
    ArrayList<String> termekList = new ArrayList<String>();
    ArrayList<String> mennyisegList = new ArrayList<String>();
    ArrayList<String> mergedList = new ArrayList<String>();

    String barcodeArray[];
    String mennyisegArray[];
    String markaArray[];
    String termekArray[];
    String mergedArray[];

    //ListView adapter
    //ListView itemsListView = (ListView) findViewById(R.id.itemListView);
    //ArrayAdapter<String> listViewAdapter =
    //        new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, barcodeArray);

    //onCreate
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        //getActionBar().setDisplayHomeAsUpEnabled(true);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().hide();

        vonalkodTextView = (TextView)findViewById(R.id.vonalkodTextView);
        messageTextView = (TextView)findViewById(R.id.messageTextView);

        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        fastMode = (CompoundButton) findViewById(R.id.fast_mode);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);
        autoFocus.setChecked(true);

        spinner = (Spinner) findViewById(R.id.bolt_spinner);
        spinner2 = (Spinner) findViewById(R.id.bolt_spinner2);
        spinner2.setEnabled(false);

        //ArrayList-ekből Array-ek:
        barcodeArray = barcodeList.toArray(new String[barcodeList.size()]);
        mennyisegArray = mennyisegList.toArray(new String[mennyisegList.size()]);
        termekArray = mennyisegList.toArray(new String[mennyisegList.size()]);
        mergedArray = mergedList.toArray(new String[mennyisegList.size()]);

/* IMEI - még nem jó!
        TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        deviceID = telephonyManager.getDeviceId();

        //Toast toast= Toast.makeText(getApplicationContext(),"IMEI: "+deviceID, Toast.LENGTH_LONG);
        //toast.setGravity(Gravity.CENTER,0,0); toast.show();
*/

        //Mennyiség gomb kezdeti elrejtése
        View changeButton = findViewById(R.id.enter_mennyiseg);
        changeButton.setVisibility(View.GONE);

        //SPINNER ADAPTEREK
        ArrayAdapter<String> spinnerArrayAdapterTerulet = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, teruletvalaszto);
        spinnerArrayAdapterTerulet.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
        spinner.setAdapter(spinnerArrayAdapterTerulet);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                               int position, long id) {
                teruletnev = adapter.getItemAtPosition(position).toString();
                if (teruletnev.matches("Raktár")){
                    spinnerState = "raktar";
                    spinner2.setEnabled(false);
                    //spinner2.setVisibility(View.GONE);
                    populateSpinner();
                }
                if  (teruletnev.matches("Csata Edit")){
                    spinnerState = "csataedit";
                    //spinner2.setVisibility(View.VISIBLE);
                    populateSpinner();
                }
                if  (teruletnev.matches("Csata Mária")){
                    spinnerState = "csatamaria";
                    populateSpinner();
                }
                if  (teruletnev.matches("Kacsó Évi")){
                    spinnerState = "kacsoevi";
                    populateSpinner();
                }
                if  (teruletnev.matches("Sándor Zsuzsi")){
                    spinnerState = "sandorzsuzsi";
                    populateSpinner();
                }
                if  (teruletnev.matches("Süle Ildikó")){
                    spinnerState = "suleildi";
                    populateSpinner();
                }
                if  (teruletnev.matches("Tamás Ildikó")){
                    spinnerState = "tamasildiko";
                    populateSpinner();
                }
                if  (teruletnev.matches("Zilahi Anita")){
                    spinnerState = "zilahianita";
                    populateSpinner();
                }
                if  (teruletnev.matches("Összes üzlet")){
                    spinnerState = "osszesbolt";
                    populateSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                //teruletnev = adapter.getItemAtPosition(position).toString();
                boltnev = adapter.getItemAtPosition(position).toString();
                //messageTextView.setText("Nyomj meg alul egy gombot!");
                            }
           @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });

        findViewById(R.id.read_barcode).setOnClickListener(this);
        findViewById(R.id.enter_barcode).setOnClickListener(this);
        findViewById(R.id.enter_mennyiseg).setOnClickListener(this);

        //Hosszú klikk - nem jó
        //findViewById(R.id.read_barcode).setOnLongClickListener(this);
    }

//eo onCreate
    void populateSpinner(){
        if(spinnerState == "raktar"){
            spinner2.setEnabled(false);
            messageTextView.setText("VÁLASZ BOLTCSOPORTOT");
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, raktar);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinner2.setAdapter(spinnerArrayAdapter);
        }

        if(spinnerState == "csataedit"){
            spinner2.setEnabled(true);
            messageTextView.setText("VÁLASZ ÜZLETET");
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, csataedit);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinner2.setAdapter(spinnerArrayAdapter);
        }

        if(spinnerState == "csatamaria"){
            spinner2.setEnabled(true);
            messageTextView.setText("VÁLASZ ÜZLETET");
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, csatamaria);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinner2.setAdapter(spinnerArrayAdapter);
        }

        if(spinnerState == "kacsoevi"){
            spinner2.setEnabled(true);
            messageTextView.setText("VÁLASZ ÜZLETET");
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, kacsoevi);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinner2.setAdapter(spinnerArrayAdapter);
        }

        if(spinnerState == "sandorzsuzsi"){
            spinner2.setEnabled(true);
            messageTextView.setText("VÁLASZ ÜZLETET");
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, sandorzsuzsi);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinner2.setAdapter(spinnerArrayAdapter);
        }

        if(spinnerState == "suleildi"){
            spinner2.setEnabled(true);
            messageTextView.setText("VÁLASZ ÜZLETET");
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, suleildi);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinner2.setAdapter(spinnerArrayAdapter);
        }

        if(spinnerState == "tamasildiko"){
            spinner2.setEnabled(true);
            messageTextView.setText("VÁLASZ ÜZLETET");
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tamasildiko);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinner2.setAdapter(spinnerArrayAdapter);
        }

        if(spinnerState == "zilahianita"){
            spinner2.setEnabled(true);
            messageTextView.setText("VÁLASZ ÜZLETET");
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zilahianita);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinner2.setAdapter(spinnerArrayAdapter);
        }

        if(spinnerState == "osszesbolt"){
            spinner2.setEnabled(true);
            messageTextView.setText("VÁLASZ ÜZLETET");
            ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, osszesbolt);
            spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // The drop down view
            spinner2.setAdapter(spinnerArrayAdapter);
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */

        /*
    Hosszú klikk - nem jó
    @Override


   public void onLongClick(View v) {
        Toast toast= Toast.makeText(getApplicationContext(),"LONG CLICK", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0); toast.show();
        return true;
    }*/

    public void onClick(View v) {
        fastMode = (CompoundButton) findViewById(R.id.fast_mode);
        //VONALKÓD OLVASÓ MEGHÍVASA
        if (v.getId() == R.id.read_barcode) {
            // launch barcode activity.
            if(fastMode.isChecked()){
                Intent intent = new Intent(this, BarcodeCaptureActivityFast.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
                intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());
                intent.putExtra(BarcodeCaptureActivity.FastMode, fastMode.isChecked());
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }
            else {
                Intent intent = new Intent(this, BarcodeCaptureActivity.class);
                intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
                intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());
                intent.putExtra(BarcodeCaptureActivity.FastMode, fastMode.isChecked());
                startActivityForResult(intent, RC_BARCODE_CAPTURE);
            }





        }
        //VONALKOD BEÍRÁSA KÉZZEL
        if (v.getId() == R.id.enter_barcode) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Írd be a vonalkódot!");
                // Set up the input
                final EditText input = new EditText(this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);
                // Set up the buttons
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Text = input.getText().toString();
                        manualInput = "YES";
                        barcode3 = m_Text;
                        getData();
                    }
                });
                builder.setNegativeButton("Mégse", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
        }

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
                    //m_Text = input2.getText().toString();
                    //getData();
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

    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     * <p/>
     * <p>You will receive this call immediately before onResume() when your
     * activity is re-starting.
     * <p/>
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     * @see #startActivityForResult
     * @see #createPendingResult
     * @see #setResult(int)
     */

// onResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //View changeButton = findViewById(R.id.enter_mennyiseg);
        //changeButton.setVisibility(View.VISIBLE);
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String barcode3 = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //Barcode barcode2 = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //vonalkodTextView.setText(barcode2.displayValue);


                    getData();
                    } else {
                    //no barcode error vibrate
                    //Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    //long[] pattern = {0, 50, 100, 50, 100, 50, 100, 50};
                    //v.vibrate(pattern, -1);
                    messageTextView.setText(R.string.barcode_failure);
                    Log.d(TAG, "No barcode captured, intent data is null");
                }
            } else {
                vonalkodTextView.setText(String.format(getString(R.string.barcode_error),
                        CommonStatusCodes.getStatusCodeString(resultCode)));
            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

//Stringrequest (url), Response
    private void getData() {
        //final TextView barcodeInfo = (TextView) findViewById(R.id.code_info);
        final TextView barcodeInfo = (TextView) findViewById(R.id.vonalkodTextView);
        String id = barcode3;

        vonalkodTextView.setText(barcode3);

        if (manualInput.equals("YES")) {
            id = m_Text;
            manualInput = "NO";
        }

        try {
            String encodedString = URLEncoder.encode(boltnev,"UTF-8");
            boltnev2 = encodedString;
            Log.d("TEST","encodedString:" + encodedString);
            } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //StringRequest felépítése
        String url = Config.DATA_URL+id+"&bkod="+boltnev2;

        if (boltnev.equals("Raktár")) {
            url = Config.DATA_RAKTAR_URL+id;
        }

        //StringRequest
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //        loading.dismiss();
                showJSON(response);
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this,error.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        barcodeInfo.setText(id);
    }

    //JSON feldolgozása, eredmények megjelenítése
    private void showJSON(String response) {
        String marka = "";
        String termek = "";
        String ar = "";
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(Config.JSON_ARRAY);
            JSONObject termekData = result.getJSONObject(0);
            marka = termekData.getString(Config.KEY_MARKA);
            termek = termekData.getString(Config.KEY_TERMEK);
            ar = termekData.getString(Config.KEY_KESZLET);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //terület és boltválasztó gombok elrejtáse
        spinner.setVisibility(View.GONE);
        spinner2.setVisibility(View.GONE);

        //Tömbök feltöltése

        //finals for intent extras
        final String ar1 = ar;
        final String barcode1 = barcode3;
        final String marka1 = marka;
        final String termek1 = termek;

/*
        if (termek.equals("null")){
            marka="ISMERETLEN MÁRKA";
            termek="ISMERETLEN TERMÉK";
        }

        if (marka.equals("null")){
            marka="ISMERETLEN MÁRKA";
            termek="ISMERETLEN TERMÉK";
        }
*/

        if (barcodeList.contains(barcode3)) {
            Toast toast = Toast.makeText(getApplicationContext(),"MÁR SZEREPEL A LISTÁN!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            //error already scanned vibrate
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {0, 200, 100, 200};
            v.vibrate(pattern, -1);
        } else if (marka == "null") {
            Toast toast = Toast.makeText(getApplicationContext(),"ISMERETLEN VONALKÓD!", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 300);
            toast.show();
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            long[] pattern = {0, 50, 50, 50, 50, 50};
            v.vibrate(pattern, -1);
        } else {
            barcodeList.add(barcode3);
            mennyisegList.add(ar);
            termekList.add(termek);
            markaList.add(marka);
            mergedList.add(marka+"\n"+termek + "\n" + ar + "db");
        }

        //ArrayList-ekből Array-ek:
        barcodeArray = barcodeList.toArray(new String[barcodeList.size()]);
        mennyisegArray = mennyisegList.toArray(new String[mennyisegList.size()]);
        termekArray = termekList.toArray(new String[termekList.size()]);
        markaArray = markaList.toArray(new String[markaList.size()]);
        mergedArray = mergedList.toArray(new String[mergedList.size()]);

        ListView itemsListView = (ListView) findViewById(itemListView);
        ArrayAdapter<String> listViewAdapter =
                //new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mergedArray);
                new ArrayAdapter<String>(this, android.R.layout.simple_selectable_list_item, mergedArray);
        itemsListView.setAdapter(listViewAdapter);

        //itemViewList = boltnev;
        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                String item = ((TextView)view).getText().toString();
                Intent intent = new Intent(view.getContext(),DetailsActivity.class);
/*
                String ar2 = mennyisegList.get(position-1);
                String marka2 = markaList.get(position-1);
                String termek2 = termekList.get(position-1);
                String barcode2 = barcodeList.get(position-1);
*/

                intent.putExtra("positionExtra",position);
                intent.putExtra("itemExtra",item);
                intent.putExtra("barcodeExtra",barcodeArray);
                intent.putExtra("dbExtra",mennyisegArray);
                intent.putExtra("markaExtra",markaArray);
                intent.putExtra("termekExtra",termekArray);

                //Toast toast = Toast.makeText(getApplicationContext(),"1:"+marka1+","+termek1+","+ar1+", Toast.LENGTH_LONG);
                //toast.setGravity(Gravity.CENTER, 0, 0);
                //toast.show();

                startActivity(intent);
            }
        });

        messageTextView = (TextView)findViewById(R.id.messageTextView);
        messageTextView.setText("BEOLVASOTT TERMÉKEK");
        //messageTextView.setVisibility(View.GONE);
        //messageTextView.setBackgroundColor(Color.parseColor("#ff0099cc"));
    }
}

//}