// Tariro Musarandega

// BloombergPanel class / Panel (initial user interface)

// Resources:
// Bro Code

package finalProject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.List;

public class BloombergPanel extends JPanel {
    private JComboBox<String> tickerComboBox, timeFrameComboBox, marketSentimentComboBox;
    private JButton viewPortfolioButton, viewBalanceButton, runPredictionButton;
    private JLabel titleLabel, stockInfoLabel, balanceLabel;
    private String[] tickers = {"AAPL", "TSLA", "MSFT", "GOOGL", "AMZN"};
    private String[] timeFrames = {"1 Month", "3 Months", "6 Months"};
    private String[] marketSentiments = {"Bullish", "Neutral", "Bearish"};
    private static double accountBalance;
    private static String selectedTicker = "";
    private static String selectedSentiment = "";
    private static List<HistoricalData> selectedHistoricalHistoricalData;
    private static List<PredictionData> selectedPredictionData;
    private MainFrame mainFrame;
    private static String selectedTimeFrame = "";

    // this panel is a card of the mainFrame
    public BloombergPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        // generate the random account balance
        accountBalance = generateRandomBalance();
        HistoricalData.loadHistoricalData("prediction_df.csv");

        // Main panel with BoxLayout for vertical stacking
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title Label
        titleLabel = new JLabel("Stock Price Prediction Dashboard");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center the title
        mainPanel.add(titleLabel);

        // Create and add components
        addCenteredComponent(mainPanel, "Stock Ticker:", tickerComboBox = new JComboBox<>(tickers));
        addCenteredComponent(mainPanel, "Market Sentiment:", marketSentimentComboBox = new JComboBox<>(marketSentiments));
        addCenteredComponent(mainPanel, "Time Frame:", timeFrameComboBox = new JComboBox<>(timeFrames));

        // Add the stock info label (this will show the stock data)
        stockInfoLabel = new JLabel("");
        stockInfoLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        stockInfoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(stockInfoLabel);

        // Initialize balance label
        balanceLabel = new JLabel("");
        balanceLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        balanceLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        mainPanel.add(balanceLabel);  // Add it to the panel

        // Button panel for actions
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        viewPortfolioButton = createStyledButton("View Portfolio");
        viewBalanceButton = createStyledButton("View Balance");
        runPredictionButton = createStyledButton("Run Prediction");

        // Add buttons to the button panel
        buttonPanel.add(viewPortfolioButton);
        buttonPanel.add(viewBalanceButton);
        buttonPanel.add(runPredictionButton);

