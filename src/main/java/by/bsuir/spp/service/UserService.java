package by.bsuir.spp.service;

import by.bsuir.spp.exception.TaskManagerException;
import by.bsuir.spp.model.User;

public interface UserService {

    User findById(Integer id) throws TaskManagerException;
    User findByLogin(String login) throws TaskManagerException;
    User save(User user) throws TaskManagerException;
    void deleteById(Integer id) throws TaskManagerException;
}
