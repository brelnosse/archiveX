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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Connexion;

public class AdddocController implements Initializable {

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
    private TextField nomEn;

    @FXML
    private TextField nomEt;

    @FXML
    private Button returnBtn;

    @FXML
    private TextField suj;
    
    @FXML
    private Button impDoc;
    @FXML
    private Label stateLabel;
    
    private String inputedFile = null;
    
    @FXML
	public void handleOpenAdminpanel(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/vue/adminpanel.fxml"));
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
	@FXML
	public void handleButtonClick(ActionEvent event) throws SQLException {
		Connection con = (new Connexion()).connect();
		String nomEtuVal = nomEt.getText().trim().toLowerCase(),
			   depVal = dep.getText().trim().toLowerCase(),
			   filVal = fil.getValue(),
			   sujVal = suj.getText().trim().toLowerCase(),
			   domVal = dom.getText().trim().toLowerCase(),
			   nomEnVal = nomEn.getText().trim().toLowerCase(),
			   dctypeVal = dctype.getValue();
		LocalDate dateVal = date.getValue();
		Statement st = con.createStatement();
		ResultSet re = st.executeQuery("SELECT * FROM document WHERE nomEtu = '"+nomEtuVal+"' AND nomEn = '"+nomEnVal+"' AND file = '"+inputedFile+"' AND dept = '"+depVal+"' AND filiere = '"+filVal+"' AND sujet = '"+sujVal+"' AND domaine = '"+domVal+"' AND type = '"+dctypeVal+"' AND date_s = '"+dateVal+"'");
		
		if(!re.next()) {
		if(!nomEtuVal.isEmpty() && !depVal.isEmpty() && !filVal.isEmpty() && !sujVal.isEmpty() && !domVal.isEmpty() && !nomEnVal.isEmpty() && dateVal != null && dctypeVal != null && inputedFile != null) {
			PreparedStatement pstmt = con.prepareStatement("INSERT INTO document(nomEtu, nomEn, file, dept, filiere, sujet, domaine, type, date_s, date_a) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_DATE)");
			pstmt.setString(1, nomEtuVal);
			pstmt.setString(2, nomEnVal);
			pstmt.setString(3, inputedFile);
			pstmt.setString(4, depVal);
			pstmt.setString(5, filVal);
			pstmt.setString(6, sujVal);
			pstmt.setString(7, domVal);
			pstmt.setString(8, dctypeVal);
			pstmt.setString(9, dateVal+"");
			pstmt.executeUpdate();
			
			showSuccess("Ajout reussi", "Le document a ete ajoute avec succes");
			System.out.println("Ajout reussi !");
		}else {
			if(nomEtuVal.isEmpty()) {
				showError("Champ vide", "Veuillez renseigner le nom de/des l'etudiant(s)");
			}else if(depVal.isEmpty()) {
				showError("Champ vide", "Veuillez renseigner le departement");
			}else if(filVal.isEmpty()) {
				showError("Champ vide", "Veuillez renseigner la filiere");
			}else if(sujVal.isEmpty()) {
				showError("Champ vide", "Veuillez renseigner le sujet traite");				
			}else if(domVal.isEmpty()) {
				showError("Champ vide", "Veuillez renseigner le domaine");
			}else if(nomEnVal.isEmpty()) {
				showError("Champ vide", "Veuillez renseigner le nom de/des encadreurs");
			}else if(dateVal == null) {
				showError("Champ vide", "Veuillez selectionner la date");
			}else if(dctypeVal == null) {
				showError("Champ vide", "Veuillez renseigner le type de document");
			}else if(inputedFile == null) {
				showError("Champ vide", "Veuillez importer un document");
			}
		}
		}else {
			showError("Document present", "Un document avec les memes informations exite deja dans la base de donnees.");
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
	}

}
