package app.hotel.apphotel.service;

import app.hotel.apphotel.entity.User;
import app.hotel.apphotel.entity.enums.ROLE;
import app.hotel.apphotel.payload.ApiResponse;
import app.hotel.apphotel.repository.AuthRepository;
import app.hotel.apphotel.security.JwtUtils;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.Optional;
import java.util.Random;

@Service
public class AuthService {

    private final JwtUtils jwtUtils;

    private final AuthRepository authRepository;

    private final JavaMailSender javaMailSender;


    public AuthService(JwtUtils jwtUtils, AuthRepository authRepository, JavaMailSender javaMailSender) {
        this.jwtUtils = jwtUtils;
        this.authRepository = authRepository;
        this.javaMailSender = javaMailSender;
    }

    public ApiResponse register(User userDTO) {
        User user = new User();

        if (!checkUsername(userDTO.getUsername())) {
            return new ApiResponse("Username already exist!", 409);
        }
        user.setUsername(user.getUsername());
        if (!checkEmail(userDTO.getEmail())) {
            return new ApiResponse("Bad request", 400);
        }
        if (authRepository.existsByEmail(userDTO.getEmail())){
            return new ApiResponse("Gmail already exist!",409);
        }
        user.setEmail(userDTO.getEmail());
        user.setRoles(userDTO.getRoles());
        user.setPassword(userDTO.getPassword());
        user.setUsername(userDTO.getUsername());
        String token=String.valueOf(generateRandom(6));
        user.setVerificationPassword(token);
        authRepository.save(user);
        String text="http://localhost:8080/auth/confirm="+token+"/"+user.getId();
        sendSimpleEmail(user.getEmail(),"Verify your account",text);
        return new ApiResponse("We sent verification url to your email, please verify your account.", true);
    }

    public ApiResponse login(String username,String password){
        if (username.isEmpty() && password.isEmpty()) {
            return new ApiResponse("Required fields are missing", 422);
        }
        User byUsernameAndPassword = authRepository.findByUsernameAndPassword(username, password);
        if (byUsernameAndPassword == null) {
            return new ApiResponse(400, "Username or password unavailable");
        }
        String token = jwtUtils.generateToken(byUsernameAndPassword.getUsername());

        return new ApiResponse("token: ",200, token);
    }

    public ApiResponse resetPassword(String gmail,String newPassword){
        User byEmail = authRepository.findByEmail(gmail);
        if (byEmail!=null){
            String token=String.valueOf(generateRandom(6));
            byEmail.setPassword(newPassword);
            byEmail.setVerificationPassword(token);
            authRepository.save(byEmail);
            String text="http://localhost:8080/auth/confirm="+token+"/"+byEmail.getId();
            sendSimpleEmail(byEmail.getEmail(),"Verify your account",text);
            return new ApiResponse("We sent verification url to your email, please verify your account.", true);
        }
        return new ApiResponse("User with this email not found!",404);
    }

    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ApiResponse giveRole(Integer userId, ROLE role){
        User user=new User();
        Optional<User> byId = authRepository.findById(userId);
        if (byId.isPresent()){
            User user1=byId.get();
            user.setUsername(user1.getUsername());
            user.setPassword(user1.getPassword());
            user.setEmail(user1.getEmail());
            user.setEnabled(user1.isEnabled());
            user.setRoles(role);
            user.setVerificationPassword(user1.getVerificationPassword());
            authRepository.delete(user1);
            authRepository.save(user);
            return new ApiResponse("Role changed",200);
        }
        return new ApiResponse("User not found",404);
    }

    public ApiResponse confirmToken(String token,Integer id){
        Optional<User> byId = authRepository.findById(id);
        if (byId.isPresent()){
            if (byId.get().getVerificationPassword().equals(token)){
                byId.get().setEnabled(true);
                return new ApiResponse("Account verified!",200);
            }
        }
        return new ApiResponse("Token isn't available",500);
    }


    public boolean checkEmail(String gmail) {
        String regex = "^[a-zA-Z0-9._%+-]+@gmail\\.com$";
        return gmail.matches(regex);
    }

    public boolean checkUsername(String username) {
        User byUsername = authRepository.findByUsername(username);
        return byUsername == null;
    }

    public int generateRandom(int integerLength) {
        Random random = new Random();
        int min = (int) Math.pow(10, integerLength - 1);
        int max = (int) Math.pow(10, integerLength) - 1;
        return random.nextInt(max - min + 1) + min;
    }

    public void sendSimpleEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("intellijideatrial@gmail.com");
        javaMailSender.send(message);
    }
}
