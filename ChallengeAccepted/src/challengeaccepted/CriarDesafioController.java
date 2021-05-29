package challengeaccepted;

import Database.Database;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class CriarDesafioController implements Initializable {
    
    @FXML private ToggleGroup togGrupoTipo;
    
    @FXML private ToggleButton togCifra; 
    @FXML private ToggleButton togHash;
    
    @FXML private Label lblTipo;
    @FXML private Label lblPassword;
    
    @FXML private TextField tfNome;
    @FXML private TextField tfPassword;
    
    @FXML private TextArea taDescricao;
    @FXML private TextArea taMensagem;
    
    @FXML private Button btnCriar;
    
    @FXML private ComboBox cbType;
    
    
    @FXML private Pane pnl;
    
    private GridPane grid;
    
    private int id;
    
    public void getUser(int userid, GridPane grid){
        this.id = userid;
        this.grid = grid;
    }
    
    private void error(String header, String content) {
        
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle("ERRO");
        a.setHeaderText(header);
        a.setContentText(content);
        
        a.showAndWait();
    }
    
    @FXML 
    private void handleClickButton(ActionEvent event) {
        if (togCifra.isSelected()) {
            lblTipo.setText("Tipo de Cifra:");
            tfPassword.setVisible(true);
            lblPassword.setVisible(true);
            cbType.getItems().removeAll(cbType.getItems());
            cbType.getItems().addAll("AES-128-ECB", "AES-128-CBC", "AES-128-CTR");
        }
        else if (togHash.isSelected()) {
            tfPassword.setText("");
            tfPassword.setVisible(false);
            lblPassword.setVisible(false);
            lblTipo.setText("Tipo de Hash:");
            cbType.getItems().removeAll(cbType.getItems());
            cbType.getItems().addAll("MD5", "SHA256", "SHA512");
        }
    }
    
    @FXML
    private void registarDesafio() {
        StringBuilder sb = new StringBuilder();
        int flag = 0;
        
        if (togCifra.isSelected()) {
            flag = 1;
            if (cbType.getValue() == null) sb.append("Tem de selecionar um tipo de cifra.\n");
            if (tfPassword.getText().equals("")) sb.append("É preciso uma password para registar o desafio.\n");
        } else if (togHash.isSelected()) {
            flag = 2;
            if (cbType.getValue() == null) sb.append("Tem de selecionar um tipo de hash.\n");
        } else sb.append("Tem de selecionar um tipo de desafio.\n");
        
        if (tfNome.getText().equals("")) sb.append("É preciso um nome para registar o desafio.\n");
        if (taDescricao.getText().equals("")) sb.append("É preciso uma descrição para registar o desafio.\n");
        if (taMensagem.getText().equals("")) sb.append("É preciso uma mensagem para registar o desafio.\n");
        
        
        if (sb.length() != 0) {
            error("Campos em falta!", sb.toString());
            return;
        }
        
        Database db = new Database();
     
        switch (flag) {
            case 1:
                db.addPasswordExercise(tfNome.getText(), taDescricao.getText(), taMensagem.getText(), tfPassword.getText(), (cbType.getSelectionModel().getSelectedIndex() + 1), id);
                break;
            case 2:
                db.addHashExercise(tfNome.getText(), taDescricao.getText(), taMensagem.getText(), (cbType.getSelectionModel().getSelectedIndex() + 1), id);
                break;
        }
        
        try{ 
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
            Parent perfil = fxmlLoader.load();
            grid.getChildren().remove(1);
            grid.add(perfil,1,0);

        } catch (Exception e) {
            System.out.println("gridPane cell");
            System.out.println(e.getMessage());
        }
    }
    
    @FXML
    private void cancelar() {
        try{ 
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MainPage.fxml"));
            Parent perfil = fxmlLoader.load();
            grid.getChildren().remove(1);
            grid.add(perfil,1,0);

        } catch (Exception e) {
            System.out.println("gridPane cell");
            System.out.println(e.getMessage());
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        cbType.getItems().addAll("AES-128-ECB", "AES-128-CBC", "AES-128-CTR");
    }    
    
}
