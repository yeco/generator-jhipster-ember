package <%=packageName%>.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import <%=packageName%>.domain.util.EntityWrapper;
import com.stormpath.sdk.account.Account;
import lombok.Data;

import javax.validation.Valid;

/**
 *
 */
@Data
public class User implements Resource<String> {
    private String id;
    private String name;
    private String email;

    public User(String username, String name, String email) {
        this.id = username;
        this.name = name;
        this.email = email;
    }

    public User(Account account) {
        this.id = account.getUsername();
        this.name = account.getFullName();
        this.email = account.getEmail();
    }

    @Data
    public static class UserWrapper implements EntityWrapper<User> {
        @Valid
        private User user;

        public UserWrapper(User user) {
            this.user = user;
        }

        @JsonIgnore
        @Override
        public User getEntity() {
            return user;
        }
    }
}
