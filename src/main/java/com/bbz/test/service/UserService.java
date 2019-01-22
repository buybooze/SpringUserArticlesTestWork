package com.bbz.test.service;

import com.bbz.test.dao.UsersDao;
import com.bbz.test.model.User;
import com.bbz.test.validation.exception.EmailExistsException;
import com.bbz.test.validation.exception.UserNameExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UsersDao usersDao;

    public void registerNewUserAccount(User user) throws EmailExistsException, UserNameExistsException {
        if (emailExists(user.getEmail())) {
            throw new EmailExistsException("Пользователь с email: " +  user.getEmail() + " уже существует");
        }
        if (nameExists(user.getName())) {
            throw new UserNameExistsException("Пользователь с именем: " +  user.getName() + " уже существует");
        }
       usersDao.insert(user);
    }

    private boolean emailExists(String email) {
        User user = usersDao.findByEmail(email);
        if (user != null) {
            return true;
        }
        return false;
    }

    private boolean nameExists(String name) {
        User user = usersDao.findByName(name);
        if (user != null) {
            return true;
        }
        return false;
    }

    public void registerNewUserAccountBatch(List<User> users) {
        usersDao.insertBatch(users);
    }

    public User findUserById(int id) {
        return usersDao.findById(id);
    }

    public User findUserByEmail(String email) {
        return usersDao.findByEmail(email);
    }

    public User findUserByName(String name) {
        return usersDao.findByName(name);
    }

    public List<User> findAllUsers() {
        return usersDao.findAll();
    }

    public void deleteUser(int id) {
        usersDao.delete(id);
    }
}