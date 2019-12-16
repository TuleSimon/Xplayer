package source;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.DateCell;
import javafx.scene.control.*;
import javafx.scene.control.DatePicker;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;


import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class createUser implements Initializable {
    @FXML
    private JFXTextField username;
    @FXML
    private JFXTextField email;
    @FXML
    private Circle circle;
    @FXML
    ImageView usericon;
    @FXML
    Label error;
    @FXML
    Button submit;
    @FXML
    Label error1;
    @FXML
    Label error2;
    @FXML
    private JFXDatePicker datepicker;
    private JFXButton create;
    private Connection connection;
    public boolean result;
    ResultSet resultSet;
    private PreparedStatement preparedStatement;
    BooleanProperty added=new SimpleBooleanProperty();
    Image image;
    FileInputStream fileInputStream;
    File imagedire;

    public createUser(){
        try {
            connection=new DBconnection().connectDatabase();
            pattern = Pattern.compile(EMAIL_PATTERN);


        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void initialize (URL url, ResourceBundle resourceBundle){
        Circle rect = new Circle();
        rect.setStyle("-fx-border-style:solid inside;"+"-fx-border-width:5;"+"-fx-border-color:orange;");
        rect.setFill(Color.WHITE);
        rect.setCenterX(120.0);
        rect.setCenterY(110.0);
        rect.setRadius(90.0f);
        rect.setEffect(new Reflection());
        usericon.setClip(rect);
        listener();
        dateconfigure();
        datepicker.setDefaultColor(Color.ORANGE);
        //rect=null;
        //usericon.setPreserveRatio(true);
        //datepicker.setStyle("-fx-background-");
    }
    public void createUser(ActionEvent e){

        String name=username.getText();
        String emailv=email.getText();
        System.out.println(username.getText());
        String Dob=datepicker.getEditor().getText();
        System.out.println(datepicker.getEditor().getText());
        String query="INSERT INTO Users(picture,username,email,dob) VALUES(?,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(query);
                if(imagedire.isFile()){
                        // set parameters
                        preparedStatement.setBytes(1, readFile(imagedire.getAbsolutePath()));
                        //preparedStatement.setInt(2, materialId);
                    preparedStatement.setString(2,name);
                    preparedStatement.setString(3,emailv);
                    preparedStatement.setString(4,Dob);
                    result=preparedStatement.execute();
                    System.out.println("Stored the file in the BLOB column.");
                        name=null;
                        emailv=null;
                        Dob=null;
                        query=null;
                        resultSet.close();
                        preparedStatement.close();
                    }
                else {
                    System.out.println("error ");
                }
            }
            catch (Exception ekk){
                System.out.println("im "+ekk.getLocalizedMessage());
                e.consume();
            }

            if(!result){
              new CreateDialogs().showAlert("insertion sucessful","WELCOME "+username.getText(),"Insertion Details");
              added.setValue(true);
                ((Stage)email.getScene().getWindow()).close();
                username=null;
                email=null;
                //new loadScreens().showhomeScene();

            }
            else{
                System.out.println(" insertion was Unsucessful");

            }
        }


public boolean exists(String username){
        String statement="SELECT * FROM Users WHERE username=?";
    try {
        preparedStatement= connection.prepareStatement(statement);
        preparedStatement.setString(1,username);

        resultSet=preparedStatement.executeQuery();
        System.out.println("true");
        if(resultSet.next()){
        return true;
        }
        else if(!resultSet.next()){
        return false;}
    } catch (SQLException e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
    }
    return false;
}
public void listener(){
        username.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(!validate(newValue)){
                    error.setText("Invalid username! check again");
                    submit.setDisable(true);
                }
                else if(exists(newValue)){
                    error.setText("Username already exists");
                    submit.setDisable(true);
                }
                else{
                    error.setText("");
                    if(validates(email.getText())){
                       submit.setDisable(false);
                        datevalid();
                    }
                }
            }
        });

    email.textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            if (!(validates(newValue))) {
                error2.setText("Invalid email! check again");
                submit.setDisable(true);
            }
            else {
                error2.setText("");
                if (validate(username.getText())) {
                   submit.setDisable(false);
                    datevalid();
                }
            }
        }
    });

}

