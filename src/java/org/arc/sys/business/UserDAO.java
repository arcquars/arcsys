/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;
import org.arc.sys.hibernate.entities.User;

/**
 *
 * @author angel
 */
public interface UserDAO {
    
    public User createUser(
            Integer userId,
            Integer perId,
            String login,
            String password);
    
    public User readUser(Integer userId);
    
    public boolean userValid(String login, String password);
    public int getUserId(String login, String password);
    
}
