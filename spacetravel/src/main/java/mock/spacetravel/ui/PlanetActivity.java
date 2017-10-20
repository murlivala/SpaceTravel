package mock.spacetravel.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import mock.spacetravel.R;
import mock.spacetravel.adapter.CustomAdapter;
import mock.spacetravel.callbacks.UICallback;
import mock.spacetravel.viewmodel.PlanetActivityVM;

public class PlanetActivity extends AppCompatActivity implements AdapterView.OnItemClickListener , UICallback{

    @BindView(R.id.list) ListView listView;
    @BindView(R.id.tv_logout) TextView tv_logout;
    @BindView(R.id.tv_title) TextView tv_title;
    PlanetActivityVM planetActivityVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planet);
        planetActivityVM = new PlanetActivityVM(getApplicationContext(),PlanetActivity.this);
        setViews();
        listView.setAdapter(new CustomAdapter(getApplicationContext()));
        listView.setOnItemClickListener(this);
        planetActivityVM.getAllFlights();
    }

    public void setViews(){
        ButterKnife.bind(this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Museo-700.otf");
        tv_title.setTypeface(custom_font);
        tv_logout.setTypeface(custom_font);
        setListeners();
    }

    private void setListeners(){
        tv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),getString(R.string.log_out),Toast.LENGTH_SHORT).show();
                planetActivityVM.setLoginStatus(false);
                launchLoginScreen();
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();
        overridePendingTransition(R.anim.slide_in_up,R.anim.slide_out_up);//bottom to top slide
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String planet = planetActivityVM.getOriginStation(position);
        launchFlightScheduleScreen(planet);
    }

    private void launchFlightScheduleScreen(String planet){
        Intent intent = new Intent();
        intent.setClassName(getPackageName(),getPackageName()+".ui.FlightScheduleActivity");
        intent.putExtra("planet",planet);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);//left to right slide
    }

    private void launchLoginScreen(){
        Intent intent = new Intent();
        intent.setClassName(getPackageName(),getPackageName()+".ui.SpaceTravelActivity");
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(isFinishing()){
            planetActivityVM.onViewFinished();
        }
    }

    @Override
    public void updateView(int status, String response){

    }

    @Override
    public void onError(int status, String errMessage){

    }
}