        // Add main panel and button panel to the frame
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);  // Center the form components
        add(buttonPanel, BorderLayout.SOUTH); // Place the buttons at the bottom

        // Setup action listeners for buttons
        setupActionListeners();

        // Fetch stock data once when the UI starts
        updateStockTicker();
    }

    // Method to generate a random account balance between 1000 and 100000
    private double generateRandomBalance() {
        Random random = new Random();
        return Math.round((1000 + (100000 - 1000) * random.nextDouble()) * 100.0) / 100.0;
    }

    // Helper method to add components with centered alignment
    private void addCenteredComponent(JPanel panel, String labelText, JComponent component) {
        JPanel subPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        subPanel.add(new JLabel(labelText));
        subPanel.add(component);
        panel.add(subPanel);
    }

    // Helper method to create a styled button
    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(150, 40));
        return button;
    }

    // Setup action listeners for buttons
    private void setupActionListeners() {
        // View Portfolio button functionality
        viewPortfolioButton.addActionListener(e -> {
            Map<String, StockPortfolio> portfolio = TradingPanel.getPortfolio();
            StringBuilder portfolioSummary = new StringBuilder();
            portfolioSummary.append("Your Portfolio Summary:\n\n");

            // Check if the portfolio is empty
            if (portfolio.isEmpty()) {
                portfolioSummary.append("You don't own any stocks.");
            } else {
                // Loop through the portfolio and append the details of each stock
                for (Map.Entry<String, StockPortfolio> entry : portfolio.entrySet()) {
                    String stockTicker = entry.getKey();
                    StockPortfolio stockPortfolio = entry.getValue();
                    String companyName = stockPortfolio.getCompanyName();
                    int sharesOwned = stockPortfolio.getSharesOwned();
                    double stockPrice = StockTicker.getClosingPrice(); // Get the current price of the stock

                    // Calculate the total value of the shares owned
                    double totalValue = stockPortfolio.getCurrentValue(stockPrice);

                    // Append stock details to the portfolio summary
                    String stockSummary = companyName + " (" + stockTicker + "): "
                            + sharesOwned + " shares at $" + stockPrice
                            + " each, Total Value: $" + totalValue;
                    portfolioSummary.append(stockSummary).append("\n");
                }
            }

            // Show the portfolio summary in a message dialog
            JOptionPane.showMessageDialog(mainFrame, portfolioSummary.toString(), "Portfolio Overview", JOptionPane.INFORMATION_MESSAGE);
        });

        // View Balance button functionality
        viewBalanceButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(mainFrame,
                    "Account Balance: $" + accountBalance,
                    "Balance Information",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // Add action listener for ticker selection
        tickerComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Update the selected ticker
                selectedTicker = (String) tickerComboBox.getSelectedItem();
                loadStockDataForSelectedTicker(selectedTicker);

                // debugging code
                System.out.println("Selected Ticker: " + selectedTicker);
                if (selectedHistoricalHistoricalData != null && !selectedHistoricalHistoricalData.isEmpty()) {
                    System.out.println("Historical Data for " + selectedTicker + ":");
                    for (HistoricalData data : selectedHistoricalHistoricalData) {
                        System.out.println(data);
                    }
                } else {
                    System.out.println("No data found for " + selectedTicker);
                }
            }
        });

        ActionListener tickerListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get selected ticker
                selectedTicker = (String) tickerComboBox.getSelectedItem();

                // Call method to load stock data for selected ticker
                loadStockDataForSelectedTicker(selectedTicker);
            }
        };

        // Assign the ActionListener to tickerComboBox
        tickerComboBox.addActionListener(tickerListener);

        marketSentimentComboBox.addActionListener(e -> {
            String sentiment = (String) marketSentimentComboBox.getSelectedItem();
            PredictionData.getPredictionFile(selectedTicker, sentiment);
        });

        timeFrameComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Get the selected time frame from the combo box
                selectedTimeFrame = (String) timeFrameComboBox.getSelectedItem();

                // Slice the prediction data based on the selected time frame
                assert selectedTimeFrame != null;
                List<PredictionData> slicedData = slicePredictionData(selectedPredictionData, selectedTimeFrame);

            }
        });

        // Create ActionListener for marketSentimentComboBox
        ActionListener sentimentListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selectedSentiment = (String) marketSentimentComboBox.getSelectedItem();

                // Get the prediction file path
                String predictionFilePath = PredictionData.getPredictionFile(selectedTicker, selectedSentiment);

                if (predictionFilePath != null) {
                    // Parse the CSV and load prediction data
                    selectedPredictionData = PredictionData.parseCSVToPredictionData(predictionFilePath);

                    if (selectedPredictionData != null && !selectedPredictionData.isEmpty()) {
                        System.out.println("Prediction Data Loaded for Sentiment: " + selectedSentiment);
                        for (int i = 0; i < Math.min(5, selectedPredictionData.size()); i++) {
                            System.out.println("Prediction Data Entry " + (i + 1) + ": " + selectedPredictionData.get(i).toString());
                        }
                    } else {
                        System.out.println("Prediction Data is Empty.");
                    }
                } else {
                    System.out.println("No prediction file found for " + selectedTicker + " with sentiment " + selectedSentiment);
                    selectedPredictionData = null;
                }
            }
        };

        // Assign the ActionListener to marketSentimentComboBox
        marketSentimentComboBox.addActionListener(sentimentListener);

        // Run Prediction button functionality
        runPredictionButton.addActionListener(e -> {
            if (selectedHistoricalHistoricalData == null || selectedHistoricalHistoricalData.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "No historical data available for the selected ticker.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (selectedPredictionData == null || selectedPredictionData.isEmpty()) {
                JOptionPane.showMessageDialog(mainFrame, "No prediction data available for the selected sentiment.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String selectedTimeFrame = (String) timeFrameComboBox.getSelectedItem();
            assert selectedTimeFrame != null;
            selectedPredictionData = slicePredictionData(selectedPredictionData, selectedTimeFrame);

            // Only switch to trading panel if data is loaded successfully
                mainFrame.switchToTradingPanel();
            });


        // When the user selects a new ticker, update the stock data
      tickerComboBox.addActionListener(e -> updateStockTicker());


    }

    // Fetch and display stock data (once on startup or when ticker changes)
    private void updateStockTicker() {
        String ticker = (String) tickerComboBox.getSelectedItem();
        StockTicker stockTicker = AlphaVantageAPI.getLatestStockData(ticker);

        if (stockTicker != null) {
            stockInfoLabel.setText("Ticker :" + ticker + stockTicker.toString());
            startMovingTicker(stockTicker);  // Start moving the ticker with new data
        } else {
            stockInfoLabel.setText("Error: Unable to fetch data");
        }
    }

    // Start the ticker moving with updated data
    private void startMovingTicker(StockTicker stockTicker) {
        String ticker = (String) tickerComboBox.getSelectedItem();
        String tickerText = "Ticker: " + ticker + stockTicker.toString();
        stockInfoLabel.setText(tickerText);

        // Calculate the width of the text using FontMetrics
        FontMetrics metrics = stockInfoLabel.getFontMetrics(stockInfoLabel.getFont());
        int textWidth = metrics.stringWidth(tickerText);

        // Set the JLabel size to fit the text
        stockInfoLabel.setSize(textWidth, stockInfoLabel.getHeight());

        // Timer to move the ticker horizontally
        Timer tickerTimer = new Timer(25, new ActionListener() {
            private int position = mainFrame.getWidth(); // Start just off the right edge of the frame

            @Override
            public void actionPerformed(ActionEvent e) {
                // Move the text left
                position -= 1;
                stockInfoLabel.setLocation(position, stockInfoLabel.getY());

                // Wrap around when it goes off the left side of the screen
                if (position < -textWidth) {
                    position = mainFrame.getWidth();
                }
            }
        });

        tickerTimer.start(); // Start the ticker
    }

    // method for loading the historical data for the selected ticker
    private void loadStockDataForSelectedTicker(String ticker) {
        selectedHistoricalHistoricalData = HistoricalData.getHistoricalData(ticker);
        if (selectedHistoricalHistoricalData == null || selectedHistoricalHistoricalData.isEmpty()) {
            System.out.println("No historical data found for ticker: " + ticker);
        } else {
            System.out.println("Loaded historical data for: " + ticker);
        }
    }

    // method for loading the predicted data for the selected ticker
    private List<PredictionData> slicePredictionData(List<PredictionData> selectedPredictionData , String timeFrame) {
        // query the length of the prediction data based on the time frame combo box selection
        this.selectedPredictionData = selectedPredictionData;
        int length;

        switch (timeFrame) {
            case "1 Month":
                length = 30;
                break;
            case "3 Months":
                length = 90;
                break;
            case "6 Months":
            default:
                length = Integer.MAX_VALUE;
                break;
        }

        if (selectedPredictionData == null || selectedPredictionData.isEmpty()) {
            System.err.println("Prediction data list is empty or null.");
            return new ArrayList<>();
        }

        // Ensure the length does not exceed the size of the list
        length = Math.min(length, selectedPredictionData.size());

        // Return a sublist of the desired length
        return selectedPredictionData.subList(0, length);
    }

    // method to access the predictions data
    public static List<PredictionData> getPredictionData() {
        return selectedPredictionData;
    }

    // method to access the queried historical data
    public static List<HistoricalData> getSelectedHistoricalStockData(){
        return selectedHistoricalHistoricalData;
    }

    // method to access the selected stock ticker
    public static String getSelectedTicker() {
        return selectedTicker;
    }

    // method to get the account balance to 2d.p
    public static double getAccountBalance() {
        return Math.round(accountBalance * 100.0) / 100.0;
    }

    // update the accountBalance based on transactions in the tradingPanel
    public static void setAccountBalance(double newBalance) {
        accountBalance = newBalance;
    }

    // access the selected sentiment
    public static String getSelectedSentiment () {
        return selectedSentiment;
    }

    // access the selected time frame
    public static String getSelectedTimeFrame () {
        return selectedTimeFrame;
    }

    // creation of the BloombergPanel
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BloombergPanel(new MainFrame()));
    }
}

