package at.wero.spring.auth.controller;

import at.wero.spring.auth.model.User;
import at.wero.spring.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        Assert.notNull(userService, "Argument userService must not be null");
        this.userService = userService;
    }

    @PostMapping(
            value = "/register",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> register(@RequestBody User user) {
        this.userService.register(user);
        return ResponseEntity.ok().build();
    }
}
