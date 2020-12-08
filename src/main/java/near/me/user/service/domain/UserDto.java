package near.me.user.service.domain;

import lombok.*;
import near.me.user.service.domain.AddressDto;

import java.io.Serializable;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto implements Serializable {

    private long id;
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String encryptedPassword;
    private AddressDto address;
    private LocationDto location;
}
