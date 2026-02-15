package model;

import javafx.scene.control.Button;

public class Document {
	private int id;
	private String nomEt;
	private String nomEn;
	private Button doc;
	private String dep;
	private String fil;
	private String suj;
	private String dom;
	private String type;
	private Button upbtn;
	private Button delbtn;
	private Button viewbtn;
	private String date;
	private int nbretel;
	
	public Document(int id, String nomEt, String nomEn, String doc, String dep, String fil, String suj, String dom, String type) {
		this.id = id;
		this.nomEt = nomEt;
		this.nomEn = nomEn;
		this.doc = new Button(doc);
		this.doc.setStyle("-fx-background-color: rgb(255,0,105);-fx-text-fill: white;-fx-cursor: hand");
		this.doc.setId(id+"");
		this.dep = dep;
		this.fil = fil;
		this.suj = suj;
		this.dom = dom;
		this.type = type;
		this.upbtn = new Button("Modifier");
		this.upbtn.setId(id+"");
		this.upbtn.setStyle("-fx-background-color: rgb(255,0,105);-fx-text-fill: white;-fx-cursor: hand");
	}
	public Document(int id, String nomEt, String nomEn, String doc, String dep, String fil, String suj, String dom, String type, String p) {
		this.id = id;
		this.nomEt = nomEt;
		this.nomEn = nomEn;
		this.doc = new Button(doc);
		this.doc.setStyle("-fx-background-color: rgb(255,0,105);-fx-text-fill: white;-fx-cursor: hand");
		this.doc.setId(id+"");
		this.dep = dep;
		this.fil = fil;
		this.suj = suj;
		this.dom = dom;
		this.type = type;
		this.delbtn = new Button("Supprimer");
		this.delbtn.setId(id+"");
		this.delbtn.setStyle("-fx-background-color: rgb(255,0,105);-fx-text-fill: white;-fx-cursor: hand");
	}
	public Document(int id, String nomEt, String nomEn, String doc, String dep, String fil, String suj, String dom, String type, int nbretel, String dt) {
		this.id = id;
		this.nomEt = nomEt;
		this.nomEn = nomEn;
		this.doc = new Button(doc);
		this.doc.setStyle("-fx-background-color: rgb(255,0,105);-fx-text-fill: white;-fx-cursor: hand");
		this.doc.setId(id+"");
		this.dep = dep;
		this.fil = fil;
		this.suj = suj;
		this.dom = dom;
		this.type = type;
		this.nbretel = nbretel;
		this.date = dt;
		this.viewbtn = new Button("Details");
		this.viewbtn.setId(id+"");
		this.viewbtn.setStyle("-fx-background-color: rgb(255,0,105);-fx-text-fill: white;-fx-cursor: hand");
	}	
	public Document(int id, String nomEt, String nomEn, String doc, String dep, String fil, String suj, String dom, String type, int nbretel, String dt, String d) {
		this.id = id;
		this.nomEt = nomEt;
		this.nomEn = nomEn;
		this.doc = new Button(doc);
		this.doc.setId(id+"");
		this.doc.setStyle("-fx-background-color: rgb(255,0,105);-fx-text-fill: white;-fx-cursor: hand");
		this.dep = dep;
		this.fil = fil;
		this.suj = suj;
		this.dom = dom;
		this.type = type;
		this.nbretel = nbretel;
		this.date = dt;
		this.viewbtn = new Button("Telecharger");
		this.viewbtn.setId(id+"");
		this.viewbtn.setStyle("-fx-background-color: rgb(255,0,105);-fx-text-fill: white;-fx-cursor: hand");
	}	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNomEt() {
		return nomEt;
	}
	public void setNomEt(String nomEt) {
		this.nomEt = nomEt;
	}
	public String getNomEn() {
		return nomEn;
	}
	public void setNomEn(String nomEn) {
		this.nomEn = nomEn;
	}
	public Button getDoc() {
		return doc;
	}
	public void setDoc(Button doc) {
		this.doc = doc;
	}
	public String getDep() {
		return dep;
	}
	public void setDep(String dep) {
		this.dep = dep;
	}
	public String getFil() {
		return fil;
	}
	public void setFil(String fil) {
		this.fil = fil;
	}
	public String getSuj() {
		return suj;
	}
	public void setSuj(String suj) {
		this.suj = suj;
	}
	public String getDom() {
		return dom;
	}
	public void setDom(String dom) {
		this.dom = dom;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Button getUpbtn() {
		return upbtn;
	}
	public void setUpbtn(Button upbtn) {
		this.upbtn = upbtn;
	}
	public Button getDelbtn() {
		return delbtn;
	}
	public void setDelbtn(Button delbtn) {
		this.delbtn = delbtn;
	}
	public Button getViewbtn() {
		return viewbtn;
	}
	public void setViewbtn(Button viewbtn) {
		this.viewbtn = viewbtn;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String dt) {
		this.date = dt;
	}	
	public int getNbretel() {
		return nbretel;
	}
	public void setNbretel(int nb) {
		this.nbretel = nb;
	}
}
