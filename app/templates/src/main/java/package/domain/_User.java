package <%=packageName%>.domain;

import com.stormpath.sdk.account.Account;
import lombok.Data;

/**
 *
 */
@Data
public class User implements Resource<String> {
    private String id;
    private String username;
    private String name;
    private String email;

    public User(String username, String name, String email) {
        this.id = username;
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public User(Account account) {
        this.id = account.getUsername();
        this.username = account.getUsername();
        this.name = account.getFullName();
        this.email = account.getEmail();
    }
}
