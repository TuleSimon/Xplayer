package source;


    /*
     * To change this license header, choose License Headers in Project Properties.
     * To change this template file, choose Tools | Templates
     * and open the template in the editor.
     */
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;

/**
     *
     * @author simon
     */
    public class getMetadata {
    ObservableList<File> direct=FXCollections.observableArrayList();
    ObservableList<StringProperty> artist=FXCollections.observableArrayList();
        private Worker<String> worker;

        public Worker<String> songs2( ObservableList<File> musicfiles,ObservableList<musicTemplate> templates ) {
            ///File rootdirect=new File(musicfiles.get(0).getParent()+"\\thumbs\\");
            worker = new Task<String>() {
                @Override
                protected String call() throws Exception {

                    System.out.println("starting");
                    updateTitle("Example Task");
                    updateMessage("Starting...");
                    final int total = musicfiles.size();
                    updateProgress(0, total);
                    Thread.sleep(10000);
                    for (int i = 0; i < musicfiles.size(); i++) {
                        if(i==200)
                            break;
                        File rootdirect=new File(musicfiles.get(i).getParent()+"\\thumbs\\");
                        if(!rootdirect.exists()){
                            rootdirect.mkdir();
                        }
                        direct.add(new File(musicfiles.get(i).getParent()+"\\thumbs\\"+i+".jpg"));
                         if(direct.get(i).exists()){
                            System.out.println("exit");
                            continue;
                        }
                        if (isCancelled()) {
                            updateValue("Canceled at " + System.currentTimeMillis());
                            return null; // ignored
                        }
                        try {
                            System.gc();
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            updateValue("Canceled at " + System.currentTimeMillis());
                            return null;
                        }
                        updateTitle("Example Task (" + i + ")");
                        updateMessage("Processed " + i + " of " + total + " items.");
                        updateProgress(i, total);
                        System.out.println("starting" +i);
                        metaadd2(musicfiles.get(i),i,templates.get(i));
                    }
                    return "Completed at " + System.currentTimeMillis();
                }
                @Override
                protected void scheduled() {
                    System.out.println("The task is scheduled.");
                }
                @Override
                protected void running() {
                    System.out.println("The task is running.");
                }
            };

            return worker;
        }



    Media media;

        public void metaadd(File file, int index, musicTemplate template){
            MediaPlayer player =new MediaPlayer(new Media(file.toURI().toString()));
            MediaPlayer finalPlayer = player;
            player.getMedia().getMetadata().addListener(new MapChangeListener<String,Object>(){
                @Override
                public void onChanged(MapChangeListener.Change<? extends String, ? extends Object> change) {
                    handleMetadata(change.getKey(), change.getValueAdded(), index, template);
                }

    });
        player=null;
        }

        private void handleMetadata(String key, Object value,int index,musicTemplate template) {

            if (key.equals("title")) {
                try{template.setmusicname(value.toString());}
                catch(Exception e){}
            }
            if (key.equals("image") ) {
                //System.out.println("set is "+template.set);
              // BackgroundFill background_fill = new BackgroundFill( template.getmostDominant(new ImageView((Image) value)), CornerRadii.EMPTY, Insets.EMPTY);
                //template.setColor(background_fill);
              // template.setImage((Image)value);
                try{
                    if(direct.get(index).exists()){
                        ImageView view=new ImageView();
                      template.setImage(new Image(direct.get(index).toURI().toString()));}
                     //template.background_fill = new BackgroundFill( template.getmostDominant(template.imageview), CornerRadii.EMPTY, Insets.EMPTY);

                }
                catch (Exception e){

                }
            }

            if (key.equals("year")) {
                template.setyear(value.toString());}

            if (key.equals("artist")) {
                template.setartist(value.toString());
                System.out.println("changing "+artist.get(index)+" to "+value.toString());
                boolean add=true;
                for(StringProperty art:artist){
                    if(value.toString().equalsIgnoreCase(art.getValue())){
                        add=false;
                        break;
                    }
                                }
                if(add){
                    vb.getChildren().add(template.getTemp());
                    ((JFXButton)vb.getChildren().get(vb.getChildren().size()-1)).setOnAction(a->{
                        song.getChildren().remove(0,song.getChildren().size());
                        for(StringProperty art:artist){
                            if(value.toString().equalsIgnoreCase(art.getValue())){
                                //song.getChildren().add(templates1.get(artist.indexOf(art)).h2);
                                song.getChildren().add(templates1.get(artist.indexOf(art)).getTemp2());
                            }
                        }

                    });
                }
                artist.get(index).setValue(value.toString().toLowerCase());
            }
        }

    public void metaadd2(File file,int index, musicTemplate template) throws InterruptedException {
        MediaPlayer player = new MediaPlayer(new Media(file.toURI().toString()));
        MediaPlayer finalPlayer = player;

                player.getMedia().getMetadata().addListener(new MapChangeListener<String, Object>() {
                    @Override
                    public void onChanged(MapChangeListener.Change<? extends String, ? extends Object> change) {
                        handleMetadata2(change.getKey(), change.getValueAdded(), index);
                    }
                });


    }

            private void handleMetadata2(String key, Object value,int index) {

        if (key.equals("image")) {
            System.out.println("writing image");
            //  BackgroundFill background_fill = new BackgroundFill( template.getmostDominant(new ImageView((Image) value)), CornerRadii.EMPTY, Insets.EMPTY);
            //template.setColor(background_fill);
//             template.setImage((Image)value);
            BufferedImage bImage = SwingFXUtils.fromFXImage((Image)value, null);
            try {

                boolean st=ImageIO.write(bImage, "jpg", direct.get(index));
                System.out.println(st);

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }


    }
    FlowPane vb;
    VBox song;
    ObservableList<musicTemplate> templates1;
    public Worker<String> songs3(ObservableList<File> musicfiles, ObservableList<musicTemplate> templates, FlowPane vbs,VBox sg){
        this.vb=vbs;
        this.song=sg;
        this.templates1=templates;
        worker = new Task<String>() {
            @Override
            protected String call() throws Exception {
                System.out.println("starting");
                updateTitle("Example Task");
                updateMessage("Starting...");
                final int total = musicfiles.size();
                updateProgress(0, total);
                Thread.sleep(5000);

                for (int i = 0; i < total; i++) {
                    if(i==200)
                        break;
                    //direct(musicfiles.get(i).getParent()+"\\thumbs\\"+i+".jpg");
                    if (isCancelled()) {
                        updateValue("Canceled at " + System.currentTimeMillis());
                        return null; // ignored
                    }
                    try {
                        System.gc();
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        updateValue("Canceled at " + System.currentTimeMillis());
                        return null;
                    }
                    updateTitle("Example Task (" + i + ")");
                    updateMessage("Processed " + i + " of " + total + " items.");
                    updateProgress(i, total);
                    System.out.println("starting" +i);
                    artist.add(new SimpleStringProperty("unknown"));
                    metaadd(musicfiles.get(i),i,templates.get(i));
                }
                return "Completed at " + System.currentTimeMillis();
            }
            @Override
            protected void scheduled() {
                System.out.println("The task is scheduled.");
            }
            @Override
            protected void running() {
                System.out.println("The task is running.");
            }
        };
        ((Task<String>) worker).setOnSucceeded(events -> {
                                    String ty = "unknown";
                                    for (StringProperty art : artist) {
                                        if (ty.equalsIgnoreCase(art.getValue())) {
                                            song.getChildren().add(templates1.get(artist.indexOf(art)).getTemp2());
                                        }
                                    }
                                });
                System.out.println("sucessful task");


                    return worker;
    }



    // File outputFile = new File(file);
        }