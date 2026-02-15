package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.Connexion;
import model.Gestionnaire;
import model.SharedData;

public class UpdategestformController implements Initializable{

    @FXML
    private TableColumn<Gestionnaire, Button> editCol;
    
    @FXML
    private TableColumn<Gestionnaire, String> emailCol;

    @FXML
    private TableView<Gestionnaire> gestTable;

    @FXML
    private TableColumn<Gestionnaire, Integer> idCol;
    
    @FXML
    private TableColumn<Gestionnaire, String> codeCol;

    @FXML
    private TableColumn<Gestionnaire, String> nomCol;

    @FXML
    private TableColumn<Gestionnaire, String> prenomCol;

    @FXML
    private Button returnBtn;

    ObservableList<Gestionnaire> gestList = FXCollections.observableArrayList();
    
    @FXML
    void handleOpenAdminpanel(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/vue/adminpanel.fxml"));
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
    }
    
    Connection con = null;
    String q = null;
    PreparedStatement preparedStatement = null;
    ResultSet r = null;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    	try {
			loadData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }
    public void handleOpenUpdategest(ActionEvent ev) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("/vue/updategest.fxml"));
    	Stage stage = (Stage)((Node) ev.getSource()).getScene().getWindow();
    	stage.setScene(new Scene(root));
    }
    @FXML
    void refreshTable() throws SQLException {
    	gestList.clear();

    	q = "SELECT * FROM gestionnaire";
    	preparedStatement = con.prepareStatement(q);
    	r = preparedStatement.executeQuery();
    	
    	while(r.next()) {
    		gestList.add(new Gestionnaire(r.getInt("id"), r.getString("nom"), r.getString("prenom"), r.getString("email"),r.getString("code_conn")));
    		gestTable.setItems(gestList);
    	}
    }
    public void loadData() throws SQLException {
    	
    	con = (new Connexion()).connect();
    	refreshTable();
    	
    	idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    	nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
    	prenomCol.setCellValueFactory(new PropertyValueFactory<>("prenom"));
    	codeCol.setCellValueFactory(new PropertyValueFactory<>("code_conn"));
    	emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    	editCol.setCellValueFactory(new PropertyValueFactory<>("upbtn"));
    	idCol.setStyle("-fx-alignment: CENTER");
    	codeCol.setStyle("-fx-alignment: CENTER");
    	editCol.setStyle("-fx-alignment: CENTER");    
    	emailCol.setStyle("-fx-alignment: CENTER");
    	for(Gestionnaire item: gestTable.getItems()) {
    		editCol.getCellObservableValue(item).getValue().setOnAction(e -> {
    			try {
    				SharedData.getSecIntance().setData2(((Button)e.getSource()).getId());
					handleOpenUpdategest(e);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
    		});
    	}
    }
}
