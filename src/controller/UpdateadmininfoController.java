package controller;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.SharedData;
import model.Connexion;
public class UpdateadmininfoController {
	@FXML
	private Button returnBtn;
	@FXML
	private Label pseudoChar;
	@FXML
	private TextField pseudo;
	@FXML
	private TextField email;
	@FXML
	private PasswordField mdp;
	@FXML
	private PasswordField vmdp;
	@FXML
	private Button applyBtn;
	@FXML
	private Label msgError;
	
	private String adminID = SharedData.getInstance().getData();
	
	@FXML
	public void handleOpenAdminpanel(ActionEvent ev) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/vue/adminpanel.fxml"));
		Stage stage = (Stage)((Node) ev.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
	}
	public void handleButtonClick(ActionEvent ev) throws SQLException {
		AdminconController adminCon = new AdminconController();
		if(adminCon.checkEmail(email.getText()) == true && adminCon.checkMdp(mdp.getText(), vmdp.getText()) == true && pseudo.getText().trim().length() >= 3) {
			Connection con = (new Connexion()).connect();
			
			Statement st = con.createStatement();
			String query = "SELECT * FROM admin WHERE id != "+Integer.parseInt(adminID) + " AND (email = '"+email.getText().trim().toLowerCase()+"' OR pseudo = '"+pseudo.getText().trim().toLowerCase()+"') ";
			System.out.print(query);
			ResultSet result = st.executeQuery(query);
			
			if(!result.next()) {
				PreparedStatement pst = con.prepareStatement("UPDATE admin SET pseudo = ?, email = ?, mdp = ? WHERE id = ?");
				pst.setString(1, pseudo.getText().trim().toLowerCase());
				pst.setString(2, email.getText().trim().toLowerCase());
				pst.setString(3, mdp.getText().trim());
				pst.setInt(4, Integer.parseInt(adminID));
				pst.executeUpdate();
//				System.out.print("Bonjour" + result.getString("pseudo"));
				msgError.setText("Mise a jour reussi !");
				msgError.setVisible(true);
			}else {
				msgError.setText("Il est possible qu'un autre utilisateur ai les memes informations que celles renseigner.");
				msgError.setVisible(true);
			}
			con.close();
		}else {
			if(adminCon.checkEmail(email.getText())==false) {
				msgError.setText("L'email est incorrect");
				msgError.setVisible(true);
			}else if(adminCon.checkMdp(mdp.getText(), vmdp.getText()) == false) {
				msgError.setText("Les mots de passe sont soit court (longueur minimal 4) soit ne correspondent pas.");
				msgError.setVisible(true);
			}
		}
	}
	@FXML
	public void initialize() throws SQLException {
		Connection con = null;
		try {
			con = (new Connexion()).connect();
			Statement st = con.createStatement();
			String query = "SELECT * FROM admin WHERE id = " + Integer.parseInt(adminID);
			ResultSet result = st.executeQuery(query);
			
			while(result.next()) {
				pseudoChar.setText(result.getString("pseudo").toUpperCase().charAt(0)+"");
				pseudo.setText(result.getString("pseudo"));
				email.setText(result.getString("email"));
				mdp.setText(result.getString("mdp"));
				vmdp.setText(result.getString("mdp"));
			}
		}finally {
			if(con != null) con.close(); 
		}
	}
}
