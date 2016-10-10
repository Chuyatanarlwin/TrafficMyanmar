package comet.thanhtikesoe.com.trafficmyanmar.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import comet.thanhtikesoe.com.trafficmyanmar.PoliceStation;
import comet.thanhtikesoe.com.trafficmyanmar.R;
import static comet.thanhtikesoe.com.trafficmyanmar.PoliceStation.*;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationsHolder>{

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private static MyClickListener myClickListener;
    private String Phone[];
    private String Name[];


    public StationAdapter(String[] name, String[] phone) {
        this.Name = name;
        this.Phone = phone;
    }

    public static class StationsHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView station_name;
        TextView station_phone;
        Button btn_call;
        private final Context context;
        PoliceStation station;


        public StationsHolder(View itemView) {
            super(itemView);
            context  = itemView.getContext();
            station_name = (TextView) itemView.findViewById(R.id.textView_name);
            station_phone = (TextView) itemView.findViewById(R.id.textview_phone);
            btn_call = (Button) itemView.findViewById(R.id.call_police_station);
            btn_call.setOnClickListener(this);
            Log.i(LOG_TAG, "Adding Listener");
        }


        @Override
        public void onClick(View v) {

            myClickListener.onItemClick(getPosition(), v);
        }
    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;

    }

    @Override
    public StationsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recyclerview_item, parent, false);

        StationsHolder stationsHolder = new StationsHolder(view);
        return stationsHolder;
    }

    @Override
    public void onBindViewHolder(final StationAdapter.StationsHolder holder, final int position) {

        final String[] PoliceStation_name = new String[policeStations.length];
        for (int i = 0; i < PoliceStation_name.length; i++) {
            PoliceStation_name[i] = policeStations[i].getmStationName();
            holder.station_name.setText(PoliceStation_name[i]);
        }


        String[]  PoliceStation_phone= new String[policeStations.length];
        for (int i = 0; i < PoliceStation_phone.length; i++) {
            PoliceStation_phone[i] = policeStations[i].getmPhoneno();
            holder.station_phone.setText(String.valueOf(PoliceStation_phone[i]));
            Log.d(LOG_TAG, "PHONE_NO " +PoliceStation_phone[i]);

        }


        holder.btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[]  PoliceStation_phone= new String[policeStations.length];
                for (int i = 0; i < PoliceStation_phone.length; i++) {
                    PoliceStation_phone[i] = policeStations[i].getmPhoneno();
                    holder.station_phone.setText(PoliceStation_phone[i]);
                    Log.d(LOG_TAG,PoliceStation_phone[i]);
                }

                Context c = v.getContext();
                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_CALL);

                intent.setData(Uri.parse("tel:09977792429"));
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return policeStations.length;

    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }

}
