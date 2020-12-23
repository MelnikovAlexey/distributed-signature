package ru.otus.projectwork.signservice.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "token_user", indexes = {
        @Index(name = "token_user_id_uindex" , columnList = "id",unique = true),
        @Index(name = "token_user_tokenserial_uindex" , columnList = "token_id",unique = true),
        @Index(name = "token_user_userid_uindex" , columnList = "user_id",unique = true)
})
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserToken {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_user_seq")
    @SequenceGenerator(name = "token_user_seq", sequenceName = "token_user_id_seq", allocationSize = 1)
    private long id;
    @Column(name = "token_id")
    @JsonProperty("serial")
    private String tokenId;
    @Column(name = "user_id")
    @JsonProperty("userId")
    private long userId;

    public UserToken() {
    }

    public UserToken(long id, String tokenId, long userId) {
        this.id = id;
        this.tokenId = tokenId;
        this.userId = userId;
    }

    public long getId() {
        return id;
    }

    @JsonGetter(value = "serial")
    public String getTokenId() {
        return tokenId;
    }


    @JsonGetter(value = "userId")
    public long getUserId() {
        return userId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}

