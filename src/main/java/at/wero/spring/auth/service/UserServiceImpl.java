package at.wero.spring.auth.service;

import at.wero.spring.auth.error.UsernameExistException;
import at.wero.spring.auth.model.Role;
import at.wero.spring.auth.model.User;
import at.wero.spring.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                           BCryptPasswordEncoder bCryptPasswordEncoder) {
        Assert.notNull(userRepository, "Argument userRepository must not be null");
        Assert.notNull(bCryptPasswordEncoder, "Argument bCryptPasswordEncoder must not be null");
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void register(User user) throws UsernameExistException {
        if (user == null) throw new IllegalArgumentException("Parameter user must not be null");
        if (StringUtils.isEmpty(user.getUsername())) throw new IllegalArgumentException("Username must not be empty");
        if (StringUtils.containsWhitespace(user.getUsername())) throw new IllegalArgumentException("Username must not contain whitespace");

        User alreadyRegUser = userRepository.findByUsername(user.getUsername());
        if (alreadyRegUser != null) throw new UsernameExistException("Username exists");

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.ROLE_USER));
        userRepository.save(user);
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}
