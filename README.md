# High Propensity Buyer Detection System

A real-time stream processing application that identifies high-propensity buyers based on web activity data using Apache Flink and Kafka.

## Architecture Overview

This project implements a streaming data pipeline that processes web activity data to identify potential high-value customers. The system consists of three main layers:

1. **Data Production Layer**: Generates and sends web activity data to Kafka
2. **Stream Processing Layer**: Processes the data using Apache Flink
3. **Output Layer**: Produces high propensity buyer events

### Component Details

#### Producer Components
- `WebDataProducer`: Generates synthetic web activity data
- `KafkaWebDataSender`: Handles the communication with Kafka
- `WebDataSerializer`: Serializes web data events for Kafka

#### Processing Components
- `DataStreamJob`: Main Flink job that orchestrates the processing pipeline
- `WebDataDeserializer`: Deserializes incoming Kafka messages
- `HighPropensityBuyerDetector`: Core logic for identifying high-value customers
- `HighPropensityBuyerSerializationSchema`: Serializes output events

#### Data Models
- `WebData`: Represents web activity events (page views, cart activities)
- `HighPropensityBuyer`: Represents identified high-value customers

## Getting Started

### Prerequisites
- Java 11 or higher
- Apache Maven
- Docker and Docker Compose

### Installation

1. Clone the repository:
```bash
git clone [repository-url]
```

2. Build the project:
```bash
mvn clean package
```

3. Start the environment:
```bash
docker-compose up -d
```

## Running the Application

1. Start the Kafka producer:
```bash
java -cp target/[jar-name].jar org.digitalpower.producer.WebDataProducer
```

2. Start the Flink job:
```bash
java -cp target/[jar-name].jar org.digitalpower.process.DataStreamJob
```

## Testing

Run the test suite:
```bash
mvn test
```

## Configuration

Key configuration files:
- `docker-compose.yml`: Contains service configurations for Kafka and Zookeeper
- `src/main/resources/log4j2.properties`: Logging configuration

## Project Structure

```
├── src/
│   ├── main/java/org/digitalpower/
│   │   ├── deserialize/      # Kafka/Flink deserialization
│   │   ├── models/           # Data models
│   │   ├── process/          # Flink processing logic
│   │   ├── producer/         # Kafka producer components
│   │   └── serialize/        # Serialization logic
│   └── test/                # Test classes
├── docker-compose.yml       # Docker services configuration
└── pom.xml                 # Maven configuration
```

