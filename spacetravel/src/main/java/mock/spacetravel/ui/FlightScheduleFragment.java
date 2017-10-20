package mock.spacetravel.ui;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import mock.spacetravel.R;
import mock.spacetravel.adapter.CustomAdapter;


/**
 * A placeholder fragment containing a simple view.
 */
public class FlightScheduleFragment extends ListFragment implements AdapterView.OnItemClickListener {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public FlightScheduleFragment() {

    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static FlightScheduleFragment newInstance(int sectionNumber) {
        FlightScheduleFragment fragment = new FlightScheduleFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setListAdapter(new CustomAdapter(getActivity().getApplicationContext(),getArguments().getInt(ARG_SECTION_NUMBER)));
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //particular arrival or departure will be handled here
    }
}
