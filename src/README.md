# Webdata Stream Processor
This project handles the processing of user webdata events in real-time using Apache Flink. 
The goal is to identify high propensity buyers based on the user's behavior on the website.
After new events arrive, the system processes them and updates the user's profile. 


## Segmentation
We identify a high propensity buyer as follows:
- A user who has visited the website at least 10 times in the last 30 days.
- A user of which the average session duration is at least 5 minutes.
- A user who has added at least 1 item to the cart in the last 30 days.
