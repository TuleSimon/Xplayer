package source;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import javafx.animation.*;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;
import org.w3c.dom.css.Rect;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class musicTemplate  {
    @FXML
    Label music=new Label("name");
    @FXML
    Label artistname;
    @FXML
    Label year;
    File file;

    @FXML
    private ImageView musicicon;
    Label indexlabel=new Label("");
    Label artistlabel=new Label("UNKNOWN ARTIST");
    Label yearlabel=new Label("year");
    Label filesize=new Label("size ");
    Label duration=new Label("duration");
    //@FXML
    //private HBox hbox=new HBox();
    @FXML
    Image image=new Image(getClass().getResource("/img/earpiece.png").toExternalForm(),120,120,true,true);
    @FXML
     ImageView imageview=new ImageView(image);
    ArrayList<ImageView> imagees=new ArrayList<>();
    ObservableList<ImageView> images;
    Image icon=null;
    BackgroundFill background_fill =null;
    public musicTemplate() {

        //usicname=new Label(file.getName());
        //musicname.setWrapText(true);
        //musicname.setStyle("-fx-text-fill:black");

    }
    VBox vbox=new VBox();
    HBox container2=new HBox();
    HBox hbox;

    public HBox createTemp(File files,int indexx,String size) throws IOException {
        music.setText(files.getName());
       //music.setWrapText(true);
        music.getStylesheets().add(getClass().getResource("/css/template.css").toExternalForm());
        music.getStyleClass().add("label1");
        //music.setMaxSize(200,60);
        //music.setPrefSize(220,40);
        music.setAlignment(Pos.CENTER);
        hbox=new HBox();
        filesize.setText(size);
        container2.getChildren().addAll(yearlabel,filesize);
        vbox.getChildren().addAll(music,artistlabel,container2,duration);
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        hbox.setPrefSize(360,112);
        hbox.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(5);
        hbox.setFillHeight(true);
        imageview.setPreserveRatio(false);
        imageview.setFitWidth(120);
        imageview.setFitHeight(120);
        hbox.getChildren().addAll(imageview,vbox);
        //hbox.getStylesheets().add(getClass().getResource("/css/template.css").toExternalForm());
        artistlabel.getStylesheets().add(getClass().getResource("/css/template.css").toExternalForm());
        //hbox.getStyleClass().add("hbox");
        container2.getStylesheets().add(getClass().getResource("/css/template.css").toExternalForm());
        container2.getStyleClass().add("label4");
        yearlabel.getStylesheets().add(getClass().getResource("/css/template.css").toExternalForm());
        yearlabel.getStyleClass().add("label4");
        filesize.getStylesheets().add(getClass().getResource("/css/template.css").toExternalForm());
        filesize.getStyleClass().add("label4");
        duration.getStylesheets().add(getClass().getResource("/css/template.css").toExternalForm());
        duration.getStyleClass().add("label4");
        container2.setAlignment(Pos.CENTER);
      background_fill = new BackgroundFill( Color.PINK,CornerRadii.EMPTY, Insets.EMPTY);
        background_fill2 = new BackgroundFill( Color.ORANGERED,CornerRadii.EMPTY, Insets.EMPTY);
        //hbox.setStyle("-fx-background-color:PINK;");
        setColor(background_fill2);
        KeyValue startKeyValue = new KeyValue(music.translateXProperty(), 0);
        KeyFrame startKeyFrame = new KeyFrame(Duration.seconds(1), startKeyValue);
        KeyValue endKeyValue = new KeyValue(music.translateXProperty(), 30);
        KeyFrame endKeyFrame = new KeyFrame(Duration.seconds(2), endKeyValue);
// Create a Timeline
        Timeline timeline = new Timeline(startKeyFrame, endKeyFrame);
// Let the animation run forever
        timeline.setCycleCount(Timeline.INDEFINITE);


        timeline.play();

        hbox.setOnMouseEntered(a->{
             hbox.setPrefSize(390,135);
            Background background = new Background(background_fill);
            hbox.setBackground(background);
            // setColor(background_fill);
        });
        hbox.setOnMouseExited(a->{
            hbox.setPrefSize(360,112);
            Background background = new Background(background_fill2);
            hbox.setBackground(background);

            });
        artistlabel.getStyleClass().add("label3");
        container2.setSpacing(5);
              // music1.setValue(music);
        System.out.println("1 timex " +music );
        //musicname.setText(music1.getValue());
        h2=hbox;
        return hbox;

    }
    HBox h2;
    BackgroundFill background_fill2;
    public JFXButton getTemp(){
        JFXButton but=new JFXButton(artistlabel.getText());
       // box.getChildren().removeAll();
        ImageView gr=new ImageView(imageview.getImage());
        gr.setPreserveRatio(false);
        gr.setFitWidth(120);
        gr.setFitHeight(120);
        but.setGraphic(gr);
        Background bg=new Background(background_fill2);
        but.setBackground(bg);
        but.setPrefSize(360,112);
         but.setOnMouseEntered(a->{
            but.setPrefSize(390,135);
            Background background = new Background(background_fill);
            but.setBackground(background);
             but.setStyle("-fx-background-radius:15;");
            // setColor(background_fill);
        });

        but.setOnMouseExited(a->{
            but.setPrefSize(360,112);
            Background background = new Background(background_fill2);
            but.setBackground(background);
        });

        return hbox!=null? but:null;
    }

    public JFXButton getTemp2() {
        JFXButton buts = new JFXButton(music.getText());
        // box.getChildren().removeAll();
        ImageView grs = new ImageView(imageview.getImage());
        grs.setPreserveRatio(false);
        grs.setFitWidth(120);
        grs.setFitHeight(120);
        buts.setGraphic(grs);
        Background bg = new Background(background_fill2);
        buts.setBackground(bg);
        buts.setStyle("-fx-background-radius:15;");
        buts.setPrefSize(360,112);
        buts.setOnMouseEntered(a -> {
            buts.setPrefSize(390, 135);
            Background background = new Background(background_fill);
            buts.setBackground(background);
            // setColor(background_fill);
        });
        buts.setOnMouseExited(a->{
            buts.setPrefSize(360,112);
            Background background = new Background(background_fill2);
            buts.setBackground(background);
        });

        return buts;
    }

    HBox container3=new HBox();
    HBox hbox2=new HBox();
    VBox vbox2=new VBox();
    ImageView view2=new ImageView();

        public void setmusicname(String name){
   music.setText(name);
    }
    public void setartist(String name){
        artistlabel.setText(name);
    }
    public void setyear(String name){
        yearlabel.setText(name);
    }
    public void setDuration(String val){
        duration.setText(val);

    }
    public void setSize(String val){
        filesize.setText(val);
    }

    public void setImage(Image image){
        imageview.setImage(image);
    }

    public Color getmostDominant(ImageView imgs){
        final Image img = imgs.getImage();

// Read through the pixels and count the number of occurrences of each color.

         PixelReader pr = img.getPixelReader();
       Map<Color, Long> colCount = new HashMap<>();

        for(int x = 0; x < img.getWidth(); x++) {
            for(int y = 0; y < img.getHeight(); y++) {
                final Color col = pr.getColor(x, y);
                if(colCount.containsKey(col)) {
                    colCount.put(col, colCount.get(col) + 1);
                } else {
                    colCount.put(col, 1L);
                }
            }

        }

// Get the color with the highest number of occurrences .

        final Color dominantCol = colCount.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();
            pr=null;
            colCount=null;
            return dominantCol;
    }
    public void setColor(BackgroundFill  background_fill ){
           hbox.setBackground( new Background(background_fill));
    }
}