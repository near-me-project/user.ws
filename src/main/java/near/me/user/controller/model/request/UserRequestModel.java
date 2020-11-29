package near.me.user.controller.model.request;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import near.me.user.controller.model.request.AddressRequestModel;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestModel {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private AddressRequestModel address;
}
