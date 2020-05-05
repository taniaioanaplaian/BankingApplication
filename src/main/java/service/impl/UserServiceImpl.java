package service.impl;
import javafx.scene.control.Button;
import model.dto.UserDto;
import model.entity.Audit;
import model.entity.User;
import model.enumeration.Role;
import repository.api.AuditRepository;
import repository.api.UserRepository;
import service.api.UserService;
import utils.Utils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class.getName());
    private final UserRepository userRepository;
    private final AuditRepository auditRepository;
    private  String currentUsername;
    public UserServiceImpl(AuditRepository auditRepository, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.auditRepository = auditRepository;

    }
    public void setCurrentUsername(String currentUsername) {
        this.currentUsername = currentUsername;
    }

    @Override
    public User login(String userName, String password) {
        User user = userRepository.loadByUserName(userName);
        if(user != null) {
            if(user.getPassword().equals(Utils.getEncryptedPassword(password))) {
                currentUsername = userName;
                auditRepository.create(new Audit(LocalDateTime.now(), userName, "login"));
                return user;
            } else {
                LOGGER.warning("Wrong password for user " + userName);
            }
        } else {
            LOGGER.warning("User with username: " + userName + " was not found");
        }
        return null;
    }

    @Override
    public User register(String username, String password, Role role) {

        User user = new User(username, Utils.getEncryptedPassword(password), role);
        user = userRepository.create(user);
        if(user == null){
            LOGGER.warning("Problem during registration");
        }
        auditRepository.create(new Audit(LocalDateTime.now(), username, "register"));
        return user;
    }

    public List<UserDto> findUsers(){
        List<UserDto> users = new ArrayList<>();
        List<User> usersFound = userRepository.findAll();
        if(usersFound == null){
            LOGGER.warning("Problem during search");
        }
        assert usersFound != null;
        for(User u : usersFound){
                users.add(new UserDto(u.getUserName(), u.getRole().toString(), new Button("Delete")));
        }

        auditRepository.create(new Audit(LocalDateTime.now(), currentUsername, "findUsers"));
        return users;
    }

    @Override
    public boolean deleteUser(String username) {
           boolean delete = userRepository.delete(userRepository.loadByUserName(username).getId());
            if(!delete){
                LOGGER.warning("Could not delete!");
            }
        auditRepository.create(new Audit(LocalDateTime.now(), username, "deleteUser"));
           return delete;
    }

    @Override
    public void updateUser(Long id, String username, String role) {

        User user = userRepository.findById(id);
        if(user == null){
            LOGGER.warning("Could not find user!!");
        }
        assert user != null;
        user.setUserName(username);
        if ("ADMINISTRATOR".equals(role)) {
            user.setRole(Role.ADMINISTRATOR);
        } else {
            user.setRole(Role.EMPLOYEE);
        }

       user = userRepository.update(user);
        auditRepository.create(new Audit(LocalDateTime.now(), username, "updateUser"));
        if(user == null){
            LOGGER.warning("Could not update!");
        }
    }

    @Override
    public User findByUsername(String username) {
        User  user  =  userRepository.loadByUserName(username);
        if(user == null){
            LOGGER.warning("Could not find requested user!");
        }
        auditRepository.create(new Audit(LocalDateTime.now(), username, "findUserByName"));
        return user;
    }

    @Override
    public List<Audit> getAudit(String username) {
        return userRepository.findAudit(username);
    }


}
