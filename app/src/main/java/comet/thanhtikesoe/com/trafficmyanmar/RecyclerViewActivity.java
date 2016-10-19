package comet.thanhtikesoe.com.trafficmyanmar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

import comet.thanhtikesoe.com.trafficmyanmar.Adapter.MyRecyclerViewAdapter;
import comet.thanhtikesoe.com.trafficmyanmar.Model.PoliceStation;



public class RecyclerViewActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static String LOG_TAG = "RecyclerViewActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Yangon Traffic Police");
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyRecyclerViewAdapter(getDataSet());
        mRecyclerView.setAdapter(mAdapter);
        RecyclerView.ItemDecoration itemDecoration =
                new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Police Stations");
        // Code to Add an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).addItem(obj, index);

        // Code to remove an item with default animation
        //((MyRecyclerViewAdapter) mAdapter).deleteItem(index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ((MyRecyclerViewAdapter) mAdapter).setOnItemClickListener(new
            MyRecyclerViewAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {
                    Log.i(LOG_TAG, " Clicked on Item " + position);

                }
            });
    }

    private ArrayList<PoliceStation> getDataSet() {
        ArrayList results = new ArrayList<PoliceStation>();
        for (int index = 0; index < 20; index++) {
            PoliceStation obj = new PoliceStation("Police Station Name: " + index,
                    "Call: " + index);
            results.add(index, obj);
        }
        return results;
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

}
