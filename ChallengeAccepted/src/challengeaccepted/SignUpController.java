/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package challengeaccepted;

import Database.Database;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUpController implements Initializable {

    @FXML private TextField userName;
    @FXML private TextField email;
    @FXML private TextField password;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    private void error(String header, String content) {
        
        Alert a = new Alert(AlertType.ERROR);
        a.setTitle("ERRO");
        a.setHeaderText(header);
        a.setContentText(content);
        
        a.showAndWait();
    }
    
    private void goToFeed(ActionEvent event, int userid) throws IOException {
        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Feed.fxml"));
        
        Parent feed = loader.load();
        
        FeedController controller = loader.getController();
        controller.getUser(userid);
        
        Scene feedScene = new Scene(feed);
        
        Stage feedWindow = (Stage)((Node)event.getSource()).getScene().getWindow();
        
        feedWindow.setScene(feedScene);
        feedWindow.show();
    }
    
    @FXML
    public void goToLogin(ActionEvent event) throws IOException {
        
        StringBuilder sb = new StringBuilder();
        
        String userNameStr = userName.getText();
        String emailStr = email.getText();
        String passwordStr = password.getText();
        
        if(userNameStr.equals(""))
            sb.append("É preciso um nome de utilizador para se registar.\n");
        if(emailStr.equals(""))
            sb.append("É preciso um e-mail para se registar.\n");
        if(passwordStr.equals(""))
            sb.append("É preciso uma password para se registar.\n");
        
        if(sb.length() != 0){
            error("Campos em falta!", sb.toString());
            return;
        }
            

        Database db = new Database();
        
        switch(db.checkIfEmailDoesntExist(emailStr)) {
            
            case 1:
                error("E-mail!", "E-mail já foi usado.");
                return;
            case -1:
                error("Base de dados", "Conexão à base de dados falhou.");   
                return;
        }
        
        switch(db.checkIfUsernameIsTaken(userNameStr)) {
            
            case 1:
                error("Username!", "Username já foi usado.");
                return;
            case -1:
                error("Base de dados", "Conexão à base de dados falhou.");   
                return;
        }
        
        switch(db.checkIfPasswordIsStrong(passwordStr)) {
            
            case 1:
                error("Password!", "A password tem que ser no mínimo de 6 caracteres.");
                return;
            
            case 2:
                error("Password!", "A password tem de pelo menos conter:\n"
                        + "\t* uma letra mínuscula;\n"
                        + "\t* uma letra maíscula;\n"
                        + "\t* um número.");
                return;
        }
             
        String salt = db.generateSalt();
        passwordStr = db.encryptPassword(passwordStr, salt);
        
        if(db.addUser2Db(userNameStr, emailStr, salt, passwordStr) == 0)
            goToFeed(event,db.getUserID(userNameStr));
        else
            error("Database!","Erro ao adicionar utilizador.");
        
    } 
    
}
