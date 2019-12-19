package at.wero.spring.auth.service;

import at.wero.spring.auth.error.UsernameExistException;
import at.wero.spring.auth.model.User;

public interface UserService {
    void register(User user) throws UsernameExistException;

    User findByUsername(String username);
}
