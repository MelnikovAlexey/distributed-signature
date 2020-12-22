package ru.otus.projectwork.signservice.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.otus.projectwork.signservice.entity.CertificateAlias;

public interface UserSignRepository extends CrudRepository<CertificateAlias, Long> {

    @Query("select ca from CertificateAlias ca where ca.userId = :userId")
    CertificateAlias findByUserId(@Param(value = "userId") long id);

}
