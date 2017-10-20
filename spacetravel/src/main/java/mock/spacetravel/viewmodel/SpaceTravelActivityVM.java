package mock.spacetravel.viewmodel;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import mock.spacetravel.callbacks.UICallback;
import mock.spacetravel.model.ResponseModel;
import mock.spacetravel.service.SpaceTravelService;
import mock.spacetravel.callbacks.ServiceCallback;
import mock.spacetravel.model.FlightModel;
import mock.spacetravel.utils.Constants;
import mock.spacetravel.utils.FlightUtil;
import okhttp3.Response;

public class SpaceTravelActivityVM implements ServiceCallback {
    UICallback uiCallback;
    SpaceTravelService spaceTravelService;
    SharedPreferences sharedpreferences;
    public SpaceTravelActivityVM(Context context,UICallback callback){
        uiCallback = callback;
        sharedpreferences = context.getSharedPreferences("mypref", Context.MODE_PRIVATE);
        spaceTravelService = new SpaceTravelService(SpaceTravelActivityVM.this);
    }

    public void authenticate(){
        spaceTravelService.authenticate();
    }

    public void validateUser(String email,String password){
        spaceTravelService.validate(email,password);
    }

    @Override
    public void onSuccess(int status, Response response){
        String message = "";
        try{
            Gson gson = new Gson();
            Type responseType = new TypeToken<ResponseModel>(){}.getType();
            ResponseModel result = gson.fromJson(response.body().charStream(), responseType);

            if(BuildConfig.DEBUG) {
                Log.d("####", "onSuccess - Status:" + result.getStatus());
                Log.d("####", "onSuccess - Message:" + result.getMessage());
            }
            if(result.getMessage().contains("API")){
                if(uiCallback != null){
                    uiCallback.updateView(Constants.API_STATUS,result.getMessage());
                }
            }else if("error".equalsIgnoreCase(result.getStatus())){
                if(uiCallback != null){
                    uiCallback.updateView(Constants.LOGIN_FAILURE,result.getMessage());
                }
            }else{
                if(uiCallback != null){
                    uiCallback.updateView(Constants.LOGIN_SUCCESS,result.getMessage());
                }
            }
            return;

        }catch(Exception e){

        }

        if(uiCallback != null){
            uiCallback.updateView(status,message);
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

    public void onViewFinished(){
        uiCallback = null;
        spaceTravelService.cancel();
        spaceTravelService = null;
    }

    public boolean getLoginStatus(){
        return sharedpreferences.getBoolean(Constants.LOGIN_STATUS_KEY,false);
    }

    public void setLoginStatus(boolean status){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putBoolean(Constants.LOGIN_STATUS_KEY, status);
        editor.commit();
    }
}
