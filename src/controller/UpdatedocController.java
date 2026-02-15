package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Connexion;
import model.SharedData;

public class UpdatedocController implements Initializable{
    @FXML
    private Button addBtn;

    @FXML
    private DatePicker date;

    @FXML
    private ComboBox<String> dctype;

    @FXML
    private TextField dep;

    @FXML
    private TextField dom;

    @FXML
    private ComboBox<String> fil;

    @FXML
    private Button impDoc;

    @FXML
    private TextField nomEn;

    @FXML
    private TextField nomEt;

    @FXML
    private Label stateLabel;

    @FXML
    private TextField suj;

    private String inputedFile = null;
    Connection con = null;
    PreparedStatement pst = null;
    
    @FXML
    void handleButtonClick(ActionEvent event) {
		String nomEtuVal = nomEt.getText().trim().toLowerCase(),
				   depVal = dep.getText().trim().toLowerCase(),
				   filVal = fil.getValue(),
				   sujVal = suj.getText().trim().toLowerCase(),
				   domVal = dom.getText().trim().toLowerCase(),
				   nomEnVal = nomEn.getText().trim().toLowerCase(),
				   dctypeVal = dctype.getValue();
			LocalDate dateVal = date.getValue();
		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Etes-vous sur ?");
		alert.setHeaderText("Etes-vous sur de vouloir valider les modifications ?");
		ButtonType yes = new ButtonType("Modifier");
		ButtonType no = new ButtonType("annuler");
		
		alert.getButtonTypes().setAll(yes, no);
		alert.showAndWait().ifPresent( response ->{
			if(response == yes) {
				con = (new Connexion()).connect();
				try {
					pst = con.prepareStatement("SELECT * FROM document WHERE id != ? AND file = ?");
					pst.setInt(1, Integer.parseInt(SharedData.getThIntance().getData3())); 
					pst.setString(2, inputedFile);
					ResultSet r = pst.executeQuery();
					
					if(!r.next() && !nomEtuVal.isEmpty() && !depVal.isEmpty() && !filVal.isEmpty() && !sujVal.isEmpty() && !domVal.isEmpty() && !nomEnVal.isEmpty() && dateVal != null && dctypeVal != null && inputedFile != null) {
						pst = con.prepareStatement("UPDATE document SET nomEtu = ?, nomEn = ?, file = ?, dept = ?, filiere = ?, sujet = ?, domaine = ?, type = ?, date_s = ? WHERE id = ?");
						pst.setString(1, nomEtuVal);
						pst.setString(2, nomEnVal);
						pst.setString(3, inputedFile);
						pst.setString(4, depVal);
						pst.setString(5, filVal);
						pst.setString(6, sujVal);
						pst.setString(7, domVal);
						pst.setString(8, dctypeVal);
						pst.setString(9, dateVal+"");
						pst.setInt(10, Integer.parseInt(SharedData.getThIntance().getData3()));
						pst.executeUpdate();
						showSuccess("Succes", "Modification reussi !");
					}else {
						if(nomEtuVal.isEmpty()) {
							showError("nom de/des etudiant(s)invalide", "le nom de l'etudiant est invalide");	
						}else if(depVal.isEmpty()) {
							showError("Departement invalide", "le departement est invalide");
						}else if(filVal.isEmpty()) {
							showError("Filiere invalide", "la filiere est invalide");
						}else if(!r.next()) {
							showError("Redondance","Il est possible qu'un autre utilisateur ai les memes informations que celles renseigner.");
						}else if(sujVal.isEmpty()) {
							showError("Sujet invalide", "le sujet est invalide");
						}else if(domVal.isEmpty()) {
							showError("Domaine invalide", "le domaine est invalide");				
						}else if(nomEnVal.isEmpty()) {
							showError("Nom de/des encadreur(s) invalide", "le domaine est invalide");								
						}else if( dateVal == null) {
							showError("Date invalide", "le date est invalide");
						}else if(domVal.isEmpty()) {
							showError("Domaine invalide", "le domaine est invalide");				
						}else if(dctypeVal == null) {
							showError("Type invalide", "le type est invalide");	
						}else if(inputedFile == null) {
							showError("Document invalide", "le document est invalide");							
						}
					}
				}catch (SQLException e) {
					e.printStackTrace();
				}
			}else {
				alert.close();
			}
		});
    }

    @FXML
    void handleOpenAdminpanel(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/vue/updatedocform.fxml"));
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		
		stage.setScene(new Scene(root));
    }

    @FXML
    public void uploadDoc() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Selectionnez un fichier PDF");
		
		FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("Fichiers PDF (*.pdf)", "*.pdf");
		fileChooser.getExtensionFilters().add(pdfFilter);
		
		File file = fileChooser.showOpenDialog(null);
		if(file != null) {
			try{
				if(!file.getName().toLowerCase().endsWith("pdf")) {
					showError("Format invalide", "Veuillez selectionner un ficher PDF");
					return;
				}
				Path destinationPath = Path.of("src/upload", file.getName());
				
				if(Files.exists(destinationPath)) {
					showError("Fichier exitant", "Un fichier avec le meme nom existe deja");
					return;
				}
				Files.copy(file.toPath(), destinationPath, StandardCopyOption.REPLACE_EXISTING);
				stateLabel.setText(file.getName());
				inputedFile = "src/upload/"+file.getName();
				showSuccess("Upload reussi", "Fichier uploader avec succes");
			}catch(IOException e) {
				showError("Erreur lors de l'upload", "Une erreur esr survenue lors de l'upload");
				e.printStackTrace();
			}
		}
    }
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		dep.setText("3IAC");
		fil.setValue("GL");
		dctype.setValue("PROJET TUTOREE");
		ObservableList<String> filList = FXCollections.observableArrayList();
		filList.add("GL");
		filList.add("PREPA 3IL");
		filList.add("RSI");
		filList.add("CSI");
		fil.setItems(filList);
		
		ObservableList<String> dctypeList = FXCollections.observableArrayList();
		dctypeList.add("PROJET TUTOREE");
		dctypeList.add("RAPPORT DE STAGE");
		dctypeList.add("MEMOIRE");
		dctype.setItems(dctypeList);
		
		con = (new Connexion()).connect();
		
		try {
			Statement st = con.createStatement();
			String query = "SELECT * FROM document WHERE id = " + Integer.parseInt(SharedData.getThIntance().getData3());
			ResultSet result = st.executeQuery(query);
			
			while(result.next()) {
				nomEt.setText(result.getString("nomEtu"));
				nomEn.setText(result.getString("nomEn"));
				dep.setText(result.getString("dept"));
				fil.setValue(result.getString("filiere"));
				suj.setText(result.getString("sujet"));
				dom.setText(result.getString("domaine"));
				dctype.setValue(result.getString("type"));
				date.setPromptText(result.getString("date_s"));
				LocalDate dt = LocalDate.parse(result.getString("date_a"));
				date.setValue(dt);
				inputedFile = result.getString("file");
				stateLabel.setText(inputedFile);
			}
			System.out.print(Integer.parseInt(SharedData.getThIntance().getData3()));
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}
	@FXML
	private void showSuccess(String t, String ht) {
		Alert msg = new Alert(Alert.AlertType.INFORMATION);
		msg.setTitle(t);
		msg.setHeaderText(ht);
		msg.showAndWait();
	}

	@FXML
	void showError(String e, String f) {
		Alert msg = new Alert(Alert.AlertType.ERROR);
		msg.setTitle(e);
		msg.setHeaderText(f);
		msg.showAndWait();		
	}
}
