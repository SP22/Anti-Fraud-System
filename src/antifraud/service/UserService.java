package antifraud.service;

import antifraud.entity.User;
import antifraud.exceptions.DuplicateEntityException;
import antifraud.exceptions.EntityNotFoundException;
import antifraud.exceptions.UnsupportedRoleException;
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
        if (userRepository.count() == 0) {
            user.setRole("ADMINISTRATOR");
        } else {
            user.setLocked(true);
        }
        if (null == user.getRole()) {
            user.setRole("MERCHANT");
        }
        if (null == userRepository.findByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            return userRepository.save(user);
        }
        throw new DuplicateEntityException("User already exists");
    }

    public List<User> getUsers() {
        return userRepository.findAllByOrderByIdAsc();
    }

    public void delete(String username) {
        User user = userRepository.findByUsername(username);
        if (null == user) {
            throw new EntityNotFoundException();
        }
        userRepository.delete(user);
    }

    public void setLocked(String username, String locked) {
        User user = userRepository.findByUsername(username);
        boolean toLock = "LOCK".equals(locked);
        if ("UNLOCK".equals(locked)) {
            toLock = false;
        }
        user.setLocked(toLock);
        userRepository.save(user);
    }

    public User setRole(String username, String role) {
        User user = userRepository.findByUsername(username);
        if (null == user) {
            throw new EntityNotFoundException();
        }
        if (!"MERCHANT".equals(role) && !"SUPPORT".equals(role)) {
            throw new UnsupportedRoleException(role);
        }
        if (user.getRole().equals(role)) {
            throw new DuplicateEntityException(username);
        }
        user.setRole(role);
        return userRepository.save(user);
    }
}
