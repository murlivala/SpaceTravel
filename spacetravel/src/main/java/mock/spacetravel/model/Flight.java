package mock.spacetravel.model;

public class Flight {
    String destination;
    String arrival;
    String departure;

    public void setDestination(String destination){
        this.destination = destination;
    }

    public void setArrival(String arrival){
        this.arrival = arrival;
    }

    public void setDeparture(String departure){
        this.departure = departure;
    }

    public String getDestination(){
        return destination;
    }

    public String getArrival(){
        return arrival;
    }

    public String getDeparture(){
        return departure;
    }
}
