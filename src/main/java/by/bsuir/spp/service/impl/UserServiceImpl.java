package by.bsuir.spp.service.impl;

import by.bsuir.spp.exception.NotFoundException;
import by.bsuir.spp.exception.TaskManagerException;
import by.bsuir.spp.model.User;
import by.bsuir.spp.repository.UserRepository;
import by.bsuir.spp.service.UserService;
import by.bsuir.spp.service.util.UserValidator;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Integer id) throws TaskManagerException {
        UserValidator.validateUserId(id);
        try {
            return userRepository.findById(id).orElseThrow(() -> {
                log.error("Failed to find user by id: " + id);
                return new NotFoundException("Failed to find user by id: " + id);
            });
        } catch (DataAccessException e) {
            log.error("Failed to find user");
            throw new TaskManagerException("Failed to find user");
        }
    }

    @Override
    public User findByLogin(String login) {
        UserValidator.validateLogin(login);
        try {
            return userRepository.findByLogin(login).orElseThrow(() -> {
                log.error("Failed to find user by login: " + login);
                return new NotFoundException("Failed to find user by login: " + login);
            });
        } catch (DataAccessException e) {
            log.error("Failed to find user");
            throw new TaskManagerException("Failed to find user");
        }
    }

    @Override
    @Transactional(rollbackFor = TaskManagerException.class)
    public User save(User user) throws TaskManagerException {
       UserValidator.validateUser(user);
        try {
            return userRepository.save(user);
        } catch (DataAccessException e) {
            log.error("Failed to save user");
            throw new TaskManagerException("Failed to save user");
        }
    }

    @Override
    @Transactional(rollbackFor = TaskManagerException.class)
    public void deleteById(Integer id) throws TaskManagerException {
       UserValidator.validateUserId(id);
        try {
            userRepository.deleteById(id);
        } catch (DataAccessException e) {
            log.error("Failed to delete user by id: " + id);
            throw new TaskManagerException("Failed to delete user by id: " + id);
        }
    }
}
