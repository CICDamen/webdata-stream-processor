# Webdata Stream Processor
This project handles the processing of user webdata events in real-time using Apache Flink. 
The goal is to identify high propensity buyers based on the user's behavior on the website.
After new events arrive, the system processes them and updates the user's profile. 


## Segmentation
We identify a high propensity buyer as follows:
- A user who has visited the website at least 5 times in the last 30 days.
- A user of which the average session duration is at least 5 minutes.
- A user who has added at least 1 item to the cart in the last 7 days.
- A user who visited the /checkout page at least once in the last 5 web sessions.

## Data
We use a webdata generation script using `Faker` to simulate the events. 

The following properties are available in the events:

- `userId`: The unique identifier of the user.
- `sessionId`: The unique identifier of the session.
- `timestamp`: The timestamp of event generation.
- `sessionDurationSeconds`: The duration of the session in seconds.
- `pageViews`: The page views in the session.
- `cartActivity`: Activities related to the cart.

After segmenting the users, we create a `HighPropensityBuyer` object with the following properties:

- `userId`: The unique identifier of the user.
- `visitCount`: The number of visits in the last 30 days.
- `cartCount`: The number of add to cart activities in the last 7 days.
- `averageSessionDuration`: The average session duration over all sessions.

## Source & Sink
We use a Kafka source to read the events from the topic `webdata` to which we push the events.
After processing the data, we write the results to the topic `high-propensity-buyers`.

## Future developments
- Combine with other data streams
- Use mapping from database