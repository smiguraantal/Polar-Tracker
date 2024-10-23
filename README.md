# Polar Tracker

## Overview

Polar Tracker is a Spring Boot application designed to save workout data from the Polar API and store it in a personal database. 
The data is optimized for two types of workouts: running and cycling. 
The purpose of the application is to extract information from workouts that the Polar Flow interface does not provide. 
With the data stored in the database, it enables custom queries and the creation of unique statistics and analyses.

## Requirements and Setup

You'll need to configure some environment variables to connect the application to the necessary services:

- `POLAR_ACCESS_TOKEN` - Your Polar API access token.
- `MAIL_USER` - The email address the application will use to send notifications.
- `MAIL_PASSWORD` - The password for the email account.
- `DB_USERNAME` - The database username.
- `DB_PASSWORD` - The database password.

To start the application, run the `PolarTracker.java` file located in the `org.example` package.