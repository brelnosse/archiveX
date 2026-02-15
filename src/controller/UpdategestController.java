package controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Connexion;
import model.SharedData;

public class UpdategestController implements Initializable{
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
	
	private AddgestController ds = new AddgestController();
	private AdminconController ac = new AdminconController();
	private String adminID = SharedData.getInstance().getData();
	
	Connection con = null;
	Statement st = null;
	PreparedStatement pst = null;
	
	@FXML
	public void handleOpenAdminpanel(ActionEvent ev) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/vue/updategestform.fxml"));
		Stage stage = (Stage)((Node) ev.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
	}
	@FXML
	public void generateRandomCode(ActionEvent e) throws SQLException {
		Connection con = (new Connexion()).connect();
		Statement st = con.createStatement();
		String codeStr = ds.randomCode();
		String query = "SELECT code_conn FROM gestionnaire";
		ResultSet result = st.executeQuery(query);	
		ArrayList<String> codes = new ArrayList<>();
		while(result.next()) {
			codes.add(result.getString("code_conn"));
		}
		while(codes.contains(codeStr)) {
			codeStr = ds.randomCode();
		}
		code.setText(codeStr+"");
	}
	
	@FXML
	public void handleButtonClick(ActionEvent ev) throws SQLException {
		msgError.setVisible(false);
		nomErr.setVisible(false);
		prenomErr.setVisible(false);
		emailErr.setVisible(false);
		codeErr.setVisible(false);
		String nomVal = nom.getText().trim().toLowerCase(),
			   prenomVal = prenom.getText().trim().toLowerCase(),
			   emailVal = email.getText().trim().toLowerCase(),
			   codeVal = code.getText().trim();
		
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
					pst = con.prepareStatement("SELECT * FROM gestionnaire WHERE id != ? AND (code_conn = ? OR email = ?)");
					pst.setInt(1, Integer.parseInt(SharedData.getSecIntance().getData2())); 
					pst.setString(2, codeVal);
					pst.setString(3, emailVal);
					ResultSet r = pst.executeQuery();
					
					if(!r.next() && nomVal.length() >= 3 && prenomVal.length() >= 3 && ac.checkEmail(emailVal) == true) {
						pst = con.prepareStatement("UPDATE gestionnaire SET nom = ?, prenom = ?, email = ?, code_conn = ? WHERE id = ?");
						pst.setString(1, nomVal);
						pst.setString(2, prenomVal);
						pst.setString(3, emailVal);
						pst.setString(4, codeVal);
						pst.setInt(5, Integer.parseInt(SharedData.getSecIntance().getData2()));
						pst.executeUpdate();
						msgError.setText("Modification reussi !");
						msgError.setVisible(true);							
					}else {
						if(ac.checkEmail(emailVal) == false) {
							emailErr.setVisible(true);		
						}else if(nomVal.length() < 3) {
							nomErr.setVisible(true);
						}else if(prenomVal.length() < 3) {
							prenomErr.setVisible(true);
						}else if(!r.next()) {
							msgError.setText("Il est possible qu'un autre utilisateur ai les memes informations que celles renseigner.");
							msgError.setVisible(true);	
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}else {
				alert.close();
			}
		});

	}
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Connection con = (new Connexion()).connect();
		
		try {
			Statement st = con.createStatement();
			String query = "SELECT * FROM gestionnaire WHERE id = " + Integer.parseInt(SharedData.getSecIntance().getData2());
			ResultSet result = st.executeQuery(query);
			
			while(result.next()) {
				pseudoChar.setText(result.getString("nom").toUpperCase().charAt(0)+"");
				nom.setText(result.getString("nom"));
				prenom.setText(result.getString("prenom"));
				email.setText(result.getString("email"));
				code.setText(result.getString("code_conn"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
