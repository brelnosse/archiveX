package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javafx.application.HostServices;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import model.Document;
import model.SharedData;

public class DeletedocController implements Initializable{
    @FXML
    private TableColumn<Document, String> depCol;

    @FXML
    private TableColumn<Document, Button> docCol;

    @FXML
    private TableColumn<Document, String> domCol;

    @FXML
    private TableColumn<Document, Button> editCol;

    @FXML
    private TableColumn<Document, String> filCol;

    @FXML
    private TableView<Document> gestTable;

    @FXML
    private TableColumn<Document, Integer> idCol;

    @FXML
    private TableColumn<Document, String> nomEnCol;

    @FXML
    private TableColumn<Document, String> nomEtCol;

    @FXML
    private Button returnBtn;

    @FXML
    private TableColumn<Document, String> sujCol;

    @FXML
    private TableColumn<Document, String> typeCol;
    
    ObservableList<Document> docList = FXCollections.observableArrayList();
    Connection con = null;
    Statement st = null;
    ResultSet rt = null;
    
    @FXML
    private HostServices hostServices;

    // Méthode pour définir HostServices (à appeler depuis votre contrôleur principal)
    public void setHostServices(HostServices hostServices) {
        this.hostServices = hostServices;
    }
    
    public void ouvrirPDF(File file) {
        if (hostServices != null) {
            hostServices.showDocument(file.getAbsolutePath());
        }else {
        	showError("Erreur", "une erreur inattendu est survenue");
        } 
    }
	@FXML
	void showError(String e, String f) {
		Alert msg = new Alert(Alert.AlertType.ERROR);
		msg.setTitle(e);
		msg.setHeaderText(f);
		msg.showAndWait();		
	}
    @FXML
	public void handleOpenAdminpanel(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/vue/adminpanel.fxml"));
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		
		stage.setScene(new Scene(root));
	}
    @FXML
    public void refreshTable() throws SQLException {
    	docList.clear();
    	
    	st = con.createStatement();
    	rt = st.executeQuery("SELECT * FROM document");	
    	while(rt.next()) {
    		docList.add(new Document(rt.getInt("id"), rt.getString("nomEtu"), rt.getString("nomEn"), rt.getString("file"), rt.getString("dept"), rt.getString("filiere"), rt.getString("sujet"), rt.getString("domaine"), rt.getString("type"), ""));
    		gestTable.setItems(docList);
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
	public void loadData() throws SQLException {
		con = (new Connexion()).connect();
		refreshTable();
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		nomEtCol.setCellValueFactory(new PropertyValueFactory<>("nomEt"));
		nomEnCol.setCellValueFactory(new PropertyValueFactory<>("nomEn"));
		docCol.setCellValueFactory(new PropertyValueFactory<>("doc"));
		filCol.setCellValueFactory(new PropertyValueFactory<>("fil"));		
		depCol.setCellValueFactory(new PropertyValueFactory<>("dep"));
		sujCol.setCellValueFactory(new PropertyValueFactory<>("suj"));
		domCol.setCellValueFactory(new PropertyValueFactory<>("dom"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		editCol.setCellValueFactory(new PropertyValueFactory<>("delbtn"));
		idCol.setStyle("-fx-alignment: CENTER");
		nomEtCol.setStyle("-fx-alignment: CENTER");
		nomEnCol.setStyle("-fx-alignment: CENTER");
		filCol.setStyle("-fx-alignment: CENTER");   
		docCol.setStyle("-fx-alignment: CENTER");   
    	depCol.setStyle("-fx-alignment: CENTER");
    	sujCol.setStyle("-fx-alignment: CENTER");
    	domCol.setStyle("-fx-alignment: CENTER");
    	typeCol.setStyle("-fx-alignment: CENTER");    
    	editCol.setStyle("-fx-alignment: CENTER");  
    	for(Document item: gestTable.getItems()) {
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
	    					String query = "DELETE FROM document WHERE id = ?";    						
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
    		docCol.getCellObservableValue(item).getValue().setOnAction(e -> {
    			ouvrirPDF(new File(((Button)e.getSource()).getText()));
    		});
    	}
	}

}
