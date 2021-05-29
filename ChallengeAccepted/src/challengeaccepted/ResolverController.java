/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package challengeaccepted;

import Database.Database;
import java.io.IOException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import javafx.scene.layout.GridPane;

public class ResolverController implements Initializable {

    private ChallengeResolver cr;
    private Database db;
    private int userID;
    private final String HEXES = "0123456789abcdef";
    private String[] cifras = {"AES-128-ECB", "AES-128-CBC", "AES-128-CTR"};
    private String[] hashs = {"MD5", "SHA-256", "SHA-512/256"};
    private HashMap<Integer,Long> challengeTimer;
    
    @FXML private TextField tipo; @FXML private TextField utilizador;  
    @FXML private TextField titulo; @FXML private TextField algoritmo;
    @FXML private TextArea descricao; @FXML private TextArea solucao; @FXML private TextArea dados;
    @FXML private GridPane mainGrid;
    @FXML private Label tipoDados; @FXML private Label tentativas;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    
    public String getHex(byte[] raw) {
        if( raw == null) {
            return null;
        }
        final StringBuilder hex = new StringBuilder(2*raw.length);
        for(final byte b : raw ) {
            hex.append(HEXES.charAt((b & 0xF0) >> 4)).
            append(HEXES.charAt((b & 0x0F)));
        }
        return hex.toString();
    }
    
    // Vamos definir qual é o desafio para ser mostrado, retirando da base de dados a informação sobre este
    public void setChallenge(int exerciseId, int userID, HashMap challengeTimer) {
        db =  new Database();
        this.userID = userID;
        this.challengeTimer = challengeTimer;
        cr = db.getExercise(exerciseId);
        initializeFields();
    }
    
    public void initializeFields() { // Vamos inicializer os campos da view com os dados do exercício. Algumas labels vão ser diferentes dependendo do tipo de exercício.
        tipo.setText(cr.getType() == 1 ? "Cifra" : "Hash");
        tipoDados.setText(cr.getType() == 1 ? "Criptograma" : "Hash");
        utilizador.setText(db.getUsername(cr.getUserID()));
        titulo.setText(cr.getTitle());
        descricao.setText(cr.getDescription());
        algoritmo.setText(cr.getType() == 1 ? cifras[cr.getCipher() - 1] : hashs[cr.getHash() - 1]);
        tentativas.setText(tentativas.getText() + db.getTentativas(userID, cr.getId()));
        if(cr.getType() == 1) initializeCifra(); else initializeHash();
    }
    
    private void initializeCifra() { // Inicializamos o exercício de cifra, mostrando no campo de dados o criptograma gerado com o algoritmo definido.
        String cifra = cifras[cr.getCipher() - 1];
        try {
            byte[] salt = new byte[16];
            Random random = new Random();
            random.nextBytes(salt);
            KeySpec spec = new PBEKeySpec(cr.getPassword().toCharArray(), salt, 65536, 128);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            Cipher cipher = Cipher.getInstance(cifra.substring(0,3) + "/" + cifra.substring(8, 11) + "/PKCS5Padding");
            SecretKeySpec sKey = new SecretKeySpec(f.generateSecret(spec).getEncoded(), cifra.substring(0,3));
            cipher.init(Cipher.ENCRYPT_MODE, sKey);
            dados.setText(getHex(cipher.doFinal(cr.getMessage().getBytes())));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException ex) {
            Logger.getLogger(ResolverController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void initializeHash() { // Inicializamos o exercício de hash, mostrando no campo de dados o hash gerado com o algoritmo definido.
        try {
            MessageDigest oMD = MessageDigest.getInstance(hashs[cr.getHash() - 1]);
            oMD.update(cr.getMessage().getBytes());
            dados.setText(getHex(oMD.digest()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void setMainGrid(GridPane mainGrid) {
        this.mainGrid = mainGrid;
    }
    
    public void voltarConsultar() { // Voltamos à view anterior
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
    
    public void tentarResolver() {
        if (cr.getType() == 1) tentarResolverCifra();
        else tentarResolverHash();
        voltarConsultar();
    }

    private void tentarResolverCifra() { // Se a solução estiver correta mostramos que foi resolvido e voltamos à view anterior, guardando na base de dados que o utilizador atual resolveu o exercício.
        if(cr.getPassword().equals(solucao.getText())) {
            db.addAttempt(cr.getId(), userID, true);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("RESOLVIDO");
            a.setHeaderText("Conseguiu resolver");
            a.setContentText("Conseguiu resolver o exercício de cifra com sucesso! A password era: " + cr.getPassword());
            a.showAndWait();
            voltarConsultar();
        } else { // Caso contrário, avisamos que não conseguiu resolver e aumentamos o campo do número de tentativas.
            db.addAttempt(cr.getId(), userID, false);
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("ERRADO");
            a.setHeaderText("Enganou-se na solução");
            a.setContentText("Infelizmente essa não é a solução correta. Tente mais tarde!");
            if(challengeTimer == null)
                challengeTimer = new HashMap();
            challengeTimer.put(cr.getId(), Calendar.getInstance().getTimeInMillis());
            a.showAndWait();
        }
    }

    private void tentarResolverHash() { // Se a solução estiver correta mostramos que foi resolvido e voltamos à view anterior, guardando na base de dados que o utilizador atual resolveu o exercício.
        if(cr.getMessage().equals(solucao.getText())) {
            db.addAttempt(cr.getId(), userID, true);
            Alert a = new Alert(Alert.AlertType.INFORMATION);
            a.setTitle("RESOLVIDO");
            a.setHeaderText("Conseguiu resolver");
            a.setContentText("Conseguiu resolver o exercício de hash com sucesso! A mensagem era: " + cr.getMessage());
            a.showAndWait();
        } else { // Caso contrário, avisamos que não conseguiu resolver e aumentamos o campo do número de tentativas.
            db.addAttempt(cr.getId(), userID, false);
            Alert a = new Alert(Alert.AlertType.ERROR);
            a.setTitle("ERRADO");
            a.setHeaderText("Enganou-se na solução");
            a.setContentText("Infelizmente essa não é a solução correta. Tente mais tarde!");
            a.showAndWait();
        }
    }
    
}
