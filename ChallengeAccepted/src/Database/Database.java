/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Database;

import challengeaccepted.Challenge;
import challengeaccepted.ChallengeResolver;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Random;


public class Database {
    
    private Connection con;
    
    public Database() {
        
        String url = "jdbc:sqlite:src/res/database.db";
        
        try{
            
            con = DriverManager.getConnection(url);
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
    
    public int addUser2Db(String username, String email, String salt, String password) {
        
        try {
            
            Statement stmt = con.createStatement();
            
            String user = 
                    "INSERT INTO Users VALUES( null, '" + username + "', '" + email + "', '" + salt + "', '" + password + "');";
            
            stmt.execute(user);
            
            stmt.close();
            
            return 0;
        } catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return 1;
        }
    }
    
    public int addPasswordExercise(String title, String description, String message, String password, int cipher, int userID) {
        
        int type = 1;
        
        try {
            
            Statement stmt = con.createStatement();
            
            String exercise = 
                    "INSERT INTO Exercise VALUES( null, " + type + ", '" + title + "', '" + description + "', '" + message + "', '" + password + "', '" + cipher + "', null, " + userID + ");";
            
            stmt.execute(exercise);
            
            stmt.close();
            
            return 0;
        } catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return 1;
        }
    }
    
    public int addHashExercise(String title, String description, String message, int hash, int userID) {
        
        int type = 2;
        
        try {
            
            Statement stmt = con.createStatement();
            
            String exercise = 
                    "INSERT INTO Exercise VALUES( null, " + type + ", '" + title + "', '" + description + "', '" + message + "', null, null, '" + hash + "', " + userID + ");";
            
            stmt.execute(exercise);
            
            stmt.close();
            
            return 0;
        } catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return 1;
        }
    }
    
    public int addAttempt(int challengeID, int userID, boolean solved) {
        try {
            
            Statement stmt = con.createStatement();
            
            String attempt =
                    "SELECT AttemptsID, NumberOfTries FROM Attempts WHERE UserID = " + userID + " AND ExerciseID = " + challengeID + ";";
            
            ResultSet rs = stmt.executeQuery(attempt);
    
            if(rs.next()) { // There was data for this attempt
                int id = rs.getInt(1);
                int num = rs.getInt(2);
                
                stmt.close();

                stmt = con.createStatement();
                
                attempt =
                        "UPDATE Attempts "
                        + "SET NumberOfTries = " + (num + 1) + ", "
                        + "Value = " + solved + " "
                        + "WHERE AttemptsID = " + id + ";";
                stmt.execute(attempt);
            } else {
                stmt.close();
                
                stmt = con.createStatement();
                
                attempt = 
                    "INSERT INTO Attempts VALUES( null, " + 1 + ", " + solved + ", " + challengeID + ", " + userID + ");";
                stmt.execute(attempt);
            }

            stmt.close();
            
            return 0;
        } catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return 1;
        }
    }
    
    public String getUsername(int UserID) {
        
        try {
            
            Statement stmt = con.createStatement();
            
            String user = 
                    "SELECT Username FROM Users WHERE UserID = '" + UserID + "';";
            
            ResultSet rs = stmt.executeQuery(user);
            
            if(rs.next()) {
                String i = rs.getString(1);
                stmt.close();
                return i;
            } 
            else {
                stmt.close();
                return "";
            }
                
        } catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return "";
        }
        
    }
    
    public int getUserID(String username) {
        
        try {
            
            Statement stmt = con.createStatement();
            
            String user = 
                    "SELECT UserID FROM Users WHERE Username = '" + username + "';";
            
            ResultSet rs = stmt.executeQuery(user);
            
            if(rs.next()) {
                int i = rs.getInt(1);
                stmt.close();
                return  i;
            }
            else {
                stmt.close();
                return -1;
            }
                
        } catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return -2;
        }
    }
    
    public String getUserEmail(int UserID){
     
        try {
            
            Statement stmt = con.createStatement();
            
            String user = 
                    "SELECT Email FROM Users WHERE UserID = '" + UserID + "';";
            
            ResultSet rs = stmt.executeQuery(user);
            
            if(rs.next()) {
                String i = rs.getString(1);
                stmt.close();
                return i;
            } 
            else {
                stmt.close();
                return "";
            }
                
        } catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return "";
        }
        
    }
    
    public int getTentativas(int UserId){
        
        int soma = 0;
        
        try{
            
            Statement stmt = con.createStatement();
            
            String query =
                    "SELECT NumberOfTries FROM Attempts WHERE UserID = '" + UserId + "';";
                    
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next())
                soma += rs.getInt(1);
            
            stmt.close();
            
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        return soma;
    }
    
    public int getTentativas(int UserId, int ExerciseID){
        
        int soma = 0;
        
        try{
            
            Statement stmt = con.createStatement();
            
            String query =
                    "SELECT NumberOfTries FROM Attempts WHERE UserID = '" + UserId + "' AND ExerciseID = '" + ExerciseID + "';";
                    
            ResultSet rs = stmt.executeQuery(query);
            
            if(rs.next())
                soma += rs.getInt(1);
            
            stmt.close();
            
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        return soma;
    }
    
    public int getAcertos(int UserId){
        
        int soma = 0;
        
        try {
            
            Statement stmt = con.createStatement();
            
            String query =
                    "SELECT Value FROM Attempts WHERE UserID = '" + UserId + "';";
            
            ResultSet rs = stmt.executeQuery(query);
            
            while(rs.next()) if(rs.getInt(1) == 1) soma++;
  
            stmt.close();
            
        } catch (SQLException ex){
            System.out.println(ex.getMessage());
        }
        
        
        return soma;
    }
    
    public ArrayList<Challenge> getUserChallenges(int userId){
        
        ArrayList<Challenge> challenges = new ArrayList<>();
        
        try{
            
            Statement stmt = con.createStatement();
            
            String query = 
                    "SELECT ExerciseID,Type,Title FROM Exercise WHERE UserID = " + userId + ";";
            
            ResultSet rs = stmt.executeQuery(query);
          
            while(rs.next())
                challenges.add(new Challenge(rs.getInt(1), rs.getInt(2), rs.getString(3)));
            
            stmt.close();
            
            return challenges;
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        
        return null;
    }
    
    private String getSalt(String email) {
        
        try {
            
            Statement stmt = con.createStatement();
            
            String user = 
                    "SELECT Salt FROM Users WHERE Email = '" + email + "' OR Username = '" + email + "';";
            
            ResultSet rs = stmt.executeQuery(user);
            
            if(rs.next()) {
                String s = rs.getString(1);
                stmt.close();
                return s;
            }
            else {
                stmt.close();
                return "";
            }
        } catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return "";
        }
        
    }
    
    private String bytesToHex(byte[] bytes) {
        
        char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        
        return new String(hexChars);
    }
    
    public String generateSalt() {
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            Random rand = new Random();
            
            return bytesToHex(md.digest((String.valueOf(rand.nextInt(256))).getBytes()));
        } catch (NoSuchAlgorithmException ex) {
            return "";
        }
    }
    
    public String encryptPassword(String password, String salt) {
        
        try {
            
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            
            return bytesToHex(md.digest((salt + password).getBytes()));
            
        } catch (NoSuchAlgorithmException ex) {
            return password;
        }
    }
    
    public int checkIfEmailDoesntExist(String email) {
        
        try{
            
            Statement stmt = con.createStatement();
            
            String query = "SELECT Email FROM Users WHERE Email = '" + email + "';";
            
            ResultSet rs = stmt.executeQuery(query);
            
            
            
            if(!rs.next()) {
                stmt.close();
                return 0;
            }
            else {
                stmt.close();
                return 1;
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return -1;
        }     
    }
    
    public int checkIfUsernameIsTaken(String username) {
        
        try{
            
            Statement stmt = con.createStatement();
            
            String query = "SELECT Username FROM Users WHERE Username = '" + username + "';";
            
            ResultSet rs = stmt.executeQuery(query);
            
            
            
            if(!rs.next()) {
                stmt.close();
                return 0;
            }
            else {
                stmt.close();
                return 1;
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return -1;
        }  
    }
    
    public int checkIfPasswordIsStrong(String password) {
        
        
        if(password.length() < 6)
            return 1;
        
        if(password.matches("(?=.*[0-9]).*") && password.matches("(?=.*[a-z]).*") && password.matches("(?=.*[A-Z]).*")) 
            return 0;
        else
            return 2;
    }
    
    public boolean checkIfExerciseIsDone(int exerciseId, int userId) {
        
        try{
            
            Statement stmt = con.createStatement();
            
            String query = "SELECT Value FROM Attempts WHERE UserID = " + userId + " AND ExerciseID = " + exerciseId + ";";
            
            ResultSet rs = stmt.executeQuery(query);
            
            if(!rs.next()) {
                if(rs.getInt(1) == 1) {
                    stmt.close();
                    return true;
                }
            }
            else {
                stmt.close();
                return false;
            }
            
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return false;
        }       
        return false;
    }
    
    public ChallengeResolver getExercise(int exerciseId) {
        try{
            Statement stmt = con.createStatement();
            String query = 
                "SELECT * FROM Exercise WHERE ExerciseId =" + exerciseId + ";";
            ResultSet rs = stmt.executeQuery(query);

            ChallengeResolver c = new ChallengeResolver(rs.getInt(1), rs.getInt(2), rs.getInt(7), rs.getInt(8), rs.getInt(9), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));

            stmt.close();

            return c;

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
    
    public ArrayList<Challenge> getExercises(int userId) {

            ArrayList<Challenge> exercises = new ArrayList<>();

            try{

                Statement stmt = con.createStatement();
                String query = 
                    "SELECT ExerciseID, Type, Title FROM Exercise WHERE (ExerciseID NOT IN (SELECT ExerciseID FROM Attempts WHERE Value = 1 AND UserID =" + userId + ")) AND (Exercise.UserID <> " + userId +");";
                ResultSet rs = stmt.executeQuery(query);

                while(rs.next())
                    exercises.add(new Challenge(rs.getInt(1), rs.getInt(2), rs.getString(3)));

                stmt.close();
                
                return exercises;

            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                return null;
            }
            
        }
    
    public int login(String email, String password) {
        
        try {
            
            Statement stmt = con.createStatement();
            
            String salt = getSalt(email);
            
            String user = 
                    "SELECT UserID FROM Users WHERE (Email = '" + email + "' OR Username = '" + email + "') AND Password = '" + encryptPassword(password,salt) + "';";
            
            ResultSet rs = stmt.executeQuery(user);
            
            if(rs.next()){
                int i = rs.getInt(1);
                stmt.close();
                return i;
            }
            else {
                stmt.close();
                return -1;
            }
                
        } catch (SQLException ex) {
            
            System.out.println(ex.getMessage());
            return -1;
        }
        
    }
}
