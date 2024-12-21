// Tariro Musarandega

// Trading Panel Class

// Resources:
// Bro Code

package finalProject;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

public class TradingPanel extends JPanel {
    private MainFrame mainFrame;
    private List<HistoricalData> historicalPrices;
    private List<PredictionData> forecastedPrices;
    private static HashMap<String, StockPortfolio> portfolio = new HashMap<>();

    private JFrame frame;
    private JButton buyStocksButton, sellStocksButton, backButton;
    private JTextArea explanationTextArea;
    private ChartPanel chartPanel;
    private DefaultCategoryDataset dataset;


    public TradingPanel(List<HistoricalData> historicalPrices, List<PredictionData> forecastedPrices, MainFrame mainFrame) {
        // Check for null or empty data and print to check validity
        this.mainFrame = mainFrame;
        if (historicalPrices == null || historicalPrices.isEmpty()) {
            System.out.println("Historical prices data is missing or empty.");
        } else {
            System.out.println("Historical prices data loaded. Size: " + historicalPrices.size());
        }

        if (forecastedPrices == null || forecastedPrices.isEmpty()) {
            System.out.println("Forecasted prices data is missing or empty.");
        } else {
            System.out.println("Forecasted prices data loaded. Size: " + forecastedPrices.size());
        }

        // create the dataset and chart
        dataset = new DefaultCategoryDataset();
        chartPanel = createStockPriceChart(historicalPrices, forecastedPrices);
        chartPanel.setPreferredSize(new Dimension(700, 400));


        // Set the historical and forecasted data fields
        this.historicalPrices = historicalPrices;
        this.forecastedPrices = forecastedPrices;


        setLayout(new BorderLayout());

        // Add the chart panel to the layout
        add(chartPanel, BorderLayout.WEST);

        // Explanation text area
        JPanel explanationPanel = new JPanel(new BorderLayout());
        explanationTextArea = new JTextArea("This graph shows the predicted closing prices for " +
                BloombergPanel.getSelectedTicker() + " over " +
                BloombergPanel.getSelectedTimeFrame() +
                " when the prevailing market conditions are " +
                BloombergPanel.getSelectedSentiment() + ".");
        explanationTextArea.setWrapStyleWord(true);
        explanationTextArea.setLineWrap(true);
        explanationTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(explanationTextArea);
        scrollPane.setPreferredSize(new Dimension(200, 400));
        add(explanationPanel, BorderLayout.EAST);
        explanationPanel.add(scrollPane, BorderLayout.CENTER);


        // Buy/Sell buttons
        JPanel buttonPanel = new JPanel();
        buyStocksButton = new JButton("Buy Stocks");
        sellStocksButton = new JButton("Sell Stocks");
        buttonPanel.add(buyStocksButton);
        buttonPanel.add(sellStocksButton);

        backButton = new JButton("Back to Bloomberg");
        buttonPanel.add(backButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // Button actions
        buyStocksButton.addActionListener(e -> updateChartWithNewData(historicalPrices, forecastedPrices));
        sellStocksButton.addActionListener(e -> updateChartWithNewData(historicalPrices, forecastedPrices));
        backButton.addActionListener(e -> mainFrame.switchToBloombergPanel());

        // Buying shares
        buyStocksButton.addActionListener(e -> {
            String stockName = BloombergPanel.getSelectedTicker();
            String fullCompanyName;

            // Determine the full company name based on the ticker
            switch (stockName) {
                case "MSFT":
                    fullCompanyName = "Microsoft";
                    break;
                case "GOOGL":
                    fullCompanyName = "Google";
                    break;
                case "AAPL":
                    fullCompanyName = "Apple";
                    break;
                case "TSLA":
                    fullCompanyName = "Tesla";
                    break;
                case "AMZN":
                    fullCompanyName = "Amazon";
                    break;
                default:
                    fullCompanyName = "Unknown Company"; // Fallback if no match
                    break;
            }

            double stockPrice = StockTicker.getClosingPrice(); // Fetch current stock price

            // Prompt user for number of shares to buy
            String input = JOptionPane.showInputDialog(frame, "The current price of " + stockName + " is $" + stockPrice +
                    ".\nYour current account balance is $" + BloombergPanel.getAccountBalance() +
                    ".\nHow many shares would you like to buy?");

            try {
                int sharesToBuy = Integer.parseInt(input); // Parse the input
                double totalCost = sharesToBuy * stockPrice;

                // Check if user has sufficient funds to buy the shares
                if (BloombergPanel.getAccountBalance() >= totalCost) {
                    // Update account balance
                    double newBalance = BloombergPanel.getAccountBalance() - totalCost;
                    BloombergPanel.setAccountBalance(newBalance);

                    // Add the stock to the portfolio or update the existing stock's shares
                    if (portfolio.containsKey(stockName)) {
                        // Stock already in portfolio, update the shares and average price
                        StockPortfolio stockPortfolio = portfolio.get(stockName);
                        stockPortfolio.addShares(sharesToBuy, stockPrice);
                    } else {
                        // New stock, create a new portfolio entry
                        StockPortfolio newStockPortfolio = new StockPortfolio(sharesToBuy, stockPrice, fullCompanyName);
                        portfolio.put(stockName, newStockPortfolio);
                    }

                    JOptionPane.showMessageDialog(frame, "You bought " + sharesToBuy + " shares of " + fullCompanyName + ".\n" +
                            "Your new account balance is $" + newBalance);
                } else {
                    JOptionPane.showMessageDialog(frame, "Sorry, you don't have enough money to purchase " + sharesToBuy + " shares of " + fullCompanyName, "Insufficient Funds", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number of shares.", "Input Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        // selling shares
        sellStocksButton.addActionListener(e -> {
            String stockTicker = BloombergPanel.getSelectedTicker();
            String fullCompanyName = "";

            // Determine the full company name based on the ticker
            switch (stockTicker) {
                case "MSFT":
                    fullCompanyName = "Microsoft";
                    break;
                case "GOOGL":
                    fullCompanyName = "Google";
                    break;
                case "AAPL":
                    fullCompanyName = "Apple";
                    break;
                case "TSLA":
                    fullCompanyName = "Tesla";
                    break;
                case "AMZN":
                    fullCompanyName = "Amazon";
                    break;
                default:
                    fullCompanyName = "Unknown Company";
                    break;
            }

            // Check if the stock exists in the portfolio
            if (portfolio.containsKey(stockTicker)) {
                StockPortfolio stockPortfolio = portfolio.get(stockTicker);
                int sharesOwned = stockPortfolio.getSharesOwned(); // Get the number of shares owned
                double stockPrice = StockTicker.getClosingPrice(); // Get current stock price

                // Prompt user for number of shares to sell
                String input = JOptionPane.showInputDialog(frame, "You currently own " + sharesOwned + " shares of " + fullCompanyName +
                        ".\nThe current price of " + stockTicker + " is $" + stockPrice + ".\nHow many shares would you like to sell?");

                try {
                    int sharesToSell = Integer.parseInt(input); // Parse the input

                    // Check if user is trying to sell more shares than owned
                    if (sharesToSell > sharesOwned) {
                        JOptionPane.showMessageDialog(frame, "You can't sell more shares than you own.", "Insufficient Shares", JOptionPane.ERROR_MESSAGE);
                        return;
                    }

                    // Calculate the total sale value
                    double totalSaleValue = sharesToSell * stockPrice;

                    // Update account balance after selling the shares
                    double newBalance = BloombergPanel.getAccountBalance() + totalSaleValue;
                    BloombergPanel.setAccountBalance(newBalance);

                    // Remove the shares from the portfolio
                    stockPortfolio.removeShares(sharesToSell);

                    // If there are no shares left, remove the stock from the portfolio
                    if (stockPortfolio.getSharesOwned() == 0) {
                        portfolio.remove(stockTicker);
                    }

                    // Show confirmation message
                    JOptionPane.showMessageDialog(frame, "You sold " + sharesToSell + " shares of " + fullCompanyName + ".\n" +
                            "Your new account balance is $" + newBalance);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid input. Please enter a valid number of shares.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "You don't own any shares of " + fullCompanyName, "Stock Not Owned", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // method to get the portfolio
    public static Map<String, StockPortfolio> getPortfolio() {
        return portfolio;
    }

    // method to create the chat
    private ChartPanel createStockPriceChart(List<HistoricalData> historicalPrices, List<PredictionData> forecastedPrices) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Create dataset for historical prices
        XYSeriesCollection dataset = new XYSeriesCollection();

        XYSeries historicalSeries = new XYSeries("Historical Prices");
        for (HistoricalData data : historicalPrices) {
            // Convert LocalDate to java.util.Date
            Date date = Date.from(data.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            historicalSeries.add(date.getTime(), data.getValue());
        }
        dataset.addSeries(historicalSeries);

        // Create dataset for forecasted prices
        XYSeries forecastedSeries = new XYSeries("Predicted Prices");
        for (PredictionData data : forecastedPrices) {
            // Convert string to LocalDate and then to Date
            Date date = Date.from(data.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
            forecastedSeries.add(date.getTime(), data.getPrediction());
        }
        dataset.addSeries(forecastedSeries);

        // Create the XY chart with XYPlot
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Predicted Stock Prices",
                "Date",
                "Price (USD)",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );

        // Set the x-axis to use DateAxis
        XYPlot plot = chart.getXYPlot();
        DateAxis axis = new DateAxis("Date");
        axis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
        plot.setDomainPannable(true);
        plot.setDomainCrosshairVisible(true);
        plot.setDomainPannable(true);
        plot.setRangeCrosshairVisible(true);

        plot.setDomainAxis(axis);  // Set the DateAxis on the x-axis

        return new ChartPanel(chart);
    }

    // method to update the chart with new data
    private void updateChartWithNewData(List<HistoricalData> historicalPrices, List<PredictionData> forecastedPrices) {
        dataset.clear();

        for (HistoricalData data : historicalPrices) {
            dataset.addValue(data.getValue(), "Historical Prices", data.getDate());
        }

        for (PredictionData data : forecastedPrices) {
            dataset.addValue(data.getPrediction(), "Predicted Prices", data.getDate());
        }

        chartPanel.repaint();
    }

}