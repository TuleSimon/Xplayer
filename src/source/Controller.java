package source;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import javafx.animation.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Effect;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class Controller implements Initializable {
    @FXML
    JFXDrawer drawer;
    @FXML
    JFXDrawer drawer2;
    @FXML
    JFXHamburger hamburger;
    @FXML
    JFXHamburger hamburger2;
    @FXML
    BorderPane borderPane;
    @FXML
    Tab searchtab;
    @FXML
    Tab artist;
    @FXML
    Tab Albums;
    @FXML
    VBox emptyVBox;
    @FXML
    Button imports;
    @FXML
    FlowPane flowPane;
    @FXML
    StackPane stack;
    JFXProgressBar progressBar;
    ObservableList<File> musicFiles;
    ArrayList<Image> allImage=new ArrayList<>();
    Image imageone=new Image(getClass().getResource("/img/earpiece.png").toExternalForm(),120,120,true,true);
    @FXML

    Label year;
    File file;

    @FXML
    private ImageView musicicon;
    Label yearlabel=new Label("year");
    Label filesize=new Label("size ");
    Label duration=new Label("duration");
    //@FXML
    //private HBox hbox=new HBox();
    @FXML
    Image defimage=new Image(getClass().getResource("/img/earpiece.png").toExternalForm(),120,120,true,true);
    @FXML
    //ImageView imageview=new ImageView(image);
    ArrayList<ImageView> imagees=new ArrayList<>();
    ObservableList<ImageView> images;




    //Image searchimage=new Image(getClass().getResourceAsStream("/img/white-headphone-dual-red-yellow-background_23-2147889914.jpg"));
    public void initialize(URL url, ResourceBundle resourceBundle){

        progressBar = new JFXProgressBar();
        progressBar.setMinWidth(350);

    //searchtab.setClosable(true);
    //searchtab.setGraphic(new ImageView(searchimage));

    musicFiles=new importMusics().imports();
        if(musicFiles!=null) {
            try {
                System.out.println("is is not empty");
                createHbox();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        initdrawer();
        initdrawer2();
            musicFiles.addListener(new ListChangeListener<File>() {
                @Override
                public void onChanged(Change<? extends File> c) {
                    if(musicFiles.size()<0){
                        artist.setDisable(true);
                        Albums.setDisable(true);
                    }
                    else {
                        artist.setDisable(true);
                        Albums.setDisable(true);

                    }

                }
            });
        //borderPane.setBottom();
    };

//bg.fitWidthProperty().bind(borderPane.widthProperty());
  //      bg.fitHeightProperty().bind(borderPane.heightProperty());
    //bg.fitWidthProperty().bind(borderPane.getScene().widthProperty());



    public void initdrawer(){


        try {
            VBox side= FXMLLoader.load(getClass().getResource("side.fxml"));
            drawer.setSidePane(side);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HamburgerSlideCloseTransition transition = new HamburgerSlideCloseTransition(hamburger);
        transition.setRate(-1);
        hamburger.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            transition.setRate(transition.getRate()*-1);
            transition.play();
            if(drawer.isOpened()){
                drawer.close();
            }
            else{
                try{
                    borderPane.setVisible(true);}
                catch (Exception e){

                }
                drawer.open();
            }
        });


    }
    public void importsFile(ActionEvent event) throws IOException {

        musicFiles=new importMusics(event).importss();
        if(musicFiles!=null) createHbox();


    }
    public void createHbox() throws IOException {

        //musicTemplate music=new musicTemplate(musicFiles.get(0));
        if(musicFiles!=null){
            flowPane.getChildren().remove(emptyVBox);
             addall();
          // importlayout();
        }
    }

    public void addall() throws IOException {
        int index=0;
        musicTemplate temp=new musicTemplate();
        work=model.songs2(musicFiles,templates);
        for(File s:musicFiles) {
            DecimalFormat df = new DecimalFormat("#.00");
            long size = s.length() / (1024*1024);
            templates.add(new musicTemplate());
            flowPane.getChildren().add(templates.get(index).createTemp(s,index, size+" MB"));
            medias.add(new Media(musicFiles.get(index).toURI().toString()));
            //templates.get(index).setImage((Image)medias.get(index).getMetadata().get("image"));
            int finalIndex = index;
            templates.get(index).setSize(Math.round(size) + "MB");
            flowPane.getChildren().get(index).setOnMouseClicked(a->{
                bottomclass.play(finalIndex);
            });
            index++;

            // System.out.println("not null so it reached here");
            //flowPane.setAlignment(Pos.TOP_LEFT);
        }
        new Thread((Runnable) work).start();
        ((Task<String>) work).setOnSucceeded(event -> {
            System.out.println("The task succeeded.");
            System.gc();
            try {
                Thread.sleep(3000);
                work=null;
                work2=model.songs3(musicFiles,templates,artisticons,artistsongs);
                new Thread((Runnable) work2).start();
            } catch ( InterruptedException e) {
                e.printStackTrace();
            }
            ((Task<String>) work).setOnSucceeded(events -> {
                System.out.println("The task succeeded.");
                System.gc();
                try {

                    Thread.sleep(3000);
                    work=null;
                    work2=null;
                    model=null;
                    System.gc();
                } catch ( InterruptedException e) {
                    e.printStackTrace();
                }


            });

        });
        ((Task<String>) work).setOnCancelled(event -> {
            System.out.println("The task is canceled.");
        });
        ((Task<String>) work).setOnFailed(event -> {
            System.out.println("The task failed.");
        });
    }

    public void initdrawer2() {


        try {
            bottomclass=new drawerController(musicFiles,templates);
            AnchorPane side = bottomclass;
            drawer2.setSidePane(side);
        } catch (Exception e) {
            e.printStackTrace();
        }
        transition2 = new HamburgerSlideCloseTransition(hamburger2);
        transition2.setRate(-1);
        hamburger2.addEventHandler(MouseEvent.MOUSE_CLICKED, (Event event) -> {
            transition2.setRate(transition2.getRate() * -1);
            transition2.play();
            if (drawer2.isOpened()) {
                drawer2.close();
            } else {
                try {
                    borderPane.setVisible(true);
                } catch (Exception e) {

                }
                drawer2.open();
            }
        });

    }
    ObservableList<musicTemplate> templates=FXCollections.observableArrayList();
    ObservableList<Media> medias=FXCollections.observableArrayList();
    Worker<String> work=null;
    Worker<String> work2=null;
    getMetadata model=new getMetadata();
    HamburgerSlideCloseTransition transition2;
    drawerController bottomclass;
    @FXML
    SplitPane split;
    @FXML
    FlowPane artisticons;
    @FXML
    VBox artistsongs;

    //VBox vbox2=new VBox();


}


