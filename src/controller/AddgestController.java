package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import model.Connexion;
import model.Gestionnaire;
import model.SharedData;
public class AddgestController {
	@FXML
	private Button returnBtn;
	@FXML
	private TextField nom;
	@FXML
	private TextField prenom;
	@FXML
	private TextField email;
	@FXML
	private TextField code;
	@FXML
	private Label pseudoChar;
	@FXML
	private Label msgError;
	@FXML
	private Label nomErr;
	@FXML
	private Label prenomErr;
	@FXML
	private Label emailErr;
	@FXML
	private Label codeErr;
	private String adminID = SharedData.getInstance().getData();
	
	public void handleOpenAdminpanel(ActionEvent event) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/vue/adminpanel.fxml"));
		Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
		
		stage.setScene(new Scene(root));
	}
	public String randomCode() throws SQLException {
		String code = "";
		char[] characters = new char[26];
		for(int i = 0; i < 26; i++) {
			characters[i] = (char)('A'+i);
		}
		Random rand = new Random();
		
		char l1 = characters[rand.nextInt(26)];
		char l2 = characters[rand.nextInt(26)];
		int n1 = rand.nextInt(9);
		int n2 = rand.nextInt(9);
		char l3 = characters[rand.nextInt(26)];
	
		code = l1+""+l2+""+n1+""+n2 +""+l3;

		return code;	
	}
	
	public void generateRandomCode(ActionEvent e) throws SQLException {
		Connection con = (new Connexion()).connect();
		Statement st = con.createStatement();
		String codeStr = randomCode();
		String query = "SELECT code_conn FROM gestionnaire";
		ResultSet result = st.executeQuery(query);	
		ArrayList<String> codes = new ArrayList<>();
		while(result.next()) {
			codes.add(result.getString("code_conn"));
		}
		while(codes.contains(codeStr)) {
			codeStr = randomCode();
		}
		code.setText(codeStr+"");
	}
	
	@FXML
	public void handleKeyTyped(KeyEvent ev) {
		if(!nom.getText().trim().isEmpty()) {
			pseudoChar.setText(nom.getText().trim().toUpperCase().charAt(0)+"");
		}else {
			pseudoChar.setText("");
		}
	}
	public void handleButtonClick(ActionEvent event) throws SQLException {
		msgError.setVisible(false);
		nomErr.setVisible(false);
		prenomErr.setVisible(false);
		emailErr.setVisible(false);
		codeErr.setVisible(false);
		String nomVal = nom.getText().trim().toLowerCase(),
			   prenomVal = prenom.getText().trim().toLowerCase(),
			   emailVal = email.getText().trim().toLowerCase(),
			   codeVal = code.getText().trim();
		
		AdminconController adc = new AdminconController();
		if(nomVal.length() >= 3 && prenomVal.length() >= 3 && adc.checkEmail(emailVal) == true && codeVal.length() == 5) {
			Gestionnaire gest = new Gestionnaire(nomVal, prenomVal, emailVal, codeVal, Integer.parseInt(adminID));
			
			if(gest.save() == true) {
				msgError.setText("Gestionnaire ajoute !");
				msgError.setVisible(true);
			}else {
				msgError.setText("Un utilisateur avec la meme adresse email existe deja.");
				msgError.setVisible(true);
			}
		}else {
			if(nomVal.length() < 3) {
				nomErr.setVisible(true);
			}else if(prenomVal.length() < 3) {
				prenomErr.setVisible(true);
			}else if(adc.checkEmail(emailVal) == false) {
				emailErr.setVisible(true);
			}else if(codeVal.length() != 5) {
				codeErr.setVisible(true);
			}
		}
	}
	@FXML
	public void initialize() {
		
	}
}
