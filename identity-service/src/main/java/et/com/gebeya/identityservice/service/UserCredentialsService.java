package et.com.gebeya.identityservice.service;

import et.com.gebeya.identityservice.entity.UserCredentials;
import et.com.gebeya.identityservice.repository.UserCredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserCredentialsService {
    private final UserCredentialsRepository userCredentialsRepository;

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                return userCredentialsRepository.findFirstByUserName(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    public UserCredentials createUpdateUser(UserCredentials users) {
        return userCredentialsRepository.save(users);
    }

    public UserCredentials loadUserByUsername(String userName) {
        return userCredentialsRepository.findFirstByUserName(userName).orElseThrow(() -> new IllegalArgumentException("Invalid user name or password"));
    }

    public Optional<UserCredentials> getUsers(String userName){
        return userCredentialsRepository.findFirstByUserName(userName);
    }
}
