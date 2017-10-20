package mock.spacetravel.model;

import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class FlightScheduleHolder {
    private static FlightScheduleHolder sFlightScheduleHolder;

    private FlightScheduleHolder(){
        setPlanets();
    }

    public static FlightScheduleHolder getFlightScheduleHolder(){
        if(null == sFlightScheduleHolder){
            synchronized (FlightScheduleHolder.class){
                sFlightScheduleHolder = new FlightScheduleHolder();
            }
        }
        return sFlightScheduleHolder;
    }

    private List<FlightModel> flightInfo = new ArrayList<>();

    private List<String> planets = new ArrayList<>();

    private List<Flight> flights = new ArrayList<>();

    public List<FlightModel> getFlightInfo(){
        return flightInfo;
    }

    /**
     * list of available travel origin/destinations
     * If this information comes from server ..
     * .. it will be filled as per server response
     */
    private void setPlanets(){
        planets.add("Earth");
        planets.add("Jupiter");
        planets.add("Mars");
        planets.add("Mercury");
        planets.add("Neptune");
        planets.add("Saturn");
    }

    public void setFlights(List<Flight> aFlights){
        if(flights.size() > 0)
            flights.clear();
        for(int i=0;i<aFlights.size();i++){
            flights.add(aFlights.get(i));
        }
    }

    public List<Flight> getFlights(){
        return flights;
    }

    public List<String> getPlanets(){
        return planets;
    }

    public FlightModel getFlightInfo(int index){
        return flightInfo.get(index);
    }

    public void addFlight(FlightModel flightModel){
        flightInfo.add(flightModel);
    }

    /**
     * Not in use now but can be utilized when ..
     * .. planets will be available from service
     * @param planet
     */
    public void addPlanet(String planet){
        planets.add(planet);
    }

    public void clear(){
        flightInfo.clear();
        flights.clear();
        planets.clear();
        sFlightScheduleHolder = null;
    }
}
