package ru.otus.projectwork.signservice.entity;


import org.hibernate.annotations.Type;

import javax.persistence.*;


@Entity
@Table(name = "users", indexes = {
        @Index(name ="users_id_uindex", columnList = "id", unique = true),
        @Index(name ="users_login_uindex", columnList = "login", unique = true)
})
public class User {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    @SequenceGenerator(name = "users_seq", sequenceName = "users_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "login")
    private String login;

    @Column(name = "upassword")
    private String password;

    @Column(name = "enabled")
    @Type(type = "org.hibernate.type.NumericBooleanType")
    private boolean enabled;

    @OneToOne
    @JoinColumn(name = "id_user_role")
    private UserRole userRole;

    public User() {
    }

    public User(Long id, String login, String password, boolean enabled, UserRole userRole) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.enabled = enabled;
        this.userRole = userRole;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }


    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", userRole=" + userRole +
                '}';
    }
}
