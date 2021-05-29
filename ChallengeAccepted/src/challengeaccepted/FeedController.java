package challengeaccepted;

import Database.Database;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;


public class FeedController implements Initializable {
    
    @FXML private Label usrNameLabel;
    @FXML private Circle circle;
    @FXML private FontAwesomeIconView boneco;
    @FXML private GridPane mainGrid;
    private HashMap<Integer,Long> challengeTimer;
    
    private int userId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try{ 
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
            Parent perfil = fxmlLoader.load();
            mainGrid.getChildren().remove(1);
            mainGrid.add(perfil,1,0);

        } catch (Exception e) {
            System.out.println("gridPane cell");
            System.out.println(e.getMessage());
        }
    }

    public void getUser(int userid){
        
        this.userId = userid;
        Database db = new Database();
        usrNameLabel.setText(db.getUsername(this.userId));
        
    }
    
    /*
        EXIT FUNCTION
    */
    @FXML
    private void exitButton(ActionEvent event) throws InterruptedException {
        
        Platform.exit();
        System.exit(0);
    }     
    
    @FXML
    private void goToProfile(MouseEvent event) throws InterruptedException{     
        showProfile(userId,event);  
    }  
    
    private void showProfile(int userID,MouseEvent event){
       
        try{ 
            
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Perfil.fxml"));
            Parent perfil = fxmlLoader.load();
            PerfilController perfilcontroller = fxmlLoader.getController();
            perfilcontroller.getUserAttributes(userID);
            mainGrid.getChildren().remove(1);
            mainGrid.add(perfil,1,0);
                                
            
            } catch (Exception e) {
                System.out.println("gridPane cell");
                System.out.println(e.getMessage());
            }
            
    }
    
        @FXML
    private void goToConsult(ActionEvent event) throws InterruptedException{
        showEvents(userId);
    }
    
    private void showEvents (int userID) {
       
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ConsultarEvento.fxml"));
            Parent consultar = fxmlLoader.load();
            ConsultarEventoController consultareventocontroller = fxmlLoader.getController();
            consultareventocontroller.getUserID(userID,mainGrid,challengeTimer);
            mainGrid.getChildren().remove(1);
            mainGrid.add(consultar,1,0);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        
    }
    
    private Node getNode(GridPane gridpane,int col, int row){
        for(Node node : gridpane.getChildren()){
            
            Integer c = GridPane.getColumnIndex(node);
            Integer r = GridPane.getRowIndex(node);
            if(c == null) c = 0;
            if(r == null) r = 0;
            if(c == 1 && r == 0){
                return node;
            }
            
        }
        
        return null;
    }
    
    @FXML
    private void criarDesafio(ActionEvent event) throws InterruptedException, IOException {
        try{ 

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CriarDesafio.fxml"));
            Parent perfil = fxmlLoader.load();
            CriarDesafioController criardesafiocontroller = fxmlLoader.getController();
            criardesafiocontroller.getUser(userId, mainGrid);
            mainGrid.getChildren().remove(1);
            mainGrid.add(perfil,1,0);

        } catch (Exception e) {
            System.out.println("gridPane cell");
            System.out.println(e.getMessage());
        }
        
        //Database db = new Database();
        
        //db.addPasswordExercise("Test2", "T2 t2 t2 t2", "test", "12345", "AES-128", 1);
        //db.addHashExercise("Test", "Test test test test", "testtt", "SHA-1", 1);
        
    }  
  
}

