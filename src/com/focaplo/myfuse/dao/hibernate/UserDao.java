package com.focaplo.myfuse.dao.hibernate;

import java.util.List;

import javax.persistence.Table;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import com.focaplo.myfuse.Constants;
import com.focaplo.myfuse.dao.IUserDao;
import com.focaplo.myfuse.model.User;

/**
 * This class interacts with Spring's HibernateTemplate to save/delete and
 * retrieve User objects.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *   Modified by <a href="mailto:dan@getrolling.com">Dan Kibler</a>
 *   Extended to implement Acegi UserDetailsService interface by David Carter david@carter.net
 *   Modified by <a href="mailto:bwnoll@gmail.com">Bryan Noll</a> to work with 
 *   the new BaseDaoHibernate implementation that uses generics.
*/
@Repository(value="userDao")
public class UserDao extends UniversalDao implements IUserDao, UserDetailsService {


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<User> getUsers() {
        return this.getSessionFactory().getCurrentSession().createQuery("from User u order by upper(u.username)").list();
    }

    /**
     * {@inheritDoc}
     */
    public User saveUser(User user) {
        log.debug("user's id: " + user.getId());
        this.getSessionFactory().getCurrentSession().saveOrUpdate(user);
        // necessary to throw a DataIntegrityViolation and catch it in UserManager
//        getHibernateTemplate().flush();
        return user;
    }

    /**
     * Overridden simply to call the saveUser method. This is happenening 
     * because saveUser flushes the session and saveObject of BaseDaoHibernate 
     * does not.
     *
     * @param user the user to save
     * @return the modified user (with a primary key set if they're new)
     */
    public User save(User user) {
        this.saveOrUpdate(user);
        return user;
    }

    /** 
     * {@inheritDoc}
    */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = this.getSessionFactory().getCurrentSession().createQuery("from User where username=?").setString(0, username).list();
        if (users == null || users.isEmpty()) {
            throw new UsernameNotFoundException("user '" + username + "' not found...");
        } else {
            return (UserDetails) users.get(0);
        }
    }

    /** 
     * {@inheritDoc}
    */
    public String getUserPassword(String username) {
        SimpleJdbcTemplate jdbcTemplate =
                new SimpleJdbcTemplate(SessionFactoryUtils.getDataSource(getSessionFactory()));
        Table table = AnnotationUtils.findAnnotation(User.class, Table.class);
        return jdbcTemplate.queryForObject(
                "select password from " + table.name() + " where username=?", String.class, username);

    }

	public List<User> getUsersWithRole(String role) {
		return this.getSessionFactory().getCurrentSession().createQuery("select u from User as u inner join u.roles as r where r.id=?").setString(0, role).list();
	}

	public List<User> getLabUsers() {
		return this.getSessionFactory().getCurrentSession().createQuery("select distinct u from User as u inner join u.roles as r where r.id in ('" + Constants.SUPER_USER_ROLE + "','" + Constants.USER_ROLE + "')").list();
	}
    
}
