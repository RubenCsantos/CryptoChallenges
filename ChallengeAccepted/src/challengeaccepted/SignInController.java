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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignInController implements Initializable {

    
    @FXML private TextField emailLabel;
    @FXML private TextField passwordLabel;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    
    private void error(String header, String content) {
        
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("ERRO");
        a.setHeaderText(header);
        a.setContentText(content);
        
        a.showAndWait();
    }

    /*
         Mudar do LoginScreen para o Feed
    */
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
    public void login(ActionEvent event) throws IOException{
        
        Database db = new Database();
        int userId = db.login(emailLabel.getText(), passwordLabel.getText());
        
        if (userId != -1)
            goToFeed(event, userId);
        else
            error("Log-in!", "Nome de utilizador/E-mail ou palavra-passe errada.");
    }   
    
}
