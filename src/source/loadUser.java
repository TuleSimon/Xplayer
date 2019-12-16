package source;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class loadUser {
    public Worker<String> worker;
    public AtomicBoolean neverOpen = new AtomicBoolean(true);


    public loadUser(ObservableList<File> list,FlowPane flowPane/*,VBox emptyVBox*/){
        //flowPane.getChildren().remove(emptyVBox);

        worker = new Task<String>() {
            @Override
            protected String call() throws Exception {
                updateTitle("Loading data");
                updateMessage("Starting...");
                final int total = 40;
                System.out.println("size is " +total);
                updateProgress(0, total);
                for (int i = 0; i <= total; i++) {
                    if (isCancelled()) {
                        updateValue("Canceled at " + System.currentTimeMillis());
                       return null; // ignored
                    }
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        updateValue("Canceled at " + System.currentTimeMillis());
                        return null; // ignored
                         }
                    System.out.println(" here "+i);
                    System.out.println(" imporing "+list.get(i).getName());
                    flowPane.setAlignment(Pos.TOP_LEFT);
                    //flowPane.getChildren().add(new musicTemplate(list.get(i)).createTemp(new Label(list.get(i).getName()),i));
                        updateTitle("IMPORTING  " + total + "MEDIA FILES ");
                        updateMessage("Processing " + i + " of " + total + " items.");
                        updateProgress(i, total);
                    }
                    return "Completed at " + System.currentTimeMillis();
                }
                @Override
                protected void scheduled(){
                    System.out.println("The task is scheduled.");
                }
                @Override
                protected void running (){
                    System.out.println("The task is running.");
                }
            };


        }

    public void metaadd(List<Button> buts, List<String> address, List<ImageView> image, int index, Label art, Label year){
        Media media=new Media(address.get(index));
        media.getMetadata().addListener(new MapChangeListener<String,Object>(){
            @Override
            public void onChanged(MapChangeListener.Change<? extends String, ? extends Object> change) {
                handleMetadata(change.getKey(),change.getValueAdded(),buts.get(index),image,index,art,year);
  /*  m.setOnReady(new Runnable(){
            @Override
            public void run() {
                Image mi=(Image)m.getMedia().getMetadata().get("image");
                String sub= buts.get(index).getText().substring(0, 1);
    image.get(index).setImage(mi);
            }

    });
    */
            }
        });

    }

    private void handleMetadata(String key, Object value,Button b,List<ImageView> image,int index,Label art,Label year) {
        if (key.equals("title")) {
            try{
                String sub= b.getText().substring(0, 2);
                b.setText(sub +" "+value.toString());}
            catch(Exception e){}
        }
        if (key.equals("image")) {
            String sub= b.getText().substring(0, 1);
            image.get(index).setImage((Image)value);
        }
        if (key.equals("year")) {

            year.setText(value.toString());}

        if (key.equals("artist")) {
            art.setText(value.toString());
        }
    }
}




