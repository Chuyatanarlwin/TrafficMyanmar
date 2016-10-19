package comet.thanhtikesoe.com.trafficmyanmar;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.jaredrummler.materialspinner.MaterialSpinner;

public class TrafficViolationActivity extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "THANKS FOR SUBMISSION";
    private Button btn_submit;
    private ImageButton btn_capture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_traffic_violation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Traffic Violation");


        btn_capture = (ImageButton) findViewById(R.id.capture_image);
        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_submit = (Button) findViewById(R.id.submit_button);
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TrafficViolationActivity.this, SubmissionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        spinnerSetup();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    Spinner spinnerDay;

//    public void spinnerSetup() {
//
//        MaterialSpinner spinner = (MaterialSpinner) findViewById(R.id.spinner);

        // Spinner click listener
//        spinner.setOnItemSelectedListener((MaterialSpinner.OnItemSelectedListener) this);

////      Spinner Drop down elements
//        List<String> categories = new ArrayList<String>();
//        categories.add("Case One");
//        categories.add("Case Two");
//        categories.add("Case Three");
//        categories.add("Case Four");
//
//
//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,categories);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        // Spinner element
//        // attaching data adapter to spinner
//        spinner.setAdapter(dataAdapter);
//
//        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
//
//            @Override
//            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
//                Toast.makeText(getApplicationContext(), "Select " + item, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    private void spinnerSetup() {

        MaterialSpinner spinner = (MaterialSpinner)findViewById(R.id.spinner);
        spinner.setItems("Case One", "Case Two", "Case Three","Case Four");

        spinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                Toast.makeText(getApplicationContext(), "Clicked " + item, Toast.LENGTH_LONG).show();
            }
        });

    }
}
