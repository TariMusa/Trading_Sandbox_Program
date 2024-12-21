// Tariro Musarandega

// Swing MainFrame that contains the display panels

// Resources:
// Geeks for Geeks
// Tutorials Point

package finalProject;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    private JPanel cards;
    private CardLayout cardLayout;
    private BloombergPanel bloombergPanel;
    private TradingPanel tradingPanel;

    public MainFrame() {
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        // Create only the Bloomberg panel initially
        bloombergPanel = new BloombergPanel(this);

        // Add the Bloomberg panel to the cards container
        cards.add(bloombergPanel, "Bloomberg");

        // Set the initial view
        cardLayout.show(cards, "Bloomberg");

        add(cards);

        // Set frame properties
        setPreferredSize(new Dimension(1024, 768)); // Set a reasonable default size
        setTitle("Stock Trading Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // method to switch to the TradingPanel
    public void switchToTradingPanel() {
        if (tradingPanel == null) {
            tradingPanel = new TradingPanel(
                    BloombergPanel.getSelectedHistoricalStockData(),
                    BloombergPanel.getPredictionData(),
                    this
            );
            cards.add(tradingPanel, "Trading");
        }
        cardLayout.show(cards, "Trading");
    }

    //method to display the BloombergPanel
    public void switchToBloombergPanel() {
        cardLayout.show(cards, "Bloomberg");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}