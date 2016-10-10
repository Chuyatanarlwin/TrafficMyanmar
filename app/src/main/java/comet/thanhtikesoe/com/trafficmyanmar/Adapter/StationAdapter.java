package comet.thanhtikesoe.com.trafficmyanmar.Adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import comet.thanhtikesoe.com.trafficmyanmar.PoliceStation;
import comet.thanhtikesoe.com.trafficmyanmar.R;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationsHolder>{


    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<PoliceStation> mDataset;
    private static MyClickListener myClickListener;


    public static class StationsHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener{

        TextView station_name;
        TextView station_phone;
        Button btn_call;
        private final Context context;


        public StationsHolder(View itemView) {
            super(itemView);
            context  = itemView.getContext();
            station_name = (TextView) itemView.findViewById(R.id.textView_name);
            station_phone = (TextView) itemView.findViewById(R.id.textview_phone);
            btn_call = (Button) itemView.findViewById(R.id.call_police_station);
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

    public StationAdapter(ArrayList<PoliceStation> myDataset) {
        mDataset = myDataset;
    }

    @Override
    public StationsHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_recyclerview_item, parent, false);

        StationsHolder stationsHolder = new StationsHolder(view);
        return stationsHolder;
    }

    @Override
    public void onBindViewHolder(final StationAdapter.StationsHolder holder, int position) {
        holder.station_name.setText(mDataset.get(position).getmStationName());
        holder.station_phone.setText(mDataset.get(position).getmPhoneno());

        holder.btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Context c = v.getContext();
                Intent intent= new Intent();
                intent.setAction(Intent.ACTION_CALL);
                Log.d("Phone No is:", String.valueOf(holder.station_phone));
                intent.setData(Uri.parse("tel:"+holder.station_phone));
                c.startActivity(intent);
            }
        });
    }

    public void addItem(PoliceStation dataObj, int index) {
        mDataset.add(dataObj);
        notifyItemInserted(index);
    }

    public void deleteItem(int index) {
        mDataset.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }
}
