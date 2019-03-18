package client;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ResponseProcessingException;
import javax.ws.rs.client.WebTarget;
import model.CurrentWeather;

/**
 * A REST-client consuming openweathermap REST-API. The API uses RESTful calls
 * issued in JSON format.
 *
 * @author Stefan Ohlsson
 */
public class RestClient {

    public static final Calendar CALENDAR = Calendar.getInstance();
    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("HH:mm");
    static Scanner sc = new Scanner(System.in);

    static String baseUrl = "http://api.openweathermap.org/data/2.5/weather?";

    static String cityName;
    static String cityId;

    static String lon;
    static String lat;

    static String units = "&units=metric";// metric for celcius
    static String language = "&lang=en"; //language for Description
    static String appId = "&APPID=<<Your APP-ID-string>>";// Replace with your app-id string

    static String address;

    static Client client = ClientBuilder.newClient();

    public static void getWeatherByName() {
        try {
            System.out.println("Submit a City Name");
            String cityNameOrg = sc.nextLine();
            // in order to add %20 between cityname with two words eg: New York
            cityName = cityNameOrg.replaceAll("\\s", "%20");
            address = baseUrl + "q=" + cityName + units + language + appId;

            printOutWeather(address);

        } catch (NotFoundException e) {
            System.out.println("City " + cityName + " not found in data base");
            //System.out.println("e: " + e);
        }
    }

    public static void getWeatherByCityId() {
        try {
            System.out.println("Submit a City ID");
            cityId = sc.nextLine();
            address = baseUrl + "id=" + cityId + units + language + appId;

            printOutWeather(address);

        } catch (NotFoundException e) {
            System.out.println("City with City ID " + cityId + " not found in data base");
            //System.out.println("e: " + e);
        } catch (BadRequestException e) {
            System.out.println("Bad Request: " + cityId + " is not a valid City ID");
            //System.out.println("e: " + e);
        }

    }

    public static void getWeatherByCoordinates() {
        try {
            System.out.println("Submit Latitude");
            lat = sc.nextLine();

            System.out.println("Submit Longitude");
            lon = sc.nextLine();

            address = baseUrl + "lat=" + lat + "&" + "lon=" + lon + units + language + appId;

            printOutWeather(address);

        } catch (NotFoundException e) {
            System.out.println("City with coordinates Longitude: " + lon + "and Latitude: " + lat + " not found in database");
            //System.out.println("e: " + e);

        } catch (BadRequestException e) {
            System.out.println("Bad Request: " + lon + " and/or " + lat + " are not correct values");
            //System.out.println("e: " + e);

        } catch (ResponseProcessingException | NotAuthorizedException e) {
            System.out.println("Error: " + lon + " and/or " + lat + " are not correct values");
            //System.out.println("e: " + e);
        }

    }

    public static void printOutWeather(String adress) {
        WebTarget target = client.target(adress);

        CurrentWeather currentWeather = target.request().get(CurrentWeather.class);

        StringBuilder iconUrl = new StringBuilder("http://openweathermap.org/img/w/").append(currentWeather.getWeather().get(0).getIcon()).append(".png");

        System.out.println("**********************************************************");
        System.out.println("City name:" + currentWeather.getName());
        System.out.println("Country Code: " + currentWeather.getSys().getCountry());
        System.out.println("Weather Type: " + currentWeather.getWeather().get(0).getMain());
        System.out.println("Description: " + currentWeather.getWeather().get(0).getDescription());
        System.out.println("Temperature: " + currentWeather.getMain().getTemp() + " °С");
        System.out.println("Temperature from: " + currentWeather.getMain().getTemp_min() + " to " + currentWeather.getMain().getTemp_max() + " °С");
        System.out.println("Wind Speed:" + currentWeather.getWind().getSpeed() + " m/s");
        System.out.println("Clouds: " + currentWeather.getClouds().getAll() + " %");
        System.out.println("Pressure: " + currentWeather.getMain().getPressure() + " hpa");
        System.out.println("Humidity: " + currentWeather.getMain().getHumidity() + " %");
        CALENDAR.setTimeInMillis(currentWeather.getSys().getSunrise() * 1000);
        System.out.println("Sunrise: " + SIMPLE_DATE_FORMAT.format(CALENDAR.getTime()) + " CET");
        CALENDAR.setTimeInMillis(currentWeather.getSys().getSunset() * 1000);
        System.out.println("Sunset: " + SIMPLE_DATE_FORMAT.format(CALENDAR.getTime()) + " CET");
        System.out.println("Coordinates degrees Latitude: " + currentWeather.getCoord().getLat() + ", Longitude: " + currentWeather.getCoord().getLon()); // OK
        System.out.println("City ID: " + currentWeather.getId());
        System.out.println("Weather Icon: " + iconUrl);
        System.out.println("************************************************************");
    }

}
