package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Connexion;
import model.Gestionnaire;
import model.SharedData;
import javafx.fxml.Initializable;

public class DeletegestController implements Initializable{
	@FXML
    private TableColumn<Gestionnaire, String> codeCol;

    @FXML
    private TableColumn<Gestionnaire, Button> editCol;

    @FXML
    private TableColumn<Gestionnaire, String> emailCol;

    @FXML
    private TableView<Gestionnaire> gestTable;

    @FXML
    private TableColumn<Gestionnaire, Integer> idCol;

    @FXML
    private TableColumn<Gestionnaire, String> nomCol;

    @FXML
    private TableColumn<Gestionnaire, String> prenomCol;

    @FXML
    private Button returnBtn;
    
    ObservableList<Gestionnaire> gestList = FXCollections.observableArrayList();
    Connection con = null;
    String q = null;
    PreparedStatement preparedStatement = null;
    ResultSet r = null;
    
    @FXML
    void handleOpenAdminpanel(ActionEvent event) throws IOException{
    	Parent root = FXMLLoader.load(getClass().getResource("/vue/adminpanel.fxml"));
    	Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
    	
    	stage.setScene(new Scene(root));
    }

    @FXML
    void refreshTable() throws SQLException {
    	gestList.clear();

    	q = "SELECT * FROM gestionnaire";
    	preparedStatement = con.prepareStatement(q);
    	r = preparedStatement.executeQuery();
    	
    	while(r.next()) {
    		gestList.add(new Gestionnaire(r.getInt("id"), r.getString("nom"), r.getString("prenom"), r.getString("email"),r.getString("code_conn"), "f"));
    		gestTable.setItems(gestList);
    	}
    }

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		try {
			loadData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@FXML
	private void loadData() throws SQLException {
    	con = (new Connexion()).connect();
    	refreshTable();
    	
    	idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    	nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
    	prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
    	codeCol.setCellValueFactory(new PropertyValueFactory<>("code_conn"));
    	emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    	editCol.setCellValueFactory(new PropertyValueFactory<>("delbtn"));
    	idCol.setStyle("-fx-alignment: CENTER");
    	codeCol.setStyle("-fx-alignment: CENTER");
    	emailCol.setStyle("-fx-alignment: CENTER");
    	editCol.setStyle("-fx-alignment: CENTER");    	
    	for(Gestionnaire item: gestTable.getItems()) {
    		editCol.getCellObservableValue(item).getValue().setOnAction(e -> {
    			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    			alert.setTitle("Etes-vous sur ?");
    			alert.setHeaderText("Etes-vous vraiment sur de vouloir le supprimer ?");
    			ButtonType byes = new ButtonType("Supprimer");
    			ButtonType bno = new ButtonType("Annuler");
    			alert.getButtonTypes().setAll(byes, bno);
    			alert.showAndWait().ifPresent(response -> {
    				if(response == byes) {
    					Connection con = (new Connexion()).connect();
    					try {
	    					String query = "DELETE FROM gestionnaire WHERE id = ?";    						
							PreparedStatement pst = con.prepareStatement(query);
	    					pst.setInt(1, Integer.parseInt(((Button) e.getSource()).getId()));
	    					pst.executeUpdate();
	    					refreshTable();
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
    				}else {
    					alert.close();
    				}
    			});
    		});
    	}
		
	}

}
