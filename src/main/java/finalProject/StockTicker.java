// Tariro Musarandega

// Class that manages the moving stock ticker

// Resources
// Coding Garden

package finalProject;

public class StockTicker {
    private String date;
    private static double closingPrice;
    private double high;
    private double low;

    // Constructor
    public StockTicker(String date, double closingPrice, double high, double low) {
        this.date = date;
        this.closingPrice = closingPrice;
        this.high = high;
        this.low = low;

    }

    // prints out the moving stock sticker
    @Override
    public String toString() {
        return ", Date: " + date + ", Closing: $" + closingPrice + ", High: $" + high + ", Low: $" + low;
    }

    // get the ClosingPrice
    public static double getClosingPrice() {
        return closingPrice;
    }

    // get the date
    public String getDate() {
        return this.date;
    }

    }

