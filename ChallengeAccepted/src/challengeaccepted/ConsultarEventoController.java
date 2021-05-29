package challengeaccepted;

import Database.Database;
import challengeaccepted.Challenge;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ConsultarEventoController implements Initializable {

    @FXML
    private ListView<Challenge> ListViewDesafios;
    @FXML
    private Label ListDes;

    private GridPane mainGrid;

    private int userID;

    private ArrayList<Challenge> challenges2;
    
    private HashMap<Integer,Long> challengeTimer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ListViewDesafios.setCellFactory(new ChallengeCellFactory());
        
        ListViewDesafios.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent click) {

                if (click.getClickCount() == 2) {
                    //Use ListView's getSelected Item
                    Challenge currentItemSelected = ListViewDesafios.getSelectionModel().getSelectedItem();
                    //use this to do whatever you want to. Open Link etc.

                    goToResolve(currentItemSelected,click);
                }

            }
        });

    }

    @FXML
    private void goToResolve(Challenge x, MouseEvent event) {
        resolver(x,event);
    }

    private void resolver(Challenge x, MouseEvent event) {
        try {
            if(x.getType() == 1) {
                long now = Calendar.getInstance().getTimeInMillis();
                Long then;
                try {
                    then = challengeTimer.get(x.getId());
                } catch (NullPointerException e) {
                    challengeTimer = new HashMap();
                    then = null;
                }
                if(then != null) {
                    long delta = (now - then) / 1000;
                    if(delta < 15) {
                        Alert a = new Alert(Alert.AlertType.ERROR);
                        a.setTitle("TEMPO DE ESPERA");
                        a.setHeaderText("Ainda tem de esperar");
                        a.setContentText("Precisa de esperar 15 segundos depois de falhar numa solução. Ainda faltam aproximadamente " + (15 - delta) + " segundos.");
                        a.show();
                        return;
                    }
                } 
            }
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Resolver.fxml"));
            Parent resolver = fxmlLoader.load();
            ResolverController resolvercontroller = fxmlLoader.getController();
            resolvercontroller.setChallenge(x.getId(), userID, challengeTimer);
            resolvercontroller.setMainGrid(mainGrid);
            mainGrid.getChildren().remove(1);
            mainGrid.add(resolver, 1, 0);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    public void getUserID(int userID, GridPane mainGrid, HashMap challengeTimer) {
        this.userID = userID;
        this.mainGrid = mainGrid;
        this.challengeTimer = challengeTimer;
                Database db = new Database();
        ArrayList<Challenge> challenges = db.getExercises(userID);
        challenges2 = challenges;
        challengeTimer = new HashMap(challenges2.size());
        if (challenges.size() != 0) {

            ListViewDesafios.setDisable(false);
            ListViewDesafios.setOpacity(1);
            ListViewDesafios.getItems().addAll(challenges);
            ListDes.setOpacity(0);
            ListDes.setDisable(true);
        } else {
            ListViewDesafios.setDisable(true);
            ListViewDesafios.setOpacity(0);
            ListDes.setOpacity(1);
            ListDes.setDisable(false);

        }
    }

}
