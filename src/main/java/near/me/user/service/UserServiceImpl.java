package near.me.user.service;

import near.me.common.Address;
import near.me.common.Location;
import near.me.common.UserCreationEvent;
import near.me.common.UserDeleteEvent;
import near.me.user.repository.UserRepository;
import near.me.user.repository.entity.UserEntity;
import near.me.user.service.domain.UserDto;
import near.me.user.service.messaging.RabbitClient;
import near.me.user.shared.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private RabbitClient rabbitClient;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, RabbitClient rabbitClient) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.rabbitClient = rabbitClient;
    }

    @Override
    public UserDto getUserById(String userId) {
        UserEntity from = userRepository.findByUserId(userId);
        return ModelMapper.map(from, UserDto.class);
    }

    @Override
    public UserDto createUser(UserDto userDto) {
        if (userRepository.findByEmailAsOptional(userDto.getEmail()).isPresent()) {
            throw new RuntimeException(String.format("User with email [%s] already exists", userDto.getEmail()));
        }

        final UserEntity savedEntity = createNewUser(userDto);

        return ModelMapper.map(savedEntity, UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {

        UserEntity userEntity = userRepository.findByEmail(userDto.getEmail());

        if (userEntity != null) {
            final UserEntity savedEntity = updateExistingUser(userEntity, userDto);
            return ModelMapper.map(savedEntity, UserDto.class);
        } else {
            final UserEntity savedEntity = createNewUser(userDto);
            return ModelMapper.map(savedEntity, UserDto.class);
        }
    }

    @Transactional
    @Override
    public void deleteUser(String userId) {
        userRepository.deleteByUserId(userId);

        rabbitClient.sendEvent(UserDeleteEvent.builder().userId(userId).build(), RabbitClient.SOCIAL_NETWORK_QUEUE_USER_DELETE_EVENT);
    }

    private UserEntity updateExistingUser(UserEntity oldUserEntity, UserDto userDto) {

        UserEntity updatedEntity = ModelMapper.map(userDto, UserEntity.class);
        updatedEntity.setId(oldUserEntity.getId());
        updatedEntity.setUserId(oldUserEntity.getUserId());

        final UserEntity savedEntity = userRepository.save(updatedEntity);

        UserCreationEvent event = UserCreationEvent.builder()
                .userId(savedEntity.getUserId())
                .firstName(savedEntity.getFirstName())
                .lastName(savedEntity.getLastName())
                .address(Address.builder().city(savedEntity.getAddress().getCity()).country(savedEntity.getAddress().getCountry()).build())
                .location(Location.builder().city(userDto.getLocation().getCity()).country(userDto.getLocation().getCity()).build())
                .build();

        rabbitClient.sendEvent(event, RabbitClient.SOCIAL_NETWORK_QUEUE_USER_UPDATE_EVENT);
        return savedEntity;
    }

    private UserEntity createNewUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());
        userDto.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userDto.getAddress().setAddressId(UUID.randomUUID().toString());
        final UserEntity savedEntity = userRepository.save(ModelMapper.map(userDto, UserEntity.class));

        UserCreationEvent event = UserCreationEvent.builder()
                .userId(savedEntity.getUserId())
                .firstName(savedEntity.getFirstName())
                .lastName(savedEntity.getLastName())
                .address(Address.builder().city(savedEntity.getAddress().getCity()).country(savedEntity.getAddress().getCountry()).build())
                .location(Location.builder().city(userDto.getLocation().getCity()).country(userDto.getLocation().getCity()).build())
                .build();

        rabbitClient.sendEvent(event, RabbitClient.SOCIAL_NETWORK_QUEUE_USER_CREATION_EVENT);
        return savedEntity;
    }

    @Override
    public UserDto getUserDetailsByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmailAsOptional(email).orElseThrow(() -> new RuntimeException(String.format("User with id [%s] wasn't find", email)));
        return ModelMapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmailAsOptional(email).orElseThrow(() -> new RuntimeException(String.format("User with id [%s] wasn't find", email)));
        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }
}
