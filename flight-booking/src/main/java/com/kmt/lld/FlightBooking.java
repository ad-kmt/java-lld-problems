package com.kmt.lld;

import java.util.HashMap;
import java.util.List;

public class FlightBooking {
    public static void main(String[] args) {
        FlightService flightService = FlightService.getInstance();
        UserService userService = UserService.getInstance();
        BookingService bookingService = BookingService.getInstance();

        Flight flight = flightService.createFlight("BLR", "DEL", 100);
        System.out.println(flight);

        List<Flight> searchResults = flightService.searchFlight("BLR", "DEL");
        System.out.println(searchResults);

        User user = userService.createUser("Aditya", "Aditya@mail.com");

        Booking booking = bookingService.createBooking(user.email, flight.id);
        System.out.println(booking);

    }

}

class User {
    public String name;
    public String email;

    public User(String name, String email){
        this.name = name;
        this.email = email;
    }

    @Override
    public String toString(){
        return String.format("User{name: %s, email: %s}", name, email);
    }
}

class Flight {
    int id;
    String source;
    String destination;
    int capacity;
    int booked;

    public Flight(int id, String source, String destination, int capacity){
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.capacity = capacity;
        this.booked = 0;
    }

    @Override
    public String toString(){
        return String.format("Flight{Source: %s, Destination: %s, Status: %d/%d}", source, destination, booked, capacity);
    }
}

class Booking {
    int id;
    User user;
    Flight flight;

    public Booking(int id, User user, Flight flight){
        this.id = id;
        this.user = user;
        this.flight = flight;
    }

    public String toString(){
        return String.format("Booking{ Id: %d User: %s, Flight: %s}", id, user, flight);
    }
}

class UserRepository{
    public static UserRepository instance;

    public HashMap<String, User> users = new HashMap<>();
    private UserRepository(){};

    public static UserRepository getInstance(){
        if(instance == null){
            instance = new UserRepository();
        }
        return instance;
    }
}

class BookingRepository{
    public static BookingRepository instance;

    public HashMap<Integer, Booking> bookings = new HashMap<>();
    public int counter = 1;

    public static BookingRepository getInstance(){
        if(instance == null){
            instance = new BookingRepository();
        }
        return instance;
    }
}

class FlightsRepository{

    public static FlightsRepository instance;

    public HashMap<Integer, Flight> flights = new HashMap<>();
    public int counter = 1;

    public static FlightsRepository getInstance(){
        if(instance == null){
            instance = new FlightsRepository();
        }
        return instance;
    }
}

class UserService{

    public static UserService instance;
    public UserRepository userRepository = UserRepository.getInstance();

    private UserService(){

    }

    public static UserService getInstance(){
        if(instance == null){
            instance = new UserService();
        }
        return instance;
    }

    public User createUser(String name, String email){
        if(userRepository.users.containsKey(email)){
            throw new RuntimeException("User already exists.");
        }
        User user = new User(name, email);
        userRepository.users.put(email, user);
        return user;
    }
    public User getUserByEmail(String emailId){
        if(userRepository.users.containsKey(emailId)){
            return userRepository.users.get(emailId);
        } else {
            throw new RuntimeException("User doesn't exists");
        }
    }
}

class FlightService {
    public static FlightService instance;

    public FlightsRepository flightsRepository = FlightsRepository.getInstance();

    private FlightService(){
    }

    public static FlightService getInstance(){
        if(instance == null){
            instance = new FlightService();
        }
        return instance;
    }

    public Flight createFlight(String source, String destination, int capacity){
        int flightId = flightsRepository.counter++;
        Flight flight = new Flight(flightId, source, destination, capacity);
        flightsRepository.flights.put(flightId, flight);
        return flight;
    }

    public List<Flight> searchFlight(String source, String destination){
        return flightsRepository.flights.values().stream()
                .filter(flight ->
                        flight.source.equals(source)
                        && flight.destination.equals(destination)
                        && flight.booked < flight.capacity)
                .toList();
    }
}

class BookingService{

    public BookingRepository bookingRepository = BookingRepository.getInstance();
    public UserRepository userRepository = UserRepository.getInstance();
    public FlightsRepository flightsRepository = FlightsRepository.getInstance();

    public static BookingService instance;
    private BookingService(){
    }
    public static BookingService getInstance(){
        if(instance == null){
            instance = new BookingService();
        }
        return instance;
    }

    public Booking createBooking(String userEmailId, int flightId){
        User user = userRepository.users.get(userEmailId);
        Flight flight = flightsRepository.flights.get(flightId);

        //validation
        if(flight.booked >= flight.capacity) {
            throw new RuntimeException("Booking Failed: No seats remaining.");
        }

        // update flight status
        flight.booked++;
        flightsRepository.flights.put(flight.id, flight);

        // create booking
        int bookingId = bookingRepository.counter++;
        Booking booking = new Booking(bookingId, user, flight);
        bookingRepository.bookings.put(bookingId, booking);

        return booking;
    }
}