package challengeaccepted;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;

public class CelulaChallenge extends ListCell<Challenge>{
    
    @FXML
    private Label title;
    @FXML
    private Label type;
    @FXML
    private Label id;
    
    public CelulaChallenge(){
        loadFXML();
    }
    private void loadFXML(){
        
        try{ 
            FXMLLoader cellList = new FXMLLoader(getClass().getResource("Celula.fxml"));
            cellList.setController(this);
            cellList.setRoot(this);
            cellList.load();
            
        } catch (IOException e){
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void updateItem(Challenge item, boolean empty){
        super.updateItem(item, empty);
        
        if(empty || item == null){
            setText(null);
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        } else {
            title.setText(item.getTitle());
            if(item.getType() != 1){
                type.setText("HASH");
            } else {
                type.setText("CIPHER");
            }
            id.setText(String.valueOf(item.getId()));

            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        }
        
    }
}
