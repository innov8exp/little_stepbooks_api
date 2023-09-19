package co.botechservices.novlnovl.infrastructure.security.admin;


import co.botechservices.novlnovl.domain.admin.iam.assembler.AdminAuthAssembler;
import co.botechservices.novlnovl.domain.admin.iam.entity.AdminUserEntity;
import co.botechservices.novlnovl.domain.admin.iam.service.AdminUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class AdminDetailsServiceImpl implements UserDetailsService {

    private final AdminUserService adminUserService;

    public AdminDetailsServiceImpl(AdminUserService adminUserService) {
        this.adminUserService = adminUserService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminUserEntity adminUserEntity = adminUserService.findUserByUsername(username);
        if (ObjectUtils.isEmpty(adminUserEntity)) {
            throw new UsernameNotFoundException("Cannot found the user with username: " + username);
        }
        return AdminAuthAssembler.adminUserEntityToJwtUserDetails(adminUserEntity);
    }
}
