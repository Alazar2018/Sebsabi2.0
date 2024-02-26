package et.com.gebeya.identityservice.service;


import et.com.gebeya.identityservice.dto.requestDto.TokenDto;
import et.com.gebeya.identityservice.dto.requestDto.UserInformation;
import et.com.gebeya.identityservice.dto.requestDto.UserRequestDto;
import et.com.gebeya.identityservice.dto.responseDto.AuthenticationResponse;
import et.com.gebeya.identityservice.dto.responseDto.UserResponseDto;
import et.com.gebeya.identityservice.dto.responseDto.ValidationResponseDto;
import et.com.gebeya.identityservice.entity.UserCredentials;
import et.com.gebeya.identityservice.repository.UserCredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserCredentialsRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserCredentialsService userCredentialsService;
    public AuthenticationResponse signIn(UserInformation usersCredential)  {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usersCredential.getUsername(), usersCredential.getPassword()));
        UserCredentials user = userRepository.findFirstByUserName(usersCredential.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user name or password"));
        String jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwt).authority(user.getAuthority()).build();
    }
    public ResponseEntity<UserResponseDto> signup(UserRequestDto userRequestDto){
        final String userName=userRequestDto.getUserName();
            if(userRepository.findFirstByUserName(userName).isPresent()){
                throw new RuntimeException("User already exists with this email");
            }else{
                UserResponseDto responseDto=userCredentialsService.signUp(userRequestDto);
                new ResponseEntity<>(responseDto, HttpStatus.CREATED);
            }
        return new ResponseEntity<>(HttpStatus.IM_USED);
    }

    public ResponseEntity<ValidationResponseDto> validate(TokenDto token)
    {
        final String userName;
        userName = jwtService.extractUserName(token.getToken());
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        if (StringUtils.isNotEmpty(userName)) {
            UserCredentials users = userCredentialsService.loadUserByUsername(userName);
            if (jwtService.isTokenValid(token.getToken(), users)) {
                ValidationResponseDto response = ValidationResponseDto.builder()
                        .userId(users.getUserId())
                        .authority(users.getAuthorities().toString())
                        .build();

                return new ResponseEntity<>(response, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

}
