/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.imdb;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.DirettoreGrado;
import it.polito.tdp.imdb.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAdiacenti"
    private Button btnAdiacenti; // Value injected by FXMLLoader

    @FXML // fx:id="btnCercaAffini"
    private Button btnCercaAffini; // Value injected by FXMLLoader

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxRegista"
    private ComboBox<Director> boxRegista; // Value injected by FXMLLoader

    @FXML // fx:id="txtAttoriCondivisi"
    private TextField txtAttoriCondivisi; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.boxRegista.getItems().clear();
    	Integer anno = this.boxAnno.getValue();
    	if(anno==null) {
    		this.txtResult.setText("Scegliere un anno.");
    		return;
    	}
    	this.model.creaGrafo(anno);
    	this.txtResult.setText("Grafo creato.\n");
    	this.txtResult.appendText("Ci sono "+this.model.getVerticiSize()+" vertici\n");
		this.txtResult.appendText("Ci sono "+this.model.getArchiSize()+" archi\n");
		this.boxRegista.getItems().addAll(this.model.getVertici());
		this.boxRegista.setDisable(false);
    	this.btnAdiacenti.setDisable(false);
    	this.txtAttoriCondivisi.setDisable(false);
    	this.btnCercaAffini.setDisable(false);
    }

    @FXML
    void doRegistiAdiacenti(ActionEvent event) {
    	Director regista = this.boxRegista.getValue();
    	if(regista==null) {
    		this.txtResult.setText("Scegliere un regista.");
    		return;
    	}
    	this.txtResult.setText("REGISTI ADIACENTI A: "+regista.toString()+"\n");
    	List<DirettoreGrado> list = this.model.getAdiacenti(regista);
    	for(DirettoreGrado s : list) {
    		this.txtResult.appendText(s.getD().toString()+" - # attori condivisi: "+s.getN()+"\n");
    	}
    }

    @FXML
    void doRicorsione(ActionEvent event) {
    	Director regista = this.boxRegista.getValue();
    	if(regista==null) {
			this.txtResult.setText("Scegliere un regista.");
			return;
		}
    	String input = this.txtAttoriCondivisi.getText();
    	if(input=="") {
    		this.txtResult.setText("Inserire un numero di attori condivisi.");
    		return;
    	}
    	try {
    		int n = Integer.parseInt(input);
    		if(n>0) {
    			this.txtResult.setText("Gruppo creato.\n");
    			List<Director> l = this.model.calcolaGruppo(regista, n);
    			for(Director d : l) {
    				this.txtResult.appendText(d.toString()+"\n");
    			}
    		}
    		else {    		
    			this.txtResult.setText("Il valore deve essere un numero intero positivo.");
    			return;
    		}
     	} catch (NumberFormatException e) {
     		this.txtResult.setText("Il valore immesso non è valido.");
     		return;
     	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnAdiacenti != null : "fx:id=\"btnAdiacenti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCercaAffini != null : "fx:id=\"btnCercaAffini\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxRegista != null : "fx:id=\"boxRegista\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtAttoriCondivisi != null : "fx:id=\"txtAttoriCondivisi\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
   public void setModel(Model model) {
    	this.model = model;
    	for(int i=2004;i<2007;i++) {
    		this.boxAnno.getItems().add(i);
    	}
    	this.boxRegista.setDisable(true);
    	this.btnAdiacenti.setDisable(true);
    	this.txtAttoriCondivisi.setDisable(true);
    	this.btnCercaAffini.setDisable(true);
    	this.txtResult.setEditable(false);
    }
    
}
