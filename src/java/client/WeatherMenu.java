package client;

import java.util.Scanner;

/**
 *
 * @author Stefan Ohlsson
 */
public class WeatherMenu {

    private static final Scanner sc = new Scanner(System.in);

    public static void showMenu() {
        try {
            System.out.println("Weather in your city");
            System.out.println("==========================================================");
            System.out.println("1. Submit City Name");

            System.out.println("2. Submit City ID");
            System.out.println("3. Submit City Coordinates");

            System.out.println("0. Exit");
            boolean loop = true;

            do {

                String decision = sc.nextLine();

                switch (decision) {

                    case "1":
                        RestClient.getWeatherByName();
                        showMenu();
                        break;

                    case "2":
                        RestClient.getWeatherByCityId();
                        System.out.println();
                        showMenu();
                        break;
                    case "3":
                        RestClient.getWeatherByCoordinates();
                        System.out.println();
                        showMenu();
                        break;

                    case "0":
                        loop = false;
                        sc.close();
                        System.out.println("Program terminated");
                        break;

                    default:
                        System.out.println("Try again with number 0 - 3");
                }
            } while (loop);
        } catch (IllegalStateException e) {
        } finally {
        }

    } // END showMenu

}
