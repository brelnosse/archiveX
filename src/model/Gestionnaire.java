package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javafx.scene.control.Button;

public class Gestionnaire {
	private int id;
	private String nom;
	private String prenom;
	private String email; 
	private String code_conn;
	private String date;
	private int adminID;
	private Button upbtn;
	private Button delbtn;
	private Button showdoc;
	private Button showstat;
	
	public Gestionnaire(String n, String p, String e, String c, int mi) {
		this.nom = n;
		this.prenom = p;
		this.email = e;
		this.code_conn = c;
		this.adminID = mi;
	}
	public Gestionnaire(int i, String n, String p, String e, String c) {
		this.id = i;
		this.nom = n;
		this.prenom = p;
		this.email = e;
		this.code_conn = c;
		this.upbtn = new Button("Modifier");
		this.upbtn.setId(i+"");
		this.upbtn.setStyle("-fx-background-color: rgb(255,0,105);-fx-text-fill: white;-fx-cursor: hand");
	}	
	public Gestionnaire(int i, String n, String p, String e, String c, String fff) {
		this.id = i;
		this.nom = n;
		this.prenom = p;
		this.email = e;
		this.code_conn = c;
		this.delbtn = new Button("Supprimer");
		this.delbtn.setId(i+"");
		this.delbtn.setStyle("-fx-background-color: rgb(255,0,105);-fx-text-fill: white;-fx-cursor: hand");
	}
	public Gestionnaire(int i, String n, String p, String e, String c, String d, String f) {
		this.id = i;
		this.nom = n;
		this.prenom = p;
		this.email = e;
		this.code_conn = c;
		this.date = d;
		this.showdoc = new Button("Consulter");
		this.showdoc.setId(i+"");
		this.showdoc.setStyle("-fx-background-color: rgb(255,0,105);-fx-text-fill: white;-fx-cursor: hand");		
		this.showstat = new Button("Statistique");
		this.showstat.setId(i+"");
		this.showstat.setStyle("-fx-background-color: rgb(255,0,105);-fx-text-fill: white;-fx-cursor: hand");
	}
	public int getId() {
		return this.id;
	}
	public void setId(int nid) {
		this.id = nid;
	}
	public String getNom() {
		return this.nom;
	}
	public void setNom(String nn) {
		this.nom = nn;
	}
	public String getPrenom() {
		return this.prenom;
	}
	public void setPrenom(String np) {
		this.prenom = np;
	}
	public String getEmail() {
		return this.email;
	}
	public void setEmail(String ne) {
		this.email = ne;
	}
	public String getCode_conn() {
		return this.code_conn;
	}
	public void setCode_conn(String nc) {
		this.code_conn = nc;
	}
	public int getAdminID() {
		return this.adminID;
	}
	public void setAdminID(int na) {
		this.adminID = na;
	}
	public Button getUpbtn() {
		return this.upbtn;
	}
	public void setUpbtn(Button btn) {
		this.upbtn = btn;
	}
	public Button getDelbtn() {
		return this.delbtn;
	}
	public void setDelbtn(Button btn) {
		this.delbtn = btn;
	}
	public String getDate() {
		return this.date;
	}
	public void setDate(String d) {
		this.date = d;
	}
	public Button getShowdoc() {
		return this.showdoc;
	}
	public void setShowdoc(Button btn) {
		this.showdoc = btn;
	}
	public Button getShowstat() {
		return this.showstat;
	}
	public void setShowstat(Button btn) {
		this.showstat = btn;
	}
	
	public Boolean save() throws SQLException {
		Connection con = (new Connexion()).connect();
		Boolean res = false;
		Statement st = con.createStatement();
		String query = "SELECT * FROM gestionnaire WHERE email = '"+email+"' OR code_conn = '"+code_conn+"'";
		ResultSet result = st.executeQuery(query);
		
		if(!result.next()) {
			PreparedStatement pst = con.prepareStatement("INSERT INTO gestionnaire(nom, prenom, email, code_conn, adminID, date_ajout) VALUES(?, ?, ?, ?, ?, CURRENT_DATE"+""+")");
			pst.setString(1, this.nom);
			pst.setString(2, this.prenom);
			pst.setString(3, this.email);
			pst.setString(4, this.code_conn);
			pst.setInt(5, this.adminID);
			
			pst.executeUpdate();
			res = true;
		}else {
			res = false;
		}
		return res;
	}
}
