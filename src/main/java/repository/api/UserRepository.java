package repository.api;

import model.entity.Audit;
import model.entity.User;

import java.util.List;

public interface UserRepository  extends Crud<User>{
    User loadByUserName(String userName);
    List<Audit> findAudit(String username);
}
