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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Connexion;
import model.Gestionnaire;
import java.util.Date;

public class ShowgestController implements Initializable{
    @FXML
    private TableColumn<Gestionnaire, String> codeCol;

    @FXML
    private TableColumn<Gestionnaire, String> dateCol;

    @FXML
    private TableColumn<Gestionnaire, Button> docCol;

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
    @FXML
    private Button refreshBtn;

    @FXML
    private TextField searchbar;

    ObservableList<Gestionnaire> gestList = FXCollections.observableArrayList();
    Connection con = null;
    String q = null;
    PreparedStatement preparedStatement = null;
    ResultSet r = null;
    
    @FXML
    void handleOpenAdminpanel(ActionEvent event) throws IOException {
    	Parent root = FXMLLoader.load(getClass().getResource("/vue/adminpanel.fxml"));
    	Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
    	
    	stage.setScene(new Scene(root));
    }

    @FXML
    void launchSearch(ActionEvent event) throws SQLException {
    	String searchVal = searchbar.getText().trim().toLowerCase();
    	
    	if(!searchVal.isEmpty()) {
        	String qs = "SELECT * FROM gestionnaire WHERE nom LIKE '%"+searchVal+"%' OR prenom LIKE '%"+searchVal+"%' OR email LIKE '%"+searchVal+"%'";
        	refreshTable(qs);
    	}
    }

    @FXML
    void refreshTable(String ql) throws SQLException {
    	gestList.clear();
    	
    	preparedStatement = con.prepareStatement(ql);
    	r = preparedStatement.executeQuery();
    	
    	while(r.next()) {
    		String d = r.getString("date_ajout");
    		gestList.add(new Gestionnaire(r.getInt("id"), r.getString("nom"), r.getString("prenom"), r.getString("email"),r.getString("code_conn"), d, "f"));
    		gestTable.setItems(gestList);
    	}
    }
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		con = (new Connexion()).connect();
		try {
			refreshTable("SELECT * FROM gestionnaire");
			idCol.setCellValueFactory(new PropertyValueFactory("id"));
			nomCol.setCellValueFactory(new PropertyValueFactory("nom"));
			prenomCol.setCellValueFactory(new PropertyValueFactory("prenom"));
			emailCol.setCellValueFactory(new PropertyValueFactory("email"));
			codeCol.setCellValueFactory(new PropertyValueFactory("code_conn"));
			dateCol.setCellValueFactory(new PropertyValueFactory<Gestionnaire, String>("date"));
			docCol.setCellValueFactory(new PropertyValueFactory("showdoc"));
			editCol.setCellValueFactory(new PropertyValueFactory("showstat"));
	    	idCol.setStyle("-fx-alignment: CENTER");
	    	codeCol.setStyle("-fx-alignment: CENTER");
	    	dateCol.setStyle("-fx-alignment: CENTER");
	    	emailCol.setStyle("-fx-alignment: CENTER");
	    	editCol.setStyle("-fx-alignment: CENTER");   
	    	docCol.setStyle("-fx-alignment: CENTER");
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		refreshBtn.setOnAction(e-> {
			try {
				refreshTable("SELECT * FROM gestionnaire");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
	}

}
