package CentralPoint;

import org.apache.commons.lang3.builder.HashCodeBuilder;

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
    
    @Override
    public boolean equals(Object p) {        
        return (user1.equals(((PairUser) p).user1) && user2.equals(((PairUser) p).user2));
    }
    @Override
    public int hashCode(){
        return new HashCodeBuilder(41, 59).append(user1).append(user2).toHashCode();
    }    
    
}
