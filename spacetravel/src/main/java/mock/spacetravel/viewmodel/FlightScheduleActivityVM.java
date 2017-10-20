package mock.spacetravel.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

import mock.spacetravel.callbacks.ServiceCallback;
import mock.spacetravel.callbacks.UICallback;
import mock.spacetravel.model.Flight;
import mock.spacetravel.model.FlightModel;
import mock.spacetravel.utils.FlightUtil;
import okhttp3.Response;

public class FlightScheduleActivityVM implements ServiceCallback {
    UICallback uiCallback;
    SharedPreferences sharedpreferences;
    public FlightScheduleActivityVM(Context context, UICallback callback){
        sharedpreferences = context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        uiCallback = callback;
    }

    public void onSuccess(int status, Response response){

    }
    public void onFailure(int status, String errorMessage){

    }

    public void prepareFlightSchedule(String origin){
        List<Flight> flights = getSchedule(origin);
        FlightUtil.getFlightScheduleHolder().setFlights(flights);
    }

    private List<Flight> getSchedule(String origin){
        List<Flight> flights = new ArrayList<>();
        List<FlightModel> flightList = FlightUtil.getFlightScheduleHolder().getFlightInfo();
        Flight flight;
        for(int i= 0;i<flightList.size();i++) {
            if (origin.equals(flightList.get(i).getOrigin())) {
                flight = new Flight();
                flight.setArrival(flightList.get(i).getArrival());
                flight.setDeparture(flightList.get(i).getDeparture());
                flight.setDestination(flightList.get(i).getDestination());
                flights.add(flight);
            }
        }
        return flights;
    }

    public void onViewFinished(){
        uiCallback = null;
    }
}
