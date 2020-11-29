package near.me.user.repository;

import near.me.user.repository.entity.AddressEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AddressRepository extends CrudRepository<AddressEntity, Long> {
}
