// Tariro Musarandega

// Class for managing the portfolio

package finalProject;

public class StockPortfolio {
    private int sharesOwned;
    private double averagePrice;
    private String companyName;

    // constructor
    public StockPortfolio(int sharesOwned, double averagePrice, String companyName) {
        this.sharesOwned = sharesOwned;
        this.averagePrice = averagePrice;
        this.companyName = companyName;
    }

    // get the number of sharesowned
    public int getSharesOwned() {
        return sharesOwned;
    }

    // get the company name
    public String getCompanyName() {
        return companyName;
    }

    // add the shares owned after buting
    public void addShares(int shares, double purchasePrice) {
        // Update average price whenever new shares are bought
        double totalValue = sharesOwned * averagePrice + shares * purchasePrice;
        sharesOwned += shares;
        averagePrice = totalValue / sharesOwned;  // Recalculate average price
    }

    // remove shares after selling
    public void removeShares(int shares) {
        sharesOwned -= shares;
    }

    // get the current portfolio value
    public double getCurrentValue(double currentPrice) {
        return sharesOwned * currentPrice;
    }

    //method to get the number of shares owned
    public int getShares() {
        return sharesOwned;
    }
}
