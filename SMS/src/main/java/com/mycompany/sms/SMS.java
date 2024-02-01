/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mycompany.sms;

import com.twilio.Twilio;
import java.sql.SQLException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class SMS {

    private final static String url = "jdbc:postgresql://localhost:5432/twilio";
    private final static String user = "postgres";
    private final static String password = "Asmaa123";

    private static final String QUERY = "INSERT INTO twilio (account_sid) VALUES (?)";
    private static final String SELECT_ALL_QUERY = "select * from twilio;";

    public static void main(String[] args) {

        Twilio.init("AC782451390947f7f888ca070a31fe5e8b", "1c916d83ad00d3cd7099caa6796b7452");
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+2001004406793"),
                new com.twilio.type.PhoneNumber("+12062023856"),
                "How are you Asmaa?")
                .create();

        System.out.println(message.getSid());

        try (Connection connection = DriverManager.getConnection(url, user, password); PreparedStatement preparedStatement = connection.prepareStatement(QUERY);) {

            preparedStatement.setString(1, message.getSid());
            System.out.println(preparedStatement);

            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int id = rs.getInt("id");
                String account_sid = rs.getString("account_sid");
                System.out.println(id + "," + account_sid);
            }
        } catch (SQLException e) {
            printSQLException(e);
        }
    }

    public static void printSQLException(SQLException ex) {
        for (Throwable e : ex) {
            if (e instanceof SQLException) {
                e.printStackTrace(System.err);
                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
                System.err.println("Message: " + e.getMessage());
                Throwable t = ex.getCause();
                while (t != null) {
                    System.out.println("Cause: " + t);
                    t = t.getCause();
                }
            }
        }
    }

}
