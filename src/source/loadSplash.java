package source;

import com.jfoenix.controls.JFXButton;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class loadSplash implements Initializable  {
    @FXML
    private StackPane stackpane;
//    Image defualt = new Image(getClass().getResource("/img/"));

    private ResultSet resultset;
    private PreparedStatement statement;
    private Connection connect;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

       new check().start();

    }



    /*public void doSomething(){
        new Thread((Runnable) new loadUser(stackpane).worker).start();
        ((Task<String>) new loadUser(stackpane).worker ).setOnSucceeded(event ->{
            System.out.println("The task succeeded.");
            new loadScreens().showhomeScene();

        });
        ((Task<String>) new loadUser(stackpane).worker ).setOnCancelled(event ->{
            System.out.println("The task is canceled.");
        });
        ((Task<String>) new loadUser(stackpane).worker ).setOnFailed(event ->{
            System.out.println("The task failed.");
        });

    }*/







   //thread to check if any user exists
    public class check extends Thread{
    @Override
    public void run(){
        try {
            Thread.sleep(4000);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                while(!fetch()){
                    new loadScreens().createUserScreen();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        System.out.println("sleeping");
                    }
                    //((Stage)stackpane.getScene().getWindow()).close();
                }

                    stackpane.getScene().getWindow().hide();

                    new loadScreens().showhomeScene();

                }

            });


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };
        }



        public boolean fetch(){
        try {
            connect = new DBconnection().connectDatabase();
            statement = connect.prepareStatement("SELECT * from users ");
            resultset = statement.executeQuery();
            try {
                if (!resultset.next()) {
                    connect.close();
                    statement.close();
                    resultset.close();
                    return false;
                     }
                else {

                    connect.close();
                    statement.close();
                    resultset.close();
                    return true;
                                    }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }

        } catch (Exception e) {
            new CreateDialogs().showAlert("Error while Loading Database", e.getLocalizedMessage(), "Application Encountered an Error");
        }
        return false;
    }
}
