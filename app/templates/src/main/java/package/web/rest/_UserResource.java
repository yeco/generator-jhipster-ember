package <%=packageName%>.web.rest;

import <%=packageName%>.domain.User;
import <%=packageName%>.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
/**
 *
 */
@RestController
@RequestMapping("/api/v1/users")
public class UserResource extends AbstractRestResource<User, String, User.UserWrapper> {
    @Autowired
    private UserRepository userRepository;

    @Override
    protected Class<User> entityClass() {
        return User.class;
    }

    @Override
    protected PagingAndSortingRepository<User, String> repository() {
        return userRepository;
    }

    @Override
    protected User.UserWrapper entityWrapper(User entity) {
        return new User.UserWrapper(entity);
    }
}
