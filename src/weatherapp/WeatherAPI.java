package weatherapp;


import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * WeatherAPI sends a get request to our created API which notes our IP address and sends us back the respective Weather Info for our IP address location
 *
 * EXAMPLE: https://api.weather.gov
 * *** SLC *** https://api.weather.gov/points/39.4192,-111.9507
 * *** SLC Forecast *** https://api.weather.gov/gridpoints/SLC/90,114/forecast
 * EXAMPLE: our api
 * !Use this url: (same url server calls but id=$###### will be generated based on user ip)
 *      api.openweathermap.org/data/2.5/forecast?id=$5781770&appid=3047a788b7d827644b13600e4d46ab7b
 *
 * *** website : ec2-18-222-251-236.us-east-2.compute.amazonaws.com
 * *** website/json : http://ec2-18-222-251-236.us-east-2.compute.amazonaws.com/currentweather.json
 */
public class WeatherAPI {

    private final String USER_AGENT = "Mozilla/5.0";
    private int locationID;
    private String apiKey;
    private String unitType; // Imperial or Metric
    public String response;

    /**
     * Upon object creation WeatherAPI immediately uses sendGet() to send a get request to our created API
     * @throws Exception Throws an exception if the sendGet and setJsonFile methods do not work.
     */
    public WeatherAPI() throws Exception {
        sendGet();
        setJsonFile();
    }

    /**
     * Gets Weather information based off of the OpenWeatherMap API
     * Uses our API key 3047a788b7d827644b13600e4d46ab7b
     * @throws Exception If the weather connection cannot be established, an exception is thrown.
     */
    private void sendGet() throws Exception {
        locationID = 5780993; // Salt Lake Cities id as an example.
        StringBuffer response = null;
        try {
            URL url;
            apiKey = "3047a788b7d827644b13600e4d46ab7b";
            unitType = "imperial";

            url = new URL("http://ec2-18-222-251-236.us-east-2.compute.amazonaws.com/localweather.php");

//            Old URL
//            url = new URL("https://api.openweathermap.org/data/2.5/weather?id="
//                    + locationID + "&units=" + unitType +  "&APPID=" + apiKey);

            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            con.connect();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'GET' request to URL : " + url);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (IOException e) {
            System.err.println("Weather connection error in WeatherAPI.sendGet()");
            e.printStackTrace();
        }

        assert response != null;
        this.response = response.toString();

        //print result
        System.out.println(response.toString());

    }

    /**
     * Writes JSON file to src/resources/myfile.json using response from sendGet()
     * @throws IOException If the file cannot be written, and exception is thrown.
     */
    public void setJsonFile() throws IOException {

        try (FileWriter writer = new FileWriter("src\\weatherapp\\resources\\myfile.json")) {
            writer.write(this.response);
        }
    }

}
