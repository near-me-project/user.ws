package near.me.user.controller.model.request;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LocationRequestModel {

    private String city;
    private String country;
}
