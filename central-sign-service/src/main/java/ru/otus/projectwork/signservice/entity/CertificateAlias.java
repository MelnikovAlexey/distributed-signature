package ru.otus.projectwork.signservice.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "certificate_alias")
@Getter
@Setter
public class CertificateAlias {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificate_seq")
    @SequenceGenerator(name = "certificate_seq", sequenceName = "certificate_alias_id_seq", allocationSize = 1)
    private long id;

    @Column(name = "calias")
    private String alias;

    @Column(name = "apassword")
    private String password;

    @Column(name = "id_user")
    private long userId;

    public CertificateAlias() {
    }

    public CertificateAlias(int id, String alias, String password, int userId) {
        this.id = id;
        this.alias = alias;
        this.password = password;
        this.userId = userId;
    }
}
