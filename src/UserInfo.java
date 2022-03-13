import java.util.*;

public class UserInfo {
    /**
     * Hashmap that will store the username and password of every user
     */
    HashMap<String, String> LoginInfo;
    public UserInfo(){
        this.LoginInfo = new HashMap<>();
    }

    /**
     *
     * @return HashMap of all the login info
     */
    public HashMap<String, String> getLoginInfo() {
        return LoginInfo;
    }

    /**
     * Registers the user
     * @param username The username
     * @param password The password
     */
    public void register(String username, String password){
        this.LoginInfo.put(username, password);
    }

    /**
     * Logs into the chatroom
     * @param username The username
     * @param password The password
     * @return True if the username and password are logged in, false otherwise
     */
    public boolean login(String username, String password){
        return this.LoginInfo.containsKey(username) && this.LoginInfo.get(username).equals(password);
    }
}
