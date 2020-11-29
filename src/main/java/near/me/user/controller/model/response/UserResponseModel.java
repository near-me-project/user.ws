package near.me.user.controller.model.response;

import lombok.Getter;
import lombok.Setter;
import near.me.user.controller.model.response.AddressResponseModel;

@Getter
@Setter
public class UserResponseModel {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private AddressResponseModel address;

}

