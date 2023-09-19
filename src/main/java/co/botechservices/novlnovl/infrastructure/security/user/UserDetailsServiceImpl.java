package co.botechservices.novlnovl.infrastructure.security.user;


import co.botechservices.novlnovl.domain.user.assembler.AuthAssembler;
import co.botechservices.novlnovl.domain.user.entity.UserEntity;
import co.botechservices.novlnovl.domain.user.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userService.findUserByUsername(username);
        if (ObjectUtils.isEmpty(userEntity)) {
            throw new UsernameNotFoundException("Cannot found the user with username: " + username);
        }
        return AuthAssembler.userEntityToJwtUserDetails(userEntity);
    }
}
