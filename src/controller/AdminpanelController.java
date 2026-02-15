package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.SharedData;
import model.Connexion;
public class AdminpanelController {
	@FXML
	private Label heure;
	@FXML
	private Label pseudoChar;
	@FXML
	private Label adminPseudo;
	@FXML
	private Button deconnectBtn;
	private int adminID = Integer.parseInt(SharedData.getInstance().getData());
//	private int adminID = 1;	
	public void handleOpenAdmincon(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/vue/admincon.fxml"));
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
	}
	
	public void handleOpenUpdateadmininfo(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/vue/updateadmininfo.fxml"));
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
	}
	public void handleOpenAddgest(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/vue/addgest.fxml"));
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		
		stage.setScene(new Scene(root));
	}
	public void handleOpenUpdategestform(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/vue/updategestform.fxml"));
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
	}
    @FXML
    public void handleOpenDeletegest(ActionEvent event) throws IOException{
    	Parent root = FXMLLoader.load(getClass().getResource("/vue/deletegest.fxml"));
    	Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
    	
    	stage.setScene(new Scene(root));
    }
    @FXML
    public void handleOpenShowgest(ActionEvent event) throws IOException{
    	Parent root = FXMLLoader.load(getClass().getResource("/vue/showgest.fxml"));
    	Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
    	
    	stage.setScene(new Scene(root));
    }
    @FXML
    public void handleOpenAdddoc(ActionEvent event) throws IOException{
    	Parent root = FXMLLoader.load(getClass().getResource("/vue/adddoc.fxml"));
    	Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
    	
    	stage.setScene(new Scene(root));
    }
    @FXML
    public void handleOpenUpdatedocform(ActionEvent event) throws IOException{
    	Parent root = FXMLLoader.load(getClass().getResource("/vue/updatedocform.fxml"));
    	Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
    	
    	stage.setScene(new Scene(root));
    }
    @FXML
    public void handleOpenDeletedocform(ActionEvent event) throws IOException{
    	Parent root = FXMLLoader.load(getClass().getResource("/vue/deletedoc.fxml"));
    	Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
    	
    	stage.setScene(new Scene(root));
    }
    @FXML
    public void handleShowdoc(ActionEvent event) throws IOException{
    	Parent root = FXMLLoader.load(getClass().getResource("/vue/showdoc.fxml"));
    	Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
    	
    	stage.setScene(new Scene(root));
    }
	public void showConfirmationDialog(ActionEvent ev) {
		Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation");
		alert.setHeaderText("Etes-vous sur de vouloir vous deconnectez ?");
		
		ButtonType buttonTypeYes = new ButtonType("Oui");
		ButtonType buttonTypeNo = new ButtonType("Non");
		
		alert.getButtonTypes().setAll(buttonTypeYes, buttonTypeNo);
		alert.showAndWait().ifPresent( response ->{
			if(response == buttonTypeYes) {
				try {
					handleOpenAdmincon(ev);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}else {
				alert.close();
			}
		});
	}

	@FXML
	public void initialize() throws SQLException{
		Connection con = (new Connexion()).connect();
		Statement st = con.createStatement();
		String query = "SELECT * FROM admin WHERE id = " + adminID;
		ResultSet result = st.executeQuery(query);
		
		while(result.next()) {
			pseudoChar.setText(result.getString("pseudo").toUpperCase().charAt(0)+"");
			adminPseudo.setText(result.getString("pseudo").toUpperCase().charAt(0)+""+result.getString("pseudo").substring(1));
		}
		//Modification du temps
		Timer timer = new Timer(true);
		timer.scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				Platform.runLater(()->{
					LocalTime tempsActuel = LocalTime.now();
					DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
					heure.setText(tempsActuel.format(formatter));
				});
			}
		},0, 1000);
	}
}
