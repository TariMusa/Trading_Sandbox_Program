# Trading Sandbox Program

The Trading Sandbox Program is a learning tool designed for novices to simulate stock trading without the risk of real financial loss. By using historical data and predictions based on market sentiment, users can interact with simulated stock trades in a realistic environment. This platform allows users to practice managing a portfolio, buying and selling stocks, and making informed decisions based on historical data trends and future predictions.

## Features
- Simulated Stock Trading: Trade with a virtual portfolio, buy and sell stocks from a limited selection (Google, Microsoft, Tesla, Apple, and Amazon).
- Historical Data: Analyze 10 years of historical stock prices for selected stocks.
- Market Sentiment: Input prevailing market sentiment to predict stock price movements.
- Deep Learning Predictions: Make predictions for stock price movements using deep neural networks in Python.
- Real-time Data (Planned): Integration with real-time stock data via Alpha Vantage API.
- Interactive Portfolio: View your portfolio balance and track your trades.
- Visualization: View interactive charts showing stock price trends, predictions, and portfolio performance.

## Main Interface 

<img width="1028" alt="Screenshot 2025-01-03 at 5 00 12 PM" src="https://github.com/user-attachments/assets/fd1d2049-9d35-42d9-8741-73cbd86d1e1c" />

The main interface prompts users to select a stock ticker and market sentiment. Users can view their balance, portfolio, and trade history. The user interface prompts users to enter the stock ticker on which they would like more information. This is from a combo box that currently includes only 5 tickers for Google, Microsoft, Tesla, Apple, and Amazon. Due to issues connecting to a reliable unpaid API for historical data, I had to rely on CSV files with closing stock prices spanning 10 years (August 2014 - August 2014). To manage the data volume 5 tickers seemed reasonable. 

The application also prompts the user to choose a prevailing market sentiment. This is important for the prediction that the program runs to show the potential performance of the closing stock price over the selected time frame. Each input the user makes queries the data that is used to make the prediction for the stock's performance. On this frame, the user has options to view their balance before they start trading and to view their portfolio which is empty when they start the program. They can then click the run prediction button which takes them to a different interface:


## Stock Price Prediction Interface
<img width="1021" alt="Screenshot 2025-01-03 at 5 00 54 PM" src="https://github.com/user-attachments/assets/e26d287e-2fe5-4297-8fad-4fec717b8e8c" />

This interface displays historical stock price data along with predicted future price trends based on market sentiment and deep learning models

This interface shows a visualization of the historical closing prices of the selected stock ticker over the 10-year period and the projected prices over the selected period. I used deep neural networks for these predictions in Python, and would like to explore better methods to predict the stock prices considering the only real predictor I was using besides the prices themselves were the prevailing market sentiments. From the visualization, the user can choose to either buy or sell the stocks. This is another area where the static nature of my data for the current implementation in some ways renders it useless as the stock market is in a constant phase of change and I hope to continue to work to mirror this. The user can buy or sell stocks at the current price pulled from the Alphavanatage API and after completing the transaction they can go back to the main page and either check their portfolios or balances and repeat the process. 

## Future Improvements
The following improvements are planned to enhance the functionality and accuracy of the program:
- Real-Time Stock Data: Integrating reliable APIs to fetch real-time stock prices.
- Enhanced Portfolio Management: Real-time tracking of portfolio value and dynamic stock pricing to mirror live trading environments.
- Advanced Predictive Models: Incorporating advanced time-series forecasting models like ARIMA, LSTM, or Prophet to improve stock price predictions.
- Sentiment Analysis Integration: Pulling sentiment data from news articles, social media, and financial reports to enhance stock performance predictions.
-  Looking into more and better stock price predictors and the possible use of trading algorithms/recommendations to the user to enhance the learning experience.
- Risk Management Features: Introducing features like stop-loss and take-profit orders for more realistic trading simulations.
- Gamification: Introducing a leaderboard to track the best-performing users and adding achievements for trading milestones.
