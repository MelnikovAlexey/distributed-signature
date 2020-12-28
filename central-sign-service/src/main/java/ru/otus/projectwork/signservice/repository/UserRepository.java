package ru.otus.projectwork.signservice.repository;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.otus.projectwork.signservice.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("select u from User u where u.login = :login and u.enabled = :isActive")
    User findByLogin(@Param("login") String login, @Param("isActive") boolean isActive);

    @Query("select u from User u where u.login = :login")
    User findByLogin(@Param("login") String login);
}
