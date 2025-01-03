//Tariro Musarandega

// Class to load the Historical data for each selected ticker
package finalProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HistoricalData {

    private static Map<String, List<HistoricalData>> stockDatabase = new HashMap<>();
    private LocalDate date;
    private double value;

    // Constructor for HistoricalData
    public HistoricalData(LocalDate date, double value) {
        this.date = date;
        this.value = value;
    }

    // get the date
    public LocalDate getDate() {
        return this.date;
    }

    // get the associated closing prices
    public double getValue() {
        return value;
    }

    // method for debugging
    @Override
    public String toString() {
        return "Date: " + date + ", Value: $" + value;
    }

    // Load historical data from CSV file
    public static void loadHistoricalData(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader("prediction_df.csv"))) {
            String headerLine = br.readLine();
            if (headerLine == null) {
                System.err.println("File is empty!");
                return;
            }

            // Split the header to extract ticker columns (AAPL, AMZN, GOOGL, TSLA, MSFT)
            String[] headers = headerLine.split(",");
            List<String> tickers = new ArrayList<>();
            for (int i = 4; i < headers.length; i++) {
                tickers.add(headers[i].trim()); // Add ticker to the list
                stockDatabase.putIfAbsent(headers[i].trim(), new ArrayList<>()); // Initialize stock data for each ticker
            }

            // Process the data rows
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                LocalDate date = LocalDate.parse(tokens[0]); // Extract the date

                // For each ticker, store the closing price associated with the date
                for (int i = 4; i < tokens.length; i++) {
                    String ticker = headers[i].trim();
                    double closingPrice = Double.parseDouble(tokens[i].trim()); // Extract the ticker's closing price

                    // Create a StockData object for the ticker and date
                    HistoricalData historicalData = new HistoricalData(date, closingPrice);

                    // Add to the stock database (use ticker as the key)
                    stockDatabase.get(ticker).add(historicalData);
                }
            }

        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid number format: " + e.getMessage());
        }
    }

    // Get historical data for a specific ticker
    public static List<HistoricalData> getHistoricalData(String ticker) {
        return stockDatabase.getOrDefault(ticker, new ArrayList<>());
    }


    public static void main(String[] args) throws IOException {
        String filePath = "prediction_df.csv"; // Replace with your actual file path
        loadHistoricalData(filePath);

        // Example: Retrieve historical data for AAPL
        List<HistoricalData> aaplData = getHistoricalData("AAPL");
        System.out.println("AAPL Historical Data:");
        for (HistoricalData data : aaplData) {
            System.out.println(data);
        }



    }}
