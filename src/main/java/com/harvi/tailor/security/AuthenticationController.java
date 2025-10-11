package com.harvi.tailor.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class AuthenticationController {

  @Autowired private AuthenticationManager authenticationManager;

  @Autowired private JwtTokenUtil jwtTokenUtil;

  @Autowired private UserDetailsServiceImpl userDetailsService;

  @PostMapping("/authenticate")
  public AuthenticationResponse createAuthenticationToken(
      @RequestBody AuthenticationRequest authenticationRequest) throws Exception {

    authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

    UserDetails userDetails =
        userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
    String jwtToken = jwtTokenUtil.generateToken(userDetails);
    return new AuthenticationResponse(jwtToken);
  }

  private void authenticate(String username, String password) throws Exception {
    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }
  }
}
