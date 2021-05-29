package challengeaccepted;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable {

    @FXML
    private VBox vbox;
    @FXML
    private Parent parent;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(0.60), vbox);
        t.setToX(vbox.getLayoutX() * 22);
        t.play();
        t.setOnFinished((e) -> {
            try {
                parent = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(parent);
            } catch (IOException ex) {

            }
        });
    }

    /*
        LOGIN SCREEN ANIMATED VIEW (SIGN IN)
    */
    @FXML
    private void abrir_inicioSessao(ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(0.60), vbox);
        t.setToX(vbox.getLayoutX() * 22);
        t.play();
        t.setOnFinished((e) -> {
            try {
                parent = FXMLLoader.load(getClass().getResource("SignIn.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(parent);
            } catch (IOException ex) {

            }
        });
    }

    /*
        LOGIN SCREEN ANIMATED VIEW (SIGN UP)
    */
    @FXML
    private void abrir_registo(ActionEvent event) {
        TranslateTransition t = new TranslateTransition(Duration.seconds(0.60), vbox);
        t.setToX(-1);
        t.play();
        t.setOnFinished((e) -> {
            try {
                parent = FXMLLoader.load(getClass().getResource("SignUp.fxml"));
                vbox.getChildren().removeAll();
                vbox.getChildren().setAll(parent);
            } catch (IOException ex) {

            }
        });
    }
    
    /*
        EXIT FUNCTION
    */
    @FXML
    private void exitButton (MouseEvent event){
        Platform.exit();
        System.exit(0);
    }
    
    
}
