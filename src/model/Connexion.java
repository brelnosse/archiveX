package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Connexion {
	String url = "jdbc:mysql://localhost:3305/archive";
	String user = "root";
	String pwd = null;
	Connection conn = null;	
	
	public Connexion() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver..ok");
			
			conn = DriverManager.getConnection(url, user, pwd);
			System.out.print("Connexion... ok!!!");
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(Exception e) {
			System.out.println("Erreur de connexion");
		}
	}
	
	public Connection connect() {
		return this.conn;
	}

}
