package ru.otus.projectwork.signservice.entity;

import javax.persistence.*;

@Entity
@Table(name = "user_role", indexes = {
        @Index(name ="user_role_id_uindex", columnList = "id", unique = true),
        @Index(name ="user_role_role_uindex", columnList = "role", unique = true)
})
public class UserRole {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_role_seq")
    @SequenceGenerator(name = "users_role_seq", sequenceName = "user_role_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "role")
    private String role;

    public UserRole() {
    }

    public UserRole(String role) {
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
