package near.me.user.service.domain;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto implements Serializable {

    private long id;
    private String addressId;
    private String city;
    private String country;
    private UserDto userDetails;

}
