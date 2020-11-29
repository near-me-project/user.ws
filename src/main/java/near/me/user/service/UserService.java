package near.me.user.service;

import near.me.user.service.domain.UserDto;
import org.springframework.security.core.userdetails.UserDetailsService;


public interface UserService extends UserDetailsService {
    UserDto createUser(UserDto userDto);

    UserDto getUserDetailsByEmail(String username);

    UserDto getUserById(String userId);
}
