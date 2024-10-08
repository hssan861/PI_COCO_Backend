package tn.esprit.backendpi.Controller;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import jakarta.mail.MessagingException;
import jakarta.validation.Valid;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import tn.esprit.backendpi.Entities.ERole;
import tn.esprit.backendpi.Entities.Role;
import tn.esprit.backendpi.Entities.User;
import tn.esprit.backendpi.Payload.Request.*;
import tn.esprit.backendpi.Payload.Response.MessageResponse;
import tn.esprit.backendpi.Payload.Response.UserInfoResponse;
import tn.esprit.backendpi.Repository.RoleRepository;
import tn.esprit.backendpi.Repository.UserRepository;
import tn.esprit.backendpi.Security.Jwt.JwtUtils;
import tn.esprit.backendpi.Security.Services.UserDetailsImpl;
import tn.esprit.backendpi.Service.Classes.EmailService;
import tn.esprit.backendpi.Service.Classes.ForgotPasswordService;
import tn.esprit.backendpi.Service.Classes.ResetPasswordService;
import tn.esprit.backendpi.Service.Interfaces.IUserService;


//for Angular Client (withCredentials)
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600, allowCredentials="true")

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    ForgotPasswordService forgotPasswordService;

    @Autowired
    ResetPasswordService resetPasswordService;
    @Autowired
    IUserService iUserService;
    @Autowired
    EmailService emailService;
    @Autowired
    JavaMailSender javaMailSender;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        // Authenticate user based on username and password
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // Get UserDetails from authenticated user

        Optional<User> user =userRepository.findByUsername(authentication.getName());
        // Check if the user's account is verified
        if (!user.get().isVerified()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Your account is not verified. Please verify your account to proceed."));
        }
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        // Get user roles
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());
        // Return JWT token, user details, and roles
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail(),
                        roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) throws MessagingException {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        // Generate verification code
        String verificationCode = emailService.generateVerificationCode();
        user.setVerificationCode(verificationCode);

        // Save user to database
        userRepository.save(user);

        // Send verification code via email
        emailService.sendVerificationCodeByEmail(user.getEmail(), verificationCode,user.getUsername(),javaMailSender);

        return ResponseEntity.ok(new MessageResponse("User registered successfully! Verification code sent to your email."));
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }
    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) throws MessagingException {
        forgotPasswordService.sendPasswordResetEmail(request.getUsername());
        return "Mail sent successfully";
    }
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam("token") String token, @RequestBody ResetPasswordRequest request) {
        resetPasswordService.resetPassword(token, request.getPassword());
        return "Password reset successfully!";
    }
    @PostMapping("/verify")
    public ResponseEntity<?> verifyAccount(@RequestBody VerificationRequest verificationRequest) {
        String verificationCode = verificationRequest.getVerificationCode();
        User user = userRepository.findByVerificationCode(verificationCode);
        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Invalid verification code."));
        }

        // Update user's verification status
        user.setVerified(true);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Account verified successfully! You can now login."));
    }





}