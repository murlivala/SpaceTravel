package mock.spacetravel.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import mock.spacetravel.R;
import mock.spacetravel.callbacks.UICallback;
import mock.spacetravel.utils.Constants;
import mock.spacetravel.viewmodel.SpaceTravelActivityVM;


public class SpaceTravelActivity extends AppCompatActivity implements UICallback {

    private final String TAG = SpaceTravelActivity.class.getSimpleName();
    SpaceTravelActivityVM spaceTravelActivityVM;
    @BindView(R.id.layout_top) RelativeLayout layoutTop;
    @BindView(R.id.layout_mid)RelativeLayout layoutMid;
    @BindView(R.id.tv_email) EditText et_email;
    @BindView(R.id.tv_password) EditText et_password;
    @BindView(R.id.tv_top) TextView tv_top;
    @BindView(R.id.tv_mid) TextView tv_mid;
    @BindView(R.id.btn_login) Button btnLogin;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_space_travel);
        spaceTravelActivityVM = new SpaceTravelActivityVM(getApplicationContext(),this);
        setView();
        setHandler();
        mHandler.sendEmptyMessageDelayed(Constants.PREPARE_SCREEN,Constants.ANIMATION_DURATION);
    }

    private void setView(){
        ButterKnife.bind(this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Museo-500.otf");
        et_email.setTypeface(custom_font);
        btnLogin.setTypeface(custom_font);
        tv_mid.setTypeface(custom_font);
        tv_top.setTypeface(custom_font);

        //fade out or disappear animation
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setDuration(Constants.ANIMATION_DURATION);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeOut);
        layoutMid.setAnimation(animation);

        setListeners();
    }

    private void setListeners(){

        et_password.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int action, KeyEvent keyEvent) {
                if (action == EditorInfo.IME_ACTION_SEND) {
                    spaceTravelActivityVM.validateUser(et_email.getText().toString(),et_password.getText().toString());
                    InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    mgr.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spaceTravelActivityVM.validateUser(et_email.getText().toString(),et_password.getText().toString());
            }
        });

    }

    private void launchPlanetScreen(){
        Intent intent = new Intent();
        intent.setClassName(getPackageName(),getPackageName()+".ui.PlanetActivity");
        startActivity(intent);
        finish();
    }

    private void prepareLoginScreen(){
        layoutMid.setVisibility(View.INVISIBLE);
        layoutTop.setVisibility(View.VISIBLE);
        et_email.setVisibility(View.VISIBLE);
        et_password.setVisibility(View.VISIBLE);
        btnLogin.setVisibility(View.VISIBLE);

        //fade in or emerge animation
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setDuration(Constants.ANIMATION_DURATION_SHORT);
        AnimationSet animation = new AnimationSet(false);
        animation.addAnimation(fadeIn);
        et_email.setAnimation(animation);
        et_password.setAnimation(animation);
        btnLogin.setAnimation(animation);
        layoutTop.setAnimation(animation);


    }

    private void setHandler(){
        mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message inputMessage) {
                switch(inputMessage.what){
                    case Constants.PREPARE_SCREEN:
                        if(spaceTravelActivityVM.getLoginStatus())
                        {
                            launchPlanetScreen();
                            return;
                        }else{
                            spaceTravelActivityVM.authenticate();
                            prepareLoginScreen();
                        }
                        break;
                    case Constants.LOGIN_SUCCESS:
                        spaceTravelActivityVM.setLoginStatus(true);
                        launchPlanetScreen();
                        break;
                    case Constants.LOGIN_FAILURE:
                        String message = inputMessage.getData().getString("err");
                        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.LOGIN_USER_VALIDATION:
                        String email = inputMessage.getData().getString("email");
                        String password = inputMessage.getData().getString("password");
                        spaceTravelActivityVM.validateUser(email,password);
                        break;
                    default:
                            /*
                             * Pass along other messages from the UI
                             */
                        super.handleMessage(inputMessage);
                }
            }
        };
    }

    public void updateView(int status, final String response){

        switch (status){
            case Constants.LOGIN_SUCCESS:
                mHandler.sendEmptyMessage(Constants.LOGIN_SUCCESS);
                break;
            case Constants.LOGIN_FAILURE:
                Message msg = mHandler.obtainMessage(Constants.LOGIN_FAILURE);
                Bundle bundle = new Bundle();
                bundle.putString("err",response);
                msg.setData(bundle);
                mHandler.sendMessage(msg);
                break;
            case Constants.API_STATUS:
                //action to take here in case of Invalid API key
                Log.d(TAG,"updateView - "+response);
                break;
            default:

        }

    }

    public void onError(int status, final String errMessage){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(SpaceTravelActivity.this,errMessage,Toast.LENGTH_SHORT).show();
            }
        });

        if(BuildConfig.DEBUG){
            Log.d(TAG,"onError - "+errMessage);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(isFinishing()){
            if(null != spaceTravelActivityVM){
                mHandler.removeMessages(Constants.PREPARE_SCREEN);
                mHandler.removeMessages(Constants.LOGIN_SUCCESS);
                mHandler.removeMessages(Constants.LOGIN_FAILURE);
                mHandler.removeMessages(Constants.LOGIN_USER_VALIDATION);
                spaceTravelActivityVM.onViewFinished();
            }
        }
    }

}