public boolean validate(String username){

        if (username.length()>2){
            return true;}
        return false;
}



//configuring the date picker and adding action listener
public void dateconfigure(){
    datepicker.getEditor().textProperty().addListener(new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

        datevalid();
        }
    });

    final Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
        @Override
        public DateCell call(final DatePicker datePicker) {
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item.isAfter(LocalDate.now())) {
                        setDisable(true);
                        setStyle("-fx-background-color: #EEEEEE;");
                    }
                }
            };
        }
    };
    datepicker.setDayCellFactory(dayCellFactory);
}
public void datevalid(){
        if(datepicker.getEditor().getText()!=null) {
            try {
                String date = datepicker.getEditor().getText();
                LocalDate local = LocalDate.now();

                if (datepicker.getEditor().getText().isEmpty()) {

                    error1.setText("please enter date of birth");
                    submit.setDisable(true);
                }

                else if ((local.getYear()- datepicker.getValue().getYear()) < 5) {
                    error1.setText("You must be at least 5 Years old");
                    submit.setDisable(true);
                }
                else {
                    if (validate(username.getText()) && validate(email.getText())) {
                        error1.setText("");
                        submit.setDisable(false);
                    }
                }
            }
        catch(Exception e){
            System.out.println(e.getLocalizedMessage());
            }
        }
    }




//setting the file chooser for the profile image
    public void setImage(MouseEvent e){
        imagedire=imageChooser("select your profile pix", ((Node)e.getSource()).getScene().getWindow());
        System.out.println(imagedire.getAbsolutePath());
        try {
                image=new Image(String.valueOf(imagedire.toURI()));
                fileInputStream=new FileInputStream(imagedire.getAbsoluteFile());
            System.out.println(String.valueOf(imagedire.toURI()));
            } catch (Exception e1) {
                System.out.println(e1.getLocalizedMessage());
            }

        if(image!=null){
            usericon.setImage(image);
        }
        else{
            System.out.println("no Image selected");
        }
    }


    private byte[] readFile(String file) {
        ByteArrayOutputStream bos = null;
        try {
            File f = new File(file);
            FileInputStream fis = new FileInputStream(f);
            byte[] buffer = new byte[1024];
            bos = new ByteArrayOutputStream();
            for (int len; (len = fis.read(buffer)) != -1;) {
                bos.write(buffer, 0, len);
            }
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (IOException e2) {
            System.err.println(e2.getMessage());
        }
        return bos != null ? bos.toByteArray() : null;
    }

    public void updatePicture(String filename) {
        // update sql
        String updateSQL = "UPDATE Users "
                + "SET picture = ? "
                + "WHERE username= ?";

        try (
             PreparedStatement pstmt = connection.prepareStatement(updateSQL)) {

            // set parameters
            pstmt.setBytes(1, readFile(imagedire.getAbsolutePath()));
            //pstmt.setInt(1, materialId);

            pstmt.executeUpdate();
            System.out.println("Stored the file in the BLOB column.");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

            private Pattern pattern;
        private Matcher matcher;

        private static final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        public boolean validates(final String hex) {

            matcher = pattern.matcher(hex);
            return matcher.matches();

        }
    private FileChooser filechooser=new FileChooser();
    private String titles;

    public File imageChooser(String title, Window window){
        filechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("JPG files", "*.jpg*"),
                new FileChooser.ExtensionFilter("PNG Files","*.png*"));
        filechooser.setTitle(title);

        File file=filechooser.showOpenDialog(window);
        if(file.isFile())
            return file;
        return null;

    }
}
