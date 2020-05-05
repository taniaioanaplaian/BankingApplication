package utils;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;


public class Utils {


    private static final String ALPHA_NUMERIC_STRING = "0123456789";

    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    public static LocalDateTime convertDate( java.sql.Date dateToConvert){
        Timestamp timestamp = new Timestamp(dateToConvert.getTime());
        return timestamp.toLocalDateTime();
    }
   public static  Date convertToDateViaInstant(LocalDateTime dateToConvert) {
       java.sql.Date sqlDate = java.sql.Date.valueOf(dateToConvert.toLocalDate());
       return sqlDate;
    }

    public static String getEncryptedPassword(String password){
        return  DigestUtils.sha256Hex(password);
    }

    public static void displayMessage(Label messageLabel, String messageText, String color) {

        messageLabel.setText(messageText);
        messageLabel.setVisible(true);
        messageLabel.setOpacity(1);
        messageLabel.setWrapText(true); 
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if(color.equals("RED"))
                    messageLabel.setTextFill(Color.web("#FF0000"));
                else if(color.equals("GREEN")){
                    messageLabel.setTextFill(Color.web("#009933"));
                }
                messageLabel.setText(messageText);
                messageLabel.setVisible(true);

                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    messageLabel.setVisible(false);
                }
            }
        };
        Thread thread = new Thread(runnable);
        thread.start();
    }


}
