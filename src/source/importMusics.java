package source;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.net.MalformedURLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class importMusics {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    File file=null;
    File[] fileList;
    Connection conn;
    PreparedStatement statement;
    ResultSet resultSet;
    ArrayList<File> music=new ArrayList<>();
    ObservableList<File> musics = null;
    ActionEvent e;

    //COnstructor
    public importMusics(){
        try {
            conn=new DBconnection().connectDatabase();
        } catch (ClassNotFoundException e) {
            System.out.println("error found while adding directory");
        }
        directoryChooser.setTitle("Select A Directory to Scan");
        File recordsDir = new File(System.getProperty("user.home"), "Music");
        if (! recordsDir.exists()) {
            recordsDir.mkdirs();
        }
        directoryChooser.setInitialDirectory(recordsDir);
    }
    public importMusics(ActionEvent event){
        this.e=event;
        try {
            conn=new DBconnection().connectDatabase();
        } catch (ClassNotFoundException e) {
            System.out.println("error found while adding directory");
        }
        directoryChooser.setTitle("Select A Directory to Scan");
        File recordsDir = new File(System.getProperty("user.home"), "Music");
        if (! recordsDir.exists()) {
            recordsDir.mkdirs();
        }
        directoryChooser.setInitialDirectory(recordsDir);
    }

    public ObservableList<File> imports(){
       if(fetchfromDataBase()){
           return musics;
       }
       return null;

    }
    //set DIrectory for importing
    public ObservableList<File> importss(){

        try{
         file=directoryChooser.showDialog(((Node)e.getSource()).getScene().getWindow());
         statement=conn.prepareStatement("INSERT INTO directory(Directoryname, Directoryaddress) VALUES(?,?)");

        }
        catch(Exception e){
            System.out.println("Cancelled");
        }

        if(file!=null){
            if(file.isFile()){
              //  System.out.println("is a file "+file.toURI());
                return null;
            }
            else if(file.isDirectory()){
                try {
                    System.out.println("is directory");
                    statement.setString(1,file.getName());
                    String url;
                    url= file.getPath().replace("\\", "/");
                    statement.setString(2,url);
                    boolean result=statement.execute();
                    //statement=null;
                    //url=null;
                    //statement.close();
                    //conn.close();
                //    System.out.println(result);
                } catch (SQLException e) {
                    System.out.println("error while adding "+e.getLocalizedMessage());

                }
                //directoryChooser.setInitialDirectory(file);
                String[] extensions={".mp3"};
                fileList = getMp3only(extensions);

            //ading files to aray list
                if(fileList.length>0){
                for (File file:fileList) {

                    music.add(file);
                }
                musics=FXCollections.observableList(music);
                System.out.println("everything is set");
                return musics;}
            }
            else{
                System.out.println("is not file nor Directory");
            }
    }
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle("NO MEDIA FOUND");
        alert.setHeaderText("ERROR NO MEDIA");
        alert.showAndWait();
        alert=null;
    System.out.println("is null");
    return null;
    }

    public boolean fetchfromDataBase(){

        try {
            conn=new DBconnection().connectDatabase();
            statement=conn.prepareStatement("SELECT * FROM directory");

            resultSet=statement.executeQuery();
           //
            //System.out.println(str);
            if (resultSet.next()){
              String str=resultSet.getString("Directoryaddress");
              file=new File(str);
              if(file.exists()){
                fileList=getMp3only(extensions);
                if(fileList.length>0){
                for (File files:fileList) {
                //    System.out.println(files.toURI()+" 2");
                  //  System.out.println("adding "+files.getName());
                    music.add(files);
                }
                musics=FXCollections.observableList(music);
                }
              }

                while(resultSet.next()){
                    str=resultSet.getString("Directoryaddress");
                    file=new File(str);
                    if(file.exists()){
                        fileList=getMp3only(extensions);
                    if(fileList.length>0){
                    for (File files:fileList) {
                        //    System.out.println(files.toURI()+" 2");
                        //  System.out.println("adding "+files.getName());
                        music.add(files);
                    }
                        musics=FXCollections.observableList(music);
                    }
                    }
                }
               // System.out.println("everything is set so return true");
                conn.close();
                resultSet.close();
                statement.close();
                if(music.size()>0)
                return true;
                else
                    return false;
            }
            else{
                System.out.println("returning false ");
                return false;
                //importss();
            }

        }

        catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();

        }
        return false;
    }

    //Import mp3 files and set to observable list
    public File[] getMp3only(String[] ext){
       if(file!=null) {
           //System.out.println("we here");
           return file.listFiles(filter);
       }
       else{
           Alert alert=new Alert(Alert.AlertType.ERROR);
           alert.setTitle("NO MEDIA FOUND");
           alert.setHeaderText("ERROR NO MEDIA");
           alert.showAndWait();
       }
   //    System.out.println("file is null");
        return null;
   }

    FilenameFilter filter=new FilenameFilter() {
        @Override
        public boolean accept(File pathname, String name) {
            for(int i=0;i<extensions.length;i++){

                if(name.endsWith(extensions[i])){

                    return true;
                }
            }
            return false;
        }
    };
    String[] extensions={".mp3"};
}
