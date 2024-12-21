// Tariro Musarandega

// AlphaVantage API for moving Stock Ticker

// Resources:
// AlphaVantage API documentation

package finalProject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AlphaVantageAPI {
    // input the API key
    private static final String API_KEY = "CNCC6BWZJINTO3O3";

    // Fetches the latest stock data for the selected ticker
    // Use the daily time series function from the API documentation
    public static StockTicker getLatestStockData(String stockTicker) {
        String function = "TIME_SERIES_DAILY";

        // Build the API URL to fetch daily stock data
        String url = "https://www.alphavantage.co/query?function=" + function +
                "&symbol=" + stockTicker + "&apikey=" + API_KEY;

        try {
            // Make the HTTP request
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = in.lines().reduce("", (acc, line) -> acc + line);
            in.close();

            // Log the raw response for debugging
            System.out.println("API Response: " + response);

            // limit check (25 daily requests allowed)
            if (response.contains("Thank you for using Alpha Vantage")) {
                System.out.println("Rate limit reached! Please try again later.");
                return null;
            }

            // Parse the JSON response
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

            // Check if the response contains valid time series data
            if (jsonObject.has("Note")) {
                System.out.println("API Error: " + jsonObject.get("Note").getAsString());
                return null;
            }

            JsonObject timeSeries = jsonObject.getAsJsonObject("Time Series (Daily)");

            // Debugging for no data returned (likely ticker mismatch)
            if (timeSeries == null) {
                System.out.println("Error: No data returned for ticker: " + stockTicker);
                return null;
            }

            // Get the latest date's stock data
            String latestDate = timeSeries.keySet().iterator().next();
            JsonObject latestData = timeSeries.getAsJsonObject(latestDate);

            // Extract needed data (close, high, low)
            double closingPrice = latestData.get("4. close").getAsDouble();
            double high = latestData.get("2. high").getAsDouble();
            double low = latestData.get("3. low").getAsDouble();

            // Return the stock data (date, closing price, high, low, volume)
            return new StockTicker(latestDate, closingPrice, high, low);

        } catch (Exception e) {
            e.printStackTrace();
            return null; // Return null if there's an error
        }
    }
}
