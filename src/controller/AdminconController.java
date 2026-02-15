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
import model.Connexion;
import model.SharedData;

public class AdminconController {

	@FXML
	private TextField email;
	@FXML
	private PasswordField mdp;
	@FXML
	private Label emailError;
	@FXML
	private Label mdpError;
	@FXML
	private Label msgError;
	@FXML
	private Label stateBar;
	@FXML
	private Button button;

	public Boolean checkEmail(String emailVal) {
		Boolean result = false;
		emailVal = emailVal.trim().toLowerCase();
		if(emailVal != "") {
			String[] splitedArr = emailVal.split("@");
	 		if(splitedArr.length == 2) {
	 			String[] part2 = splitedArr[1].split("\\.");
				if(part2.length == 2) {
					if(part2[1].length() >= 1 && part2[1].length() <= 3) {
						result = true;
					}
				}
	 		}
 		}
 		return result;
	}
	public Boolean checkMdp(String mdpVal) {
		Boolean result = false;
		mdpVal = mdpVal.trim();
		
		if(mdpVal != "") {
			if(mdpVal.length() >= 4) {
				result = true;
			}
		}
 		return result;
	}	
	
	public Boolean checkMdp(String mdpVal, String vmdpVal) {
		Boolean result = false;
		mdpVal = mdpVal.trim();
		vmdpVal = vmdpVal.trim();
		
		if(mdpVal != "" && vmdpVal.equals(vmdpVal)) {
			if(mdpVal.length() >= 4) {
				result = true;
			}
		}
 		return result;
	}	
	
	@FXML
	private void handleOpenAdminpanel(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/vue/adminpanel.fxml"));
		Parent adminpanel = loader.load();
		Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(adminpanel));
	}

	@FXML
	private void handleButtonClick(ActionEvent e){
		emailError.setVisible(false);
		mdpError.setVisible(false);
		msgError.setVisible(false);
		stateBar.setText("");
		
		String emailVal = email.getText(),
			   mdpVal = mdp.getText();

		if(checkEmail(emailVal) == true && checkMdp(mdpVal) == true) {
			Connection con = (new Connexion()).connect();
			
			try {
				Statement st = con.createStatement();
				String query = "SELECT * FROM admin WHERE email = '"+emailVal+"' AND mdp = '"+mdpVal+"'";
				ResultSet result = st.executeQuery(query);
				while(result.next()) {
					try {
						SharedData.getInstance().setData(result.getInt("id")+"");
						handleOpenAdminpanel(e);
						System.out.println("Connexion reussi !");
					}catch(IOException f) {
						stateBar.setText("une erreur survenue lors du changement de scene");
					}
				}	
				if(!result.next()) {
					msgError.setVisible(true);
					
				}
			}catch(SQLException h) {
				h.printStackTrace();
				stateBar.setText("Une erreur est survenue lors de la connexion a la BD ou du traitement des donnees de la BD");
			}finally {
				try {
					con.close();
				}catch(SQLException i) {
					stateBar.setText("Une erreur est survenue lors de la fermeture de la connexion a la BD");
				}
			}
		}else {
			String errmsg;
			if (checkEmail(emailVal) == false)
				emailError.setVisible(true);
			else
				mdpError.setVisible(true);
		}
	}
}
