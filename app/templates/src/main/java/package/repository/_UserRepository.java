package <%=packageName%>.repository;

import <%=packageName%>.domain.User;
import com.stormpath.sdk.account.Account;
import com.stormpath.sdk.account.AccountCriteria;
import com.stormpath.sdk.account.AccountList;
import com.stormpath.sdk.account.Accounts;
import com.stormpath.sdk.application.Application;
import com.stormpath.sdk.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 *
 */
@Repository
public class UserRepository implements PagingAndSortingRepository<User, String> {
    @Autowired
    private Client stormpathClient;
    @Value("<%= _.unescape('\$\{stormpath.application.url}')%>")
    private String stormpathApplicationUrl;

    @Override
    public Iterable<User> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends User> S save(S entity) {
        return null;
    }

    @Override
    public <S extends User> Iterable<S> save(Iterable<S> entities) {
        return null;
    }

    @Override
    public User findOne(String id) {
        Assert.notNull(id, "The given id must not be null!");

        AccountCriteria criteria = Accounts.where(Accounts.username().eqIgnoreCase(id));
        AccountList accounts = application().getAccounts(criteria);
        Account account = null;
        for (Account acc : accounts) {
            account = acc;
        }

        return new User(account);
    }

    @Override
    public boolean exists(String id) {
        return findOne(id) != null;
    }

    @Override
    public Iterable<User> findAll() {
        return null;
    }

    @Override
    public Iterable<User> findAll(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(String s) {

    }

    @Override
    public void delete(User entity) {

    }

    @Override
    public void delete(Iterable<? extends User> entities) {

    }

    @Override
    public void deleteAll() {

    }

    private Application application() {
        return stormpathClient.getDataStore().getResource(stormpathApplicationUrl, Application.class);
    }
}
