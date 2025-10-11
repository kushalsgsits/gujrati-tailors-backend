package com.harvi.tailor.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Slf4j
public class AuthenticationController {

  private final AuthenticationManager authenticationManager;
  private final JwtTokenUtil jwtTokenUtil;
  private final UserDetailsServiceImpl userDetailsService;

  @PostMapping("/authenticate")
  public AuthenticationResponse createAuthenticationToken(
      @RequestBody AuthenticationRequest authenticationRequest) {
    log.info("Authentication attempt for user: {}", authenticationRequest.username());
    authenticate(authenticationRequest.username(), authenticationRequest.password());

    UserDetails userDetails =
        userDetailsService.loadUserByUsername(authenticationRequest.username());
    String jwtToken = jwtTokenUtil.generateToken(userDetails);
    log.info("Authentication successful for user: {}", authenticationRequest.username());
    return new AuthenticationResponse(jwtToken);
  }

  private void authenticate(String username, String password) {
    authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
  }
}
