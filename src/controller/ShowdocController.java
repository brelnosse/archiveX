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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Connexion;
import model.Document;
import model.Gestionnaire;
import model.SharedData;

public class ShowdocController implements Initializable{
    @FXML
    private TableColumn<Document, String> date_aCol;

    @FXML
    private TableColumn<Document, String> depCol;

    @FXML
    private TableColumn<Document, Integer> dlnbreCol;

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
    private Button refreshBtn;

    @FXML
    private Button returnBtn;

    @FXML
    private TextField searchbar;
    @FXML
    private ComboBox<String> depCom;

    @FXML
    private ComboBox<String> filCom;

    @FXML
    private ComboBox<String> typeCom;
    @FXML
    private TableColumn<Document, String> sujCol;

    @FXML
    private TableColumn<Document, String> typeCol;
    private HostServices hostServices;


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
	public void showError(String q, String r) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.setTitle(q);
		alert.setHeaderText(r);
		alert.showAndWait();
	}
    ObservableList<Document> docList = FXCollections.observableArrayList();
	ObservableList<String> depList = FXCollections.observableArrayList();
	ObservableList<String> filList = FXCollections.observableArrayList();
	ObservableList<String> typeList = FXCollections.observableArrayList();
	
    Connection con = null;
    Statement st = null;
    PreparedStatement preparedStatement = null;
    ResultSet r = null;
    ResultSet rt = null;
    
    @FXML
    void handleOpenAdminpanel(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/vue/adminpanel.fxml"));
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		
		stage.setScene(new Scene(root));
    }

    @FXML
    void refreshTable(String ql) throws SQLException {
    	docList.clear();

    	preparedStatement = con.prepareStatement(ql);
    	rt = preparedStatement.executeQuery();
    	while(rt.next()) {
        	String f = "SELECT COUNT(*) FROM telechargement WHERE id_file = "+rt.getInt("id");
    		Statement p = con.createStatement();
    		ResultSet fd = p.executeQuery(f);
    		
        	double v = 0.0;
        	if(!fd.next()) {
        		docList.add(new Document(rt.getInt("id"), rt.getString("nomEtu"), rt.getString("nomEn"), rt.getString("file"), rt.getString("dept"), rt.getString("filiere"), rt.getString("sujet"), rt.getString("domaine"), rt.getString("type"), 0, rt.getString("date_s")));
        		gestTable.setItems(docList);
        	}else {
        		docList.add(new Document(rt.getInt("id"), rt.getString("nomEtu"), rt.getString("nomEn"), rt.getString("file"), rt.getString("dept"), rt.getString("filiere"), rt.getString("sujet"), rt.getString("domaine"), rt.getString("type"), fd.getInt(1), rt.getString("date_s")));
        		gestTable.setItems(docList);
        	}
    	}
    }
    @FXML
    void launchSearch(ActionEvent event) throws SQLException {
    	String searchVal = searchbar.getText().trim().toLowerCase();
    	
    	if(!searchVal.isEmpty()) {
        	String qs = "SELECT * FROM document WHERE nomEtu LIKE '%"+searchVal+"%' OR nomEn LIKE '%"+searchVal+"%' OR sujet LIKE '%"+searchVal+"%' OR domaine LIKE '%"+searchVal+"%'";
        	refreshTable(qs);
    	}
    }
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			loadData();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		refreshBtn.setOnAction(e-> {
			try {
				refreshTable("SELECT * FROM document");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
		depList.add("3IAC");
		depCom.setValue("3IAC");
		depCom.setItems(depList);
		
		filList.add("GL");
		filList.add("PREPA 3IL");
		filList.add("RSI");
		filList.add("CSI");
		filCom.setItems(filList);
		filCom.setValue("GL");
		
		typeCom.setValue("PROJET TUTOREE");
		typeList.add("PROJET TUTOREE");
		typeList.add("RAPPORT DE STAGE");
		typeList.add("MEMOIRE");
		typeCom.setItems(typeList);
			
	    depCom.setOnAction(e -> {
			try {
				refreshTable("SELECT * FROM document WHERE dep = '"+depCom.getValue()+"'");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		});
	    filCom.setOnAction(e -> {
			try {
				refreshTable("SELECT * FROM document WHERE filiere = '"+filCom.getValue()+"'");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}	    	
	    });
	    typeCom.setOnAction(e -> {
			try {
				refreshTable("SELECT * FROM document WHERE type = '"+typeCom.getValue()+"'");
			} catch (SQLException e1) {
				e1.printStackTrace();
			}	 	    	
	    });
	}
	@FXML
	public void loadData() throws SQLException {
		con = (new Connexion()).connect();
		refreshTable("SELECT * FROM document");
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		nomEtCol.setCellValueFactory(new PropertyValueFactory<>("nomEt"));
		nomEnCol.setCellValueFactory(new PropertyValueFactory<>("nomEn"));
		docCol.setCellValueFactory(new PropertyValueFactory<>("doc"));
		filCol.setCellValueFactory(new PropertyValueFactory<>("fil"));		
		depCol.setCellValueFactory(new PropertyValueFactory<>("dep"));
		sujCol.setCellValueFactory(new PropertyValueFactory<>("suj"));
		domCol.setCellValueFactory(new PropertyValueFactory<>("dom"));
		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		date_aCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		dlnbreCol.setCellValueFactory(new PropertyValueFactory<>("nbretel"));
		editCol.setCellValueFactory(new PropertyValueFactory<>("viewbtn"));
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
    	dlnbreCol.setStyle("-fx-alignment: CENTER");
    	for(Document item: gestTable.getItems()) {
    		editCol.getCellObservableValue(item).getValue().setOnAction(e -> {
//    			try {
//    				SharedData.getThIntance().setData3(((Button)e.getSource()).getId());
//					handleOpenUpdatedoc(e);
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
    		});
    		docCol.getCellObservableValue(item).getValue().setOnAction(e -> {
    			ouvrirPDF(new File(((Button)e.getSource()).getText()));
    		});
    	}
	}
}
