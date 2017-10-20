package mock.spacetravel.ui;

import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import mock.spacetravel.R;
import mock.spacetravel.callbacks.UICallback;
import mock.spacetravel.utils.Constants;
import mock.spacetravel.viewmodel.FlightScheduleActivityVM;

public class FlightScheduleActivity extends AppCompatActivity implements UICallback{

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private FlightScheduleActivityVM flightScheduleActivityVM;
    @BindView(R.id.pager) ViewPager mViewPager;
    @BindView(R.id.tabs) TabLayout tabLayout;
    @BindView(R.id.tv_planet) TextView planetText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_schedule);
        flightScheduleActivityVM = new FlightScheduleActivityVM(getApplicationContext(),FlightScheduleActivity.this);
        setView();
        String origin = getIntent().getStringExtra("planet");
        planetText.setText(origin);
        flightScheduleActivityVM.prepareFlightSchedule(origin);
        configureFragment();
    }

    private void setView(){
        ButterKnife.bind(this);
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/Museo-700.otf");
        planetText.setTypeface(custom_font);
    }


    private void addListeners(){
        if(null == mViewPager || null == tabLayout){
            return;
        }

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }

        });
    }

    private void configureFragment(){
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), Constants.FRAGMENT_PAGES);
        // Set up the ViewPager with the sections adapter.
        mViewPager.setAdapter(mSectionsPagerAdapter);

        for (int index = 0; index < Constants.FRAGMENT_PAGES; index++) {
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_rocket_small));
        }

        tabLayout.setupWithViewPager(mViewPager);
        addListeners();
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        int pageCount;
        public SectionsPagerAdapter(FragmentManager fm, int numOfPages) {
            super(fm);
            pageCount = numOfPages;
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a FlightScheduleFragment (defined as a static inner class below).
            return FlightScheduleFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return pageCount;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            String title = "";
            switch(position){
                case 0:
                    title = "DEPARTURE";
                    break;
                case 1:
                    title = "ARRIVALS";
            }
            return title;
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if(isFinishing()){
            flightScheduleActivityVM.onViewFinished();
        }
    }

    @Override
    public void updateView(int status, String response){

    }

    @Override
    public void onError(int status, String errMessage){

    }
}
