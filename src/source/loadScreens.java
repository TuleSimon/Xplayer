package source;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class loadScreens {
    Scene Usersscene;
    Stage stage=new Stage();
    Stage userstage=new Stage();
    Scene homescene;
    FXMLLoader loader = new FXMLLoader();
    Parent root;
    public Stage createUserScreen(){

        try {
            root= loader.load(getClass().getResource("createusersLayout.fxml").openStream());
            Usersscene = new Scene(root);
            createUser user=(createUser)loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        userstage.setResizable(false);
        userstage.setTitle("Create New User");
        userstage.initModality(Modality.APPLICATION_MODAL);
        userstage.setScene(Usersscene);
        userstage.showAndWait();

        return userstage;

    }
    public Stage getUserStage(){
        return userstage;
    }

    public void showhomeScene(){
        try {
            root= loader.load(getClass().getResource("Layout.fxml").openStream());
             homescene= new Scene(root);
            Controller home=(Controller) loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.setScene(homescene);
        stage.show();
        stage.setResizable(true);
        stage.setTitle("Media Player");

    }
    public Scene gethomeScene(){
        return homescene;
    }
}
