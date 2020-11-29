package near.me.user.controller;

import near.me.user.controller.model.request.UserRequestModel;
import near.me.user.controller.model.response.UserResponseModel;
import near.me.user.service.UserService;
import near.me.user.service.domain.UserDto;
import near.me.user.shared.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path = "/user")
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path = "/{userId}")
    public UserResponseModel getUser(@PathVariable(name = "userId") String userId) {
        UserDto userDtoOutcome = userService.getUserById(userId);
        return ModelMapper.map(userDtoOutcome, UserResponseModel.class);
    }

    @PostMapping
    public UserResponseModel createUser(@RequestBody UserRequestModel userRequestModel) {
        UserDto userDto = ModelMapper.map(userRequestModel, UserDto.class);
        UserDto userDtoOutcome = userService.createUser(userDto);
        return ModelMapper.map(userDtoOutcome, UserResponseModel.class);
    }
}
