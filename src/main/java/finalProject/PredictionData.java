// Tariro Musarandega

// Class for loading in the prediction data

// Resources:
// Bro Code

package finalProject;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public  class PredictionData {
        private LocalDate date;
        private double prediction;

        // constructor
        public PredictionData(LocalDate date, double prediction) {
            this.date = date;
            this.prediction = prediction;
        }

        // get the date
        public LocalDate getDate() {
            return date;
        }

        // get the price predicted
        public double getPrediction() {
            return prediction;
        }

        // method to print the predictions for debugging
        @Override
        public String toString() {
            return "PredictionData{" +
                    "date=" + date +
                    ", prediction=" + prediction +
                    '}';
        }


    // Method to get the prediction file path based on ticker and sentiment
    public static String getPredictionFile(String ticker, String sentiment) {
            // replace with your own file path
        String baseDirectory = "/your/base/directory"; // Adjust the base directory as needed
        String fileName = ticker + "_" + sentiment + ".csv";
        String filePath = baseDirectory + fileName;

        return filePath;
    }

    // Method to parse the CSV file and load prediction data
    public static List<PredictionData> parseCSVToPredictionData(String filePath) {
        List<PredictionData> predictionDataList = new ArrayList<>();

        // Define the date format used in the CSV file
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) { // Skip the header row
                    isHeader = false;
                    continue;
                }

                // Split the CSV line into columns
                String[] columns = line.split(",");

                try {
                    // Parse the date and prediction values
                    LocalDate date = LocalDate.parse(columns[0].trim(), dateFormatter); // Column 0: Date
                    double prediction = Double.parseDouble(columns[1].trim()); // Column 1: Prediction

                    // Create a PredictionData object and add it to the list
                    predictionDataList.add(new PredictionData(date, prediction));
                } catch (DateTimeParseException e) {
                    System.err.println("Error parsing date: " + columns[0] + " - " + e.getMessage());
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing prediction value: " + columns[1] + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the file: " + e.getMessage());
        }

        return predictionDataList;
    }
}

