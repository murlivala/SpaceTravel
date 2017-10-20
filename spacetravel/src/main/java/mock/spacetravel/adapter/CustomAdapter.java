package mock.spacetravel.adapter;


import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import mock.spacetravel.R;
import mock.spacetravel.model.Flight;
import mock.spacetravel.utils.FlightUtil;

public class CustomAdapter extends BaseAdapter {

    private Context mContext;
    int count;
    List<String> dataset;
    List<Flight> flights;
    int section = -1;//default, will be used for planets

    public CustomAdapter(Context context){
        mContext = context;
        count = FlightUtil.getFlightScheduleHolder().getPlanets().size();
        dataset = FlightUtil.getFlightScheduleHolder().getPlanets();
    }

    public CustomAdapter(Context context, int section){
        mContext = context;
        flights = FlightUtil.getFlightScheduleHolder().getFlights();
        count = flights.size();
        this.section = section;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return count;
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if(convertView == null){
            convertView = inflater.inflate(R.layout.row_layout, parent,false);
            holder = new ViewHolder();
            holder.txtname = (TextView) convertView.findViewById(R.id.firstLine);

            Typeface custom_font = Typeface.createFromAsset(mContext.getAssets(),  "fonts/Museo-700.otf");
            holder.txtname.setTypeface(custom_font);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(null != dataset){
            holder.txtname.setText(dataset.get(position));
        }else{
            if(1 == section){//Arrivals
                String text = flights.get(position).getArrival()+ "  from "+flights.get(position).getDestination();
                holder.txtname.setText(text);
            }else if(0 == section){//Departures
                String text = flights.get(position).getDeparture()+ " to "+flights.get(position).getDestination();
                holder.txtname.setText(text);
            }

        }

        return convertView;
    }

    static class ViewHolder{
        TextView txtname;
    }
}