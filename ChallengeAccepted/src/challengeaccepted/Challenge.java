package challengeaccepted;

public class Challenge {
    private int id;
    private int type;
    private String title;
    
    public Challenge(int id,int type,String title){
        
        this.id = id;
        this.type = type;
        this.title = title;
        
    }

    public void setId(int id) {this.id = id;}
    public void setType(int type) {this.type = type;}
    public void setTitle(String title) { this.title = title;}

    public int getId() {return id;}
    public int getType() {return type;}
    public String getTitle() {return title;}  
}
