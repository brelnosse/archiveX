package model;

import java.io.IOException;

import controller.DeletedocController;
import controller.ShowdocController;
import controller.UpdatedocformController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/admincon.fxml"));
        FXMLLoader sd = new FXMLLoader(getClass().getResource("/vue/showdoc.fxml"));
        FXMLLoader udf = new FXMLLoader(getClass().getResource("/vue/updatedocform.fxml"));
        FXMLLoader dddf = new FXMLLoader(getClass().getResource("/vue/deletedoc.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        
        Parent sdroot = sd.load();
        Parent udfroot = udf.load();
        Parent dddfroot = dddf.load();
        
        ShowdocController showdoc = sd.getController();
        UpdatedocformController updatedocform =  udf.getController();
        DeletedocController deletedoc = dddf.getController();
        
        showdoc.setHostServices(getHostServices()); 
        updatedocform.setHostServices(getHostServices()); 
        deletedoc.setHostServices(getHostServices()); 

        primaryStage.setTitle("ArchiveX");
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
