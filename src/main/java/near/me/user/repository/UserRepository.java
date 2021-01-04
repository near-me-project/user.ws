package near.me.user.repository;

import near.me.user.repository.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    default Optional<UserEntity> findByEmailAsOptional(String email){
        return Optional.ofNullable(findByEmail(email));
    }

    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);

    void deleteByUserId(String userId);
}
