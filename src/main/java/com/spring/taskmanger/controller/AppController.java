package com.spring.taskmanger.controller;

import com.spring.taskmanger.dao.TokenRepository;
import com.spring.taskmanger.dao.UserRepository;
import com.spring.taskmanger.filters.JwtUtil;
import com.spring.taskmanger.model.Token;
import com.spring.taskmanger.model.User;
import com.spring.taskmanger.payload.request.LoginRequest;
import com.spring.taskmanger.payload.request.SignupRequest;
import com.spring.taskmanger.payload.response.JwtResponse;
import com.spring.taskmanger.payload.response.MessageResponse;
import com.spring.taskmanger.security.CustomUserDetails;
import com.spring.taskmanger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
@CrossOrigin(origins = "*", maxAge = 3600)

//@RequestMapping("/auth")
@RestController
public class AppController {
    @Autowired
    UserService userService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtil jwtUtil;

    private final Logger logger = LoggerFactory.getLogger(AppController.class);


    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) throws Exception {

        try {

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = jwtUtil.generateJwtToken(authentication);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // adding the jwt To the data Base
            long userId = userDetails.getId();
            User user = userRepository.findById(userId).get();
            Token token = new Token(jwt, user);
            tokenRepository.save(token);
            logger.info("New Token was saved successfully to User with Id" + userId);


            //  System.out.println( userDetails.getId() +""+ userDetails.getUsername()+""+ userDetails.getName());
            return ResponseEntity.ok(new JwtResponse(jwt,
                    userDetails.getId(),
                    userDetails.getUsername(),
                    userDetails.getName(),
                    userDetails.getAge()
                    ));
       }

        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already taken!"));
        }

        User user = new User(signUpRequest.getName(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getAge());

        userService.addUser(user);

       return ResponseEntity.ok("New User has registered Succefully ");
    }


    @GetMapping("/exit")
    public ResponseEntity<?> logOut() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUser = (CustomUserDetails)authentication.getPrincipal();
        long userId = customUser.getId();
         User user = userRepository.findById(userId).get();
        String token = customUser.getToken();
        logger.info("userId" +userId + "and token is" + token );

        tokenRepository.deleteByJwtandUserId(token, user);

        return ResponseEntity.ok("You Are Logged Out!");

    }


    @GetMapping("/exitAll")
    public ResponseEntity<?> logOutAll() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails customUser = (CustomUserDetails)authentication.getPrincipal();
        long userId = customUser.getId();
        User user = userRepository.findById(userId).get();
        tokenRepository.deleteByUserId(user);

        return ResponseEntity.ok("You Are Logged Out From All devices !");

    }







    /*
    @GetMapping("")
    public String login() {
        return "index"; // return index in the templet file
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user",new User());
        return "register_form"; // return index in the templet file
    }

    @GetMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userService.addUser(user);

        return "register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = Task.findAll();
        model.addAttribute("listUsers", listUsers);

        return "users";
    }
*/


}
