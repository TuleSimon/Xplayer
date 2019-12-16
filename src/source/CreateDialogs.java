package source;

import javafx.scene.control.Alert;
import javafx.scene.control.Dialog;

public class CreateDialogs {
    public void showAlert(String Header, String Content,String Title){
        Alert dialog=new Alert(Alert.AlertType.ERROR);
        dialog.setContentText(Content);
        dialog.setHeaderText(Header);
        dialog.setTitle(Title);
        dialog.showAndWait();

    }

}
