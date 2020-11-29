package near.me.user.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity(name = "addresses")
@Table(name = "addresses")
public class AddressEntity implements Serializable {

    @Id
    @GeneratedValue
    private long id;

    @Column(name = "address_id", length = 256, nullable = false)
    private String addressId;

    @Column(name = "city", length = 15, nullable = false)
    private String city;

    @Column(name = "country", length = 15, nullable = false)
    private String country;

    @OneToOne(mappedBy = "address")
    private UserEntity userDetails;
}
