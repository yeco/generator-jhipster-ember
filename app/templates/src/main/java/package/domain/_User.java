package <%=packageName%>.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;<% if (hibernateCache != 'no') { %>
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;<% } %>
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Set;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * A user.
 */
@Data
@Entity
@Table(name = "T_USER")<% if (hibernateCache != 'no') { %>
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)<% } %>
public class User implements Serializable {

    @NotNull
    @Size(min = 0, max = 50)
    @Id
    private String login;

    @JsonIgnore
    @Size(min = 0, max = 100)
    private String password;

    @Size(min = 0, max = 50)
    @Column(name = "first_name")
    private String firstName;

    @Size(min = 0, max = 50)
    @Column(name = "last_name")
    private String lastName;

    @Email
    @Size(min = 0, max = 100)
    private String email;

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "T_USER_AUTHORITY",
            joinColumns = {@JoinColumn(name = "login", referencedColumnName = "login")},
            inverseJoinColumns = {@JoinColumn(name = "name", referencedColumnName = "name")})<% if (hibernateCache != 'no') { %>
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)<% } %>
    private Set<Authority> authorities;
}
