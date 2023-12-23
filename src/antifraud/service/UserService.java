package antifraud.service;

import antifraud.entity.User;
import antifraud.exceptions.DuplicateUserException;
import antifraud.exceptions.UserNotFoundException;
import antifraud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User create(User user) {
        if (null == userRepository.findByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        throw new DuplicateUserException("User already exists");
    }

    public List<User> getUsers() {
        return userRepository.findAllByOrderByIdAsc();
    }

    public void delete(String username) {
        User user = userRepository.findByUsername(username);
        if (null == user) {
            throw new UserNotFoundException();
        }
        userRepository.delete(user);
    }
}
