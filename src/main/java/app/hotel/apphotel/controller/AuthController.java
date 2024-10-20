package app.hotel.apphotel.controller;

import app.hotel.apphotel.entity.User;
import app.hotel.apphotel.entity.enums.ROLE;
import app.hotel.apphotel.payload.ApiResponse;
import app.hotel.apphotel.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        ApiResponse register = authService.register(user);
        return ResponseEntity.ok(register);
    }

    @GetMapping("/login")
    public ResponseEntity<?> login(@RequestBody String username,@RequestBody String password){
        return ResponseEntity.ok(authService.login(username,password));
    }

    @PostMapping("/reset")
    public ResponseEntity<?> resetPassword(@RequestBody String email,@RequestBody String newPassword){
        return ResponseEntity.ok(authService.resetPassword(email,newPassword));
    }

    @PostMapping("/giveRole/{id}")
    public ResponseEntity<?> giveRole(@PathVariable Integer id, @RequestParam ROLE role){
        return ResponseEntity.ok(authService.giveRole(id,role));
    }

    @GetMapping("/confirm")
    public ResponseEntity<?> confirm(@RequestParam String token,Integer id){
        ApiResponse apiResponse = authService.confirmToken(token, id);
        return ResponseEntity.ok(apiResponse);
    }
}
