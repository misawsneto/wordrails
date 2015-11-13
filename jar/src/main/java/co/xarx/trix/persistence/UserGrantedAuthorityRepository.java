package co.xarx.trix.persistence;

import co.xarx.trix.domain.UserGrantedAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;

public interface UserGrantedAuthorityRepository extends JpaRepository<UserGrantedAuthority, Integer>, QueryDslPredicateExecutor<UserGrantedAuthority> {
}
