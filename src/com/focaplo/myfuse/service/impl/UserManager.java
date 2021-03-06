package com.focaplo.myfuse.service.impl;

import java.util.ArrayList;
import java.util.List;


import javax.persistence.EntityExistsException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.focaplo.myfuse.exception.UserExistsException;
import com.focaplo.myfuse.model.LabelValue;
import com.focaplo.myfuse.model.User;
import com.focaplo.myfuse.service.UserService;



/**
 * Implementation of UserManager interface.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
@Service(value="userManager")
@Transactional(readOnly=true)
public class UserManager extends UniversalManager implements UserService {
   @Autowired
    private PasswordEncoder passwordEncoder;
  

    /**
     * Set the PasswordEncoder used to encrypt passwords.
     * @param passwordEncoder the PasswordEncoder implementation
     */
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * {@inheritDoc}
     */
    public User getUser(String userId) {
        return (User) userDao.get(User.class, new Long(userId));
    }

    /**
     * {@inheritDoc}
     */
    public List<User> getUsers(User user) {
        return userDao.getUsers();
    }
    
    
    @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
    public User saveUser(User user) throws UserExistsException {

        if (user.getVersion() == null) {
            // if new user, lowercase userId
            user.setUsername(user.getUsername().toLowerCase());
        }
        
        // Get and prepare password management-related artifacts
        boolean passwordChanged = false;
        if (passwordEncoder != null) {
            // Check whether we have to encrypt (or re-encrypt) the password
            if (user.getVersion() == null) {
                // New user, always encrypt
                passwordChanged = true;
            } else {
                // Existing user, check password in DB
                String currentPassword = userDao.getUserPassword(user.getUsername());
                if (currentPassword == null) {
                    passwordChanged = true;
                } else {
                    if (!currentPassword.equals(user.getPassword())) {
                        passwordChanged = true;
                    }
                }
            }

            // If password was changed (or new user), encrypt it
            log.debug("password changed?" + passwordChanged);
            if (passwordChanged) {
                user.setPassword(passwordEncoder.encodePassword(user.getPassword(), null));
            }
        } else {
            log.warn("PasswordEncoder not set, skipping password encryption...");
        }
        
        try {
            return userDao.saveUser(user);
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        } catch (EntityExistsException e) { // needed for JPA
            e.printStackTrace();
            log.warn(e.getMessage());
            throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
        }
    }

    @Transactional(readOnly=false, propagation=Propagation.REQUIRED)
    public void removeUser(String userId) {
        log.debug("removing user: " + userId);
        userDao.remove(User.class,new Long(userId));
    }

    /**
     * {@inheritDoc}
     * @param username the login name of the human
     * @return User the populated user object
     * @throws UsernameNotFoundException thrown when username not found
     */
    public User getUserByUsername(String username) throws UsernameNotFoundException {
        return (User) userDao.loadUserByUsername(username);
    }

	public List<User> getUsersWithRole(String superUserRole) {
		return this.userDao.getUsersWithRole(superUserRole);
	}



	public List<User> getAllLabUsers() {
		return this.userDao.getLabUsers();
	}
}
