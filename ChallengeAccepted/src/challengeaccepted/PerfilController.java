/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package challengeaccepted;

import Database.Database;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;


public class PerfilController implements Initializable {
   
    @FXML private Label usrEmail;
    @FXML private Label usrPercAcertos;
    @FXML private ListView<Challenge> ListViewDesafios;
    @FXML private Label criouDes;
    
    private int userId;
    private ArrayList<Challenge> challenges;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        // Ao clicar num desafio, será enviado o EXERCISEID para a "atividade" do consultar exercicios
        ListViewDesafios.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Challenge>() {

            @Override
            public void changed(ObservableValue<? extends Challenge> observable, Challenge oldValue, Challenge newValue) {
                //newValue.getId()
            }
        });
    }    
    
    public void getUserAttributes(int userid){ //usar funcao para passar o user para o perfil
        
        this.userId = userid;
        Database db = new Database();
        usrEmail.setText(db.getUserEmail(this.userId));
        double tentativas = db.getTentativas(this.userId), 
            acertos = db.getAcertos(this.userId);
        if((int)tentativas != 0){
            usrPercAcertos.setText(String.valueOf((int)((acertos/tentativas) * 100)));
        }
        else{ //ou acertou todos os que fez ou nem acertou
            if (acertos <= 0) usrPercAcertos.setText("0");
            else usrPercAcertos.setText("100"); 
        }
        
        challenges = db.getUserChallenges(userId); // TESTAR AINDA
        
        ListViewDesafios.setCellFactory(new ChallengeCellFactory());
        if(challenges.size() == 0){
            ListViewDesafios.setDisable(true);
            ListViewDesafios.setOpacity(0);
            criouDes.setOpacity(1);
            criouDes.setDisable(false);  
        } else {
            ListViewDesafios.setDisable(false);
            ListViewDesafios.setOpacity(1);
            ListViewDesafios.getItems().addAll(challenges);
            criouDes.setOpacity(0);
            criouDes.setDisable(true);      
        }
        
        
        
    }
}
