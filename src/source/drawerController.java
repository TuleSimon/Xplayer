package source;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class drawerController extends AnchorPane{



    VBox controlsbox=new VBox();
    VBox extracontrolbox=new VBox();
    VBox mediadetails=new VBox();
    HBox progessbox=new HBox();
    HBox maincontrolsbox=new HBox();
    VBox volumebox=new VBox();
       @FXML
    JFXButton play=new JFXButton("PLAY");
    @FXML
    ImageView pause;
    @FXML
    JFXButton back=new JFXButton("BACK");
    @FXML
    JFXButton next=new JFXButton("NEXT");
    @FXML
    JFXButton shuffle=new JFXButton("shuffle");
    JFXButton repeat = new JFXButton("Repeat");
    Label volumelabel = new Label("Volume: 100%");
    @FXML
    Label musicname=new Label("music name");
    @FXML
    Label musicyear=new Label("music year");
    @FXML
    Label Artistname=new Label("artist name");
    Label durationstart=new Label("0");
    Label durationend = new Label ("0");
    @FXML
    JFXSlider progress=new JFXSlider(0.0,1.0,0.1);
    @FXML
    JFXSlider volume= new JFXSlider(0.0,1.0,1.0);
    private Media media=null;
    private MediaPlayer player;
    boolean playing=false;
    Pane pane=new Pane();
   private ObservableList<File> musicfile;
   public Image imgages;
    Image defaultimage=new Image(getClass().getResourceAsStream("/img/7.jpg"));
    ImageView musicicon=new ImageView(defaultimage);
    Image playhover=new Image(getClass().getResourceAsStream("/img/playhover.png"));
    Image playnormal=new Image(getClass().getResourceAsStream("/img/play.png"));
    Image nexthover=new Image(getClass().getResourceAsStream("/img/nexthover.png"));
    Image nextnormal=new Image(getClass().getResourceAsStream("/img/NEXT.png"));
    Image backhover=new Image(getClass().getResourceAsStream("/img/backhover.png"));
    Image backnormal=new Image(getClass().getResourceAsStream("/img/back.png"));
    Image shufflehover=new Image(getClass().getResourceAsStream("/img/shuffle hover.png"));
    Image shufflenormal=new Image(getClass().getResourceAsStream("/img/shuffle.png"));
    Image pausehover=new Image(getClass().getResourceAsStream("/img/PAUSEHOVER.png"));
    Image pausenormal=new Image(getClass().getResourceAsStream("/img/pause.png"));

    public  drawerController(ObservableList<File> file,ObservableList<musicTemplate> Temp){
        this.Temps=Temp;
       // this.setMinSize(1456,0);
        this.setPrefSize(1456,120);
        this.musicfile=file;
        this.setMaxSize(Double.MAX_VALUE,Double.MAX_VALUE);
        media=null;
        player=null;
        initLayout();
        this.getChildren().addAll(pane,masterbox,controlsbox,masterbox2);
        AnchorPane.setLeftAnchor(controlsbox,this.getPrefWidth()/5);
        AnchorPane.setRightAnchor(controlsbox,this.getPrefWidth()/5);
        AnchorPane.setBottomAnchor(controlsbox,20.0);
        AnchorPane.setRightAnchor(masterbox2,0.0);
        AnchorPane.setBottomAnchor(masterbox2,18.0);
        AnchorPane.setRightAnchor(pane,0.0);
        AnchorPane.setLeftAnchor(pane,0.0);
        setAllGraphics();
    }
    HBox masterbox= new HBox();
    HBox masterbox2= new HBox();


   //ading controls to the bottombar
    public void initLayout(){

        volume.setPrefWidth(200);
        volumebox.getChildren().addAll(volume,volumelabel);
       // musicname.setStyle("-fx-background-color:skyblue;"+"-fx-background-radius:20;");
        //Artistname.setStyle("-fx-background-color:skyblue;"+"-fx-background-radius:20;");
        mediadetails.getChildren().addAll(musicname,Artistname, musicyear);
        musicname.setWrapText(true);
        extracontrolbox.getChildren().addAll(shuffle,repeat);
        musicicon.setFitHeight(this.getPrefHeight());
        musicicon.setFitWidth(130);
        progress.setPrefWidth(900);
        progress.setPrefHeight(30);
        progessbox.setAlignment(Pos.CENTER);
        progessbox.getChildren().addAll(durationstart,progress,durationend);
        durationstart.setPrefWidth(80);
        durationend.setPrefWidth(80);
        durationstart.setLabelFor(progress);
        //durationstart.setWrapText(true);
        progessbox.setPrefWidth(1200);
        maincontrolsbox.setAlignment(Pos.CENTER);
        maincontrolsbox.getChildren().addAll(back,play,next);
        controlsbox.setAlignment(Pos.CENTER);
        controlsbox.getChildren().addAll(progessbox,maincontrolsbox);
        masterbox.getChildren().addAll(musicicon,mediadetails);
        masterbox2.setSpacing(10);
        masterbox2.getChildren().addAll(extracontrolbox,volumebox);
        masterbox.setAlignment(Pos.CENTER);
        masterbox2.setAlignment(Pos.CENTER);
        this.getStylesheets().add(getClass().getResource("/css/bottomUI.css").toExternalForm());
        this.getStyleClass().add("anchorpane");
        BackgroundFill background_fill = new BackgroundFill(Color.DARKGREY,
                CornerRadii.EMPTY, Insets.EMPTY);
        setColor(background_fill);
    }

    int index;
    public void setMedia(File file,int i){
        //musicicon.setImage((defaultimage));
        media=new Media(file.toURI().toString());
        player=new MediaPlayer(media);
        songindex=i;
        player.setOnReady(new Runnable(){
            @Override
            public void run() {
                listener();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                DecimalFormat df = new DecimalFormat("#.00");
                float time=(float) ((player.getTotalDuration().toSeconds())/60);
                durationend.setText(df.format(time)+" Min");
                musicname.setText(" [ "+Temps.get(i).music.getText()+" ]");
                musicyear.setText((" [ "+Temps.get(i).yearlabel.getText()+" ]"));
                Artistname.setText(" [ "+Temps.get(i).artistlabel.getText()+" ]");
                musicicon.setImage(Temps.get(i).imageview.getImage());
                BackgroundFill background_fill = new BackgroundFill(getmostDominant(musicicon),
                CornerRadii.EMPTY, Insets.EMPTY);
                setColor(background_fill);
                musicname.setTextFill( dominantCol.invert());
                Artistname.setTextFill(dominantCol.invert());
                musicyear.setTextFill(dominantCol.invert());
                durationend.setTextFill(dominantCol.invert());
                durationstart.setTextFill(dominantCol.invert());
                volumelabel.setTextFill(dominantCol.invert());
                 }
                    });
    player.setOnEndOfMedia(new Runnable() {
        @Override
        public void run() {
           next(songindex);
        }
    });
    }
    int songindex;
    ObservableList<musicTemplate> Temps= FXCollections.observableArrayList();



   public void next(int i){
       if((musicfile.size()-i)>1){
           setMedia(musicfile.get(i+1),i+1);
           play();}
       else{
           setMedia(musicfile.get(0),0);
           play();
       }
   }
    public void previous(int i){
        if(i>0){
            setMedia(musicfile.get(i-1),i-1);
            play();}
        else{
            setMedia(musicfile.get(musicfile.size()-1),musicfile.size()-1);
            play();
        }
    }

    //play song class
    public void play(int index){

        if(index!=songindex) {
            songindex=index;
            if (media != null) {
                player.stop();
                setMedia(musicfile.get(index),index);
                player.play();
            }
            else{
                setMedia(musicfile.get(index),index);
                player.play();
            }
        }
        else{
            if (media != null) {
                if (player.getStatus() == MediaPlayer.Status.PLAYING) {
                    player.pause();
                    listener();
               }
                else {
                    player.play();
                    listener();
                }
            }
        }
    }
    public void play(){

            if (media != null) {
                player.stop();
                player.play();
            }
            else{
                player.play();
            }
        }



    public boolean isplaying(){
     return false;
    }

boolean click=false;
public void seek(Duration duration){
    if (player.getStatus() == MediaPlayer.Status.STOPPED) {
            player.pause();
        }
     player.seek(duration);
    //System.out.println(duration +" of "+ progress.getMax());
    //progress.setValue(currentTime.toMillis() / total.toMillis());

    if (player.getStatus() != MediaPlayer.Status.PLAYING) {
            updatePositionSlider(duration);
        }
}
Slider bs=null;

    private void updatePositionSlider(Duration currentTime) {

        if (progress.isValueChanging()|| click)
            return;
        final Duration total = player.getTotalDuration();
        if (total == null || currentTime == null) {
            progress.setValue(0);
        } else {
            //System.out.println(currentTime.toMillis() / total.toMillis() +" of "+ progress.getMax());
            progress.setValue(currentTime.toMillis() / total.toMillis());
        }
    }

    private String formatDuration(Duration duration) {
        double millis = duration.toMillis();
        int seconds = (int) (millis / 1000) % 60;
        int minutes = (int) (millis / (1000 * 60));
        return String.format("%02d:%02d", minutes, seconds);
    }


    public void listener(){
        progress.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                                Boolean oldValue, Boolean newValue) {
                if (oldValue && !newValue) {
                    double pos = progress.getValue();
                    final Duration seekTo = player.getTotalDuration().multiply(pos);
                    seek(seekTo);
                }

            }});
        progress.setOnMouseClicked(a->{
            click=true;
            double pos = progress.getValue();
            final Duration seekTo = player.getTotalDuration().multiply(pos);
            player.seek(seekTo);
            click=false;
        });



        volume.valueChangingProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable,
                                Boolean oldValue, Boolean newValue) {
                if (oldValue && !newValue) {
                    double pos = volume.getValue();
                    player.setVolume(pos);
                    volumelabel.setText("Volume: "+Math.round(player.getVolume()*100)+"%");
                }

            }});
        volume.setOnMouseClicked(a->{
            double pos = volume.getValue();
            player.setVolume(pos);
            volumelabel.setText("Volume: "+Math.round(player.getVolume()*100)+"%");
        });



        player.currentTimeProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                final Duration currentTime = player.getCurrentTime();
                durationstart.setText(formatDuration(currentTime));
                //System.out.println(durationstart.getText());
                updatePositionSlider(currentTime);
            }
        });
        player.statusProperty().addListener(new ChangeListener<MediaPlayer.Status>() {
            @Override
            public void changed(ObservableValue<? extends MediaPlayer.Status> observable, MediaPlayer.Status oldValue, MediaPlayer.Status newValue) {
                if(newValue.toString()=="PLAYING"){
                        play.setGraphic(new ImageView(pausenormal));
                }
                else if (newValue== MediaPlayer.Status.PAUSED){
                    play.setGraphic(new ImageView(playnormal));
                }
            }
        });
    }

    public void mousehover(MouseEvent e){
    if(e.getSource()==play){
      // play.setImage(playhover);
    }
    else if(e.getSource()==next) {

    }
    else if(e.getSource()==back){
        //back.setImage(backhover);
    }
    else if(e.getSource()==shuffle){
        //shuffle.setImage(shufflehover);
    }

   }

   public void mouseleft(MouseEvent e){
       if(e.getSource()==play){

       }
       else if(e.getSource()==next) {

       }
       else if(e.getSource()==back){
          // back.setImage(backnormal);
       }
       else if(e.getSource()==shuffle){
          // shuffle.setImage(shufflenormal);
       }
   }

   public void click(MouseEvent e) {
       if (media != null) {
           if (player.getStatus() == MediaPlayer.Status.PLAYING ) {
               player.pause();

           }
           else{
           player.play();

           }
           //set();
           listener();
       }
   }

    public Color getmostDominant(ImageView imgs){
        final Image img = imgs.getImage();

// Read through the pixels and count the number of occurrences of each color.

        final PixelReader pr = img.getPixelReader();
        final Map<Color, Long> colCount = new HashMap<>();

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

        dominantCol = colCount.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();

        return dominantCol;
    }
    Color dominantCol;
    public void setColor(BackgroundFill  background_fill ){
        Background background = new Background(background_fill);
        this.setBackground(background);
    }

   public void setAllGraphics(){
    back.setGraphic(new ImageView(backnormal));
    back.setOnMouseEntered(a->{
        back.setGraphic(new ImageView(backhover));
    });
       back.setOnMouseExited(a->{
           back.setGraphic(new ImageView(backnormal));
       });
    back.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
       back.setOnAction(a->{
           previous(songindex);
       });
       next.setGraphic(new ImageView(nextnormal));
       next.setOnMouseEntered(a->{
           next.setGraphic(new ImageView(nexthover));
       });
       next.setOnMouseExited(a->{
           next.setGraphic(new ImageView(nextnormal));
       });
       next.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
       next.setOnAction(a->{
           next(songindex);
       });

       play.setGraphic(new ImageView(playnormal));
       play.setOnMouseEntered(a->{
           if(player==null){
               play.setGraphic(new ImageView(playhover));
           }
           try{
           if(player.getStatus()== MediaPlayer.Status.PAUSED) {
               play.setGraphic(new ImageView(playhover));
           }
           else{
                   play.setGraphic(new ImageView(pausehover));
               }
           }
           catch (Exception e){

           }
       });
       play.setOnMouseExited(a->{
           if(player==null){
               play.setGraphic(new ImageView(playnormal));
           }
           try{if(player.getStatus()== MediaPlayer.Status.PAUSED){
               play.setGraphic(new ImageView(playnormal));
           }
           else{play.setGraphic(new ImageView(pausenormal));
           }}
           catch (Exception e){

           }

       });
       play.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
       shuffle.setGraphic(new ImageView(shufflenormal));
       shuffle.setOnMouseEntered(a->{
           shuffle.setGraphic(new ImageView(shufflehover));
       });
       shuffle.setOnMouseExited(a->{
           shuffle.setGraphic(new ImageView(shufflenormal));
       });
       shuffle.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
       play.setOnAction(a->{
           playbutton();
       });
    }


    public void playbutton(){
        if(player!=null){
            if (player.getStatus() == MediaPlayer.Status.PLAYING) {
                player.pause();
                listener();
            }
            else {
                player.play();
                listener();
            }
        }
        else{
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setContentText("No media File has been selected");
            alert.setTitle("ERROR PLAYING MEDIA");
            alert.setHeaderText("PLEASE PLAY A FILE FIRST");
            alert.showAndWait();
            
        }
    }

}

