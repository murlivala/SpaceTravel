package mock.spacetravel.service;

import java.io.IOException;

import mock.spacetravel.callbacks.ServiceCallback;
import mock.spacetravel.utils.Constants;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SpaceTravelService implements Callback {

    ServiceCallback serviceCallback;
    OkHttpClient client ;

    String authKeyTag = "?authkey=";
    private String authKey = Constants.AUTH_KEY_VALUE;
    String queryStringTag = "&action=";
    Call mCall;
    public SpaceTravelService(ServiceCallback callback){
        serviceCallback = callback;
        client = new OkHttpClient();
    }

    public void authenticate(){
        authenticate(Constants.AUTH_KEY_VALUE);
    }

    public void authenticate(String keyParam){
        String key = authKeyTag + keyParam;
        Request request = new Request.Builder()
                .url(Constants.SERVICE_BASE_URL + key)
                .build();
        mCall = client.newCall(request);
        mCall.enqueue(SpaceTravelService.this);
    }

    public void validate(String email,String pass){

        //Post request
        RequestBody body = new FormBody.Builder()
                .add(Constants.AUTH_KEY,Constants.AUTH_KEY_VALUE)
                .add("email", email)
                .add("password", pass)
                .add("action","login")
                .build();
        Request request = new Request.Builder()
                .url(Constants.SERVICE_BASE_URL)
                .post(body)
                .build();


        mCall = client.newCall(request);
        mCall.enqueue(SpaceTravelService.this);
    }

    public void getAllFlights(){
        getFlight("flights");
    }

    public void getFlight(String queryParam){
        String key = authKeyTag + authKey;
        String query = queryStringTag + queryParam;
        Request request = new Request.Builder()
                .url(Constants.SERVICE_BASE_URL + key + query)
                .build();

        mCall = client.newCall(request);
        mCall.enqueue(SpaceTravelService.this);
    }

    public void cancel(){

        serviceCallback = null;
        if(null != mCall){
            mCall.cancel();
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {
        if(null != serviceCallback){
            serviceCallback.onFailure(Constants.FAILURE,e.getMessage());
        }
    }

    @Override
    public void onResponse(Call call,final Response response) throws IOException {
        if(null != serviceCallback){
            serviceCallback.onSuccess(Constants.SUCCESS,response);
        }
    }

}