# CryptoTracker

A simple cryptocurrency portfolio tracker that displays your holdings in real-time. Perfect for users who manage their crypto in cold wallets and want to track their portfolio value.

## Features

- Real-time cryptocurrency price tracking
- Simple portfolio management through configuration
- Clean, minimal interface
- Support for multiple cryptocurrencies

## Tech Stack

- Frontend: React with TypeScript
- Backend: Java Spring Boot
- API: CoinGecko for real-time crypto prices

## Getting Started

### Prerequisites

- Java 17 or higher
- Node.js (v16 or higher)
- Maven

### Installation

1. Clone the repository
```bash
git clone https://github.com/yourusername/CryptoTracker.git
cd CryptoTracker
```

2. Set up the backend
```bash
cd backend
mvn install
```

3. Set up the frontend
```bash
cd frontend
npm install
```

4. Configure your portfolio
Edit `backend/src/main/resources/portfolio.json` to add your cryptocurrency holdings.

5. Start the application
```bash
# Start backend
cd backend
mvn spring-boot:run

# Start frontend
cd frontend
npm start
```

## Portfolio Configuration

The portfolio is managed through a simple JSON configuration file. Example:

```json
{
  "holdings": [
    {
      "symbol": "BTC",
      "amount": 0.5
    },
    {
      "symbol": "ETH",
      "amount": 2.0
    }
  ]
}
```

## License

This project is licensed under the MIT License 