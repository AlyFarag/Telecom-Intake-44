package com.mycompany.makecall;

import java.net.URISyntaxException;
import com.twilio.base.ResourceSet;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Say;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Call;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class MakeCall {

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/calldb";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "amlahmad12345";

    public static void main(String[] args) throws URISyntaxException, SQLException {

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Choose an option:");
            System.out.println("1. Make a call");
            System.out.println("2. See call log");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    System.out.println("Enter the to_phone_number:");
                    String toPhoneNumber = scanner.next();
                    System.out.println("Enter the message:");
                    String message = scanner.next();
                    makeCall(toPhoneNumber, message);
                }
                case 2 ->
                    retrieveCallLogFromDatabase();
                case 3 -> {
                    return;
                }
                default ->
                    System.out.println("Invalid choice. Please try again.");
            }

        }

    }

    private static void makeCall(String toPhoneNumber, String message) throws URISyntaxException, SQLException {
        Twilio.init("AC71dd281f4ec71ca0e5905b3013166ef5", "405e0d0aeded2e9055cf45750bf26047");

        VoiceResponse voiceResponse = new VoiceResponse.Builder()
                .say(new Say.Builder(message).build())
                .build();

        Call call = Call.creator(
                new com.twilio.type.PhoneNumber(toPhoneNumber),
                new com.twilio.type.PhoneNumber("+16592228596"),
                new com.twilio.type.Twiml(voiceResponse.toXml())
        ).create();

        System.out.println("Call SID: " + call.getSid());

    }

    private static void saveCallsToDatabase() throws SQLException {
        Twilio.init("AC71dd281f4ec71ca0e5905b3013166ef5", "405e0d0aeded2e9055cf45750bf26047");
        ResourceSet<Call> calls = Call.reader().limit(1).read();

        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            String sql = "INSERT INTO call_log (call_sid, duration, price, status, direction, to_phone, date_created) VALUES (?, CAST(? AS INTEGER), CAST(? AS DOUBLE PRECISION), ?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

                if (calls.iterator().hasNext()) {
                    Call record = calls.iterator().next();

                    long unixTimestamp = record.getDateCreated().toInstant().toEpochMilli();
                    java.sql.Timestamp dateCreated = new java.sql.Timestamp(unixTimestamp);

                    preparedStatement.setString(1, record.getSid());
                    preparedStatement.setObject(2, record.getDuration());
                    preparedStatement.setObject(3, record.getPrice());
                    preparedStatement.setString(4, record.getStatus().toString());
                    preparedStatement.setString(5, record.getDirection());
                    preparedStatement.setString(6, record.getTo());
                    preparedStatement.setTimestamp(7, dateCreated);

                    preparedStatement.executeUpdate();

                    System.out.println("Call details saved to the database.");
                } else {
                    System.out.println("No calls found.");
                }
            }

        }
    }

    private static void retrieveCallLogFromDatabase() throws SQLException {

        saveCallsToDatabase();
        try (Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "SELECT * FROM call_log";
            try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        String callSid = resultSet.getString("call_sid");
                        int duration = resultSet.getInt("duration");
                        double price = resultSet.getDouble("price");
                        String status = resultSet.getString("status");
                        String direction = resultSet.getString("direction");
                        String toPhone = resultSet.getString("to_phone");
                        java.sql.Timestamp dateCreated = resultSet.getTimestamp("date_created");

                        ZonedDateTime zdt = Instant.ofEpochMilli(dateCreated.getTime()).atZone(ZoneId.systemDefault());

                        System.out.println("Call SID: " + callSid);
                        System.out.println("Duration: " + duration);
                        System.out.println("Price: " + price);
                        System.out.println("Status: " + status);
                        System.out.println("Direction: " + direction);
                        System.out.println("To Phone: " + toPhone);
                        System.out.println("Date Created: " + zdt);
                        System.out.println("--------------");
                    }
                }
            }
        }
    }

}
