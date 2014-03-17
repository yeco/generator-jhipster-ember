package <%=packageName%>.web.rest;

import <%=packageName%>.web.rest.dto.UserDTO;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.account.AccountCriteria;
import com.stormpath.sdk.account.AccountList;
import com.stormpath.sdk.account.Accounts;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserResource {
    @Autowired
    private Client stormpathClient;
    @Value("<%= _.unescape('\$\{stormpath.application.url}')%>")
    private String stormpathApplicationUrl;

    @RequestMapping("{username}")
    public ResponseEntity<UserDTO> findOne(@PathVariable("username") String username) {
        AccountCriteria criteria = Accounts.where(Accounts.username().eqIgnoreCase(username));
        AccountList accounts = stormpathClient.getDataStore().getResource(stormpathApplicationUrl, Application.class).getAccounts(criteria);
        Account account = null;
        for (Account acc : accounts) {
            account = acc;
        }

        if(account == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(new UserDTO(account.getUsername(), account.getFullName(), account.getEmail()), HttpStatus.OK);
        }
    }
}
