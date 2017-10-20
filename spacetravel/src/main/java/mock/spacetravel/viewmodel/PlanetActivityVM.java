package mock.spacetravel.viewmodel;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mock.spacetravel.callbacks.ServiceCallback;
import mock.spacetravel.callbacks.UICallback;
import mock.spacetravel.model.FlightModel;
import mock.spacetravel.service.SpaceTravelService;
import mock.spacetravel.utils.Constants;
import mock.spacetravel.utils.FlightUtil;
import okhttp3.Response;

public class PlanetActivityVM implements ServiceCallback {

    UICallback uiCallback;
    SpaceTravelService spaceTravelService;
    SharedPreferences sharedpreferences;
    public PlanetActivityVM(Context context, UICallback callback){
        uiCallback = callback;
        sharedpreferences = context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        spaceTravelService = new SpaceTravelService(PlanetActivityVM.this);
    }

    public void getAllFlights(){
        spaceTravelService.getAllFlights();
    }

    public void onSuccess(int status, Response response){
        if(null != response){
            try {

                if(FlightUtil.getFlightScheduleHolder().getFlightInfo().size() == 0 ){

                    Gson gson = new Gson();
                    Type founderListType = new TypeToken<ArrayList<FlightModel>>(){}.getType();
                    List<FlightModel> flightList = gson.fromJson(response.body().charStream(), founderListType);
                    if(BuildConfig.DEBUG){
                        Log.d("####","onSuccess - Total Flights:"+flightList.size());
                    }
                    for(int i= 0;i<flightList.size();i++){
                        FlightUtil.getFlightScheduleHolder().addFlight(flightList.get(i));
                    }
                }
                if(BuildConfig.DEBUG){
                    Log.d("####","onSuccess - Total Added:"+ FlightUtil.getFlightScheduleHolder().getFlightInfo().size());
                }
                if(uiCallback != null){
                    uiCallback.updateView(status,"Successfully Added.");
                }
            } catch (Exception e) {
                if(BuildConfig.DEBUG){
                    Log.d("####","onSuccess - PARSING ERROR "+e.getMessage());
                }
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onFailure(int status, String errorMessage){
        if(BuildConfig.DEBUG){
            Log.d("####","onFailure - "+errorMessage);
        }
        if(uiCallback != null){
            uiCallback.onError(status,errorMessage);
        }
    }

    public String getOriginStation(int position){
        return FlightUtil.getFlightScheduleHolder().getPlanets().get(position);
    }

    public void onViewFinished(){
        uiCallback = null;
        spaceTravelService.cancel();
        spaceTravelService = null;
        FlightUtil.getFlightScheduleHolder().clear();
    }

    public void setLoginStatus(boolean status){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(Constants.LOGIN_STATUS_KEY, status);
        editor.commit();
    }
}
