package ru.otus.projectwork.signservice.repository;

import feign.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.otus.projectwork.signservice.entity.UserToken;

@Repository
public interface UserTokenRepository extends CrudRepository<UserToken, Long> {
    @Query("select ut from UserToken ut where ut.userId = :userId")
    UserToken findUserByUserId(@Param("userId") long id);

}
