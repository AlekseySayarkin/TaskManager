package by.bsuir.spp.security;

import by.bsuir.spp.exception.TaskManagerException;
import by.bsuir.spp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return UserDetailsImpl.of(userService.findByLogin(username));
        } catch (TaskManagerException e) {
            throw new UsernameNotFoundException(e.getMessage());
        }
    }
}
