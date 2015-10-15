package CentralPoint;

public class PairUser {
    private String user1;
    private String user2;
    
    public PairUser(String user1, String user2) {
        this.user1 = user1;
        this.user2 = user2;
    }
    
    public String getUser1() {
        return this.user1;
    }
    
    public String getUser2() {
        return this.user2;
    }

    public PairUser swap() {
        return new PairUser(user2, user1);
    }
    
}
