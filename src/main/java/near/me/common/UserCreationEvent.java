package near.me.common;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationEvent {

    private String userId;
    private String firstName;
    private String lastName;
    private Address address;
    private Location location;
}
