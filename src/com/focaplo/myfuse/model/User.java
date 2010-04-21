package com.focaplo.myfuse.model;


import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.security.core.GrantedAuthority;

import com.focaplo.myfuse.Constants;

import javax.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class represents the basic "user" object in AppFuse that allows for authentication
 * and user management.  It implements Acegi Security's UserDetails interface.
 */
@Entity
@Table(name="app_user")
public class User extends BaseObject implements Serializable, org.springframework.security.core.userdetails.UserDetails {
    private static final long serialVersionUID = 3832626162173359411L;
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
    @Column(nullable=false,length=50,unique=true)
    private String username;                    // required
    @Column(nullable=false)
    private String password;                    // required
    @Transient
    private String confirmPassword;
    @Column(name="password_hint")
    private String passwordHint;
    @Column(name="first_name",nullable=false,length=50)
    private String firstName;                   // required
    @Column(name="last_name",nullable=false,length=50)
    private String lastName;                    // required
    @Column(nullable=true)
    private String email;                       // required; unique
    @Column(nullable=true)
    private String workPhoneNumber;
    @Column(nullable=true)
    private String homePhoneNumber;
    @Column(nullable=true)
    private String cellPhoneNumber;
    //private String website;
    //private Address address = new Address();
    @ManyToMany(mappedBy="owners", targetEntity=ManagedProject.class)
    private Set<ManagedProject> ownedProjects = new HashSet<ManagedProject>();
    
    @ManyToMany(mappedBy="participants", targetEntity=ManagedProject.class)
    private Set<ManagedProject> participatedProjects = new HashSet<ManagedProject>();
    
    
    @ManyToMany(mappedBy="accessedBy", targetEntity=ExperimentNote.class)
    private Set<ExperimentNote> accessedNotes = new HashSet<ExperimentNote>();
    
    @ManyToMany(mappedBy="participants", targetEntity=LabMeeting.class)
    private Set<LabMeeting> meetings = new HashSet<LabMeeting>();
    
    @ManyToMany
    @JoinTable(
            name="user_role",
            joinColumns = { @JoinColumn( name="user_id") },
            inverseJoinColumns = @JoinColumn( name="role_id")
    ) 
    private Set<Role> roles = new HashSet<Role>();

	
    @Column(name="account_enabled")
    private boolean enabled;
    @Column(name="account_expired",nullable=false)
    private boolean accountExpired;
    @Column(name="account_locked",nullable=false)
    private boolean accountLocked;
    @Column(name="credentials_expired",nullable=false)
    private boolean credentialsExpired;
    //
    @Column(nullable=true)
    private String title;
    @Column(nullable=true)
    private Date startDate;
    @Column(nullable=true)
    private Long superUserId;
    
    
    /**
     * Default constructor - creates a new instance with no values set.
     */
    public User() {}

    /**
     * Create a new instance and set the username.
     * @param username login name for user.
     */
    public User(final String username) {
        this.username = username;
    }

    
    public Long getId() {
        return id;
    }

    
    public String getUsername() {
        return username;
    }

    
    public String getPassword() {
        return password;
    }

    
    public String getConfirmPassword() {
        return confirmPassword;
    }

    
    public String getPasswordHint() {
        return passwordHint;
    }

    
    public String getFirstName() {
        return firstName;
    }

    
    public String getLastName() {
        return lastName;
    }

    
    public String getEmail() {
        return email;
    }

    
//    public String getPhoneNumber() {
//        return phoneNumber;
//    }
//
//    public String getWebsite() {
//        return website;
//    }

    /**
     * Returns the full name.
     * @return firstName + ' ' + lastName
     */
    @Transient
    public String getFullName() {
        return lastName + ',' + firstName;
    }

//    @Embedded
//    public Address getAddress() {
//        return address;
//    }

   
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * Convert user roles to LabelValue objects for convenience.
     * @return a list of LabelValue objects with role information
     */
    @Transient
    public List<LabelValue> getRoleList() {
        List<LabelValue> userRoles = new ArrayList<LabelValue>();

        if (this.roles != null) {
            for (Role role : roles) {
                // convert the user's roles to LabelValue Objects
                userRoles.add(new LabelValue(role.getId(), role.getId()));
            }
        }

        return userRoles;
    }

    /**
     * Adds a role for the user
     * @param role the fully instantiated role
     */
    public void addRole(Role role) {
        getRoles().add(role);
    }
    
    /**
     * @see org.springframework.security.userdetails.UserDetails#getAuthorities()
     * @return GrantedAuthority[] an array of roles.
     */
    @Transient
    public Collection<GrantedAuthority> getAuthorities() {
    	Set<GrantedAuthority> auths = new HashSet<GrantedAuthority>();
    	auths.addAll(roles);
    	return auths;
    }

    

    
    
    public boolean isEnabled() {
        return enabled;
    }
    
    
    public boolean isAccountExpired() {
        return accountExpired;
    }
    
    /**
     * @see org.springframework.security.userdetails.UserDetails#isAccountNonExpired()
     */
    @Transient
    public boolean isAccountNonExpired() {
        return !isAccountExpired();
    }

    
    public boolean isAccountLocked() {
        return accountLocked;
    }
    
    /**
     * @see org.springframework.security.userdetails.UserDetails#isAccountNonLocked()
     */
    @Transient
    public boolean isAccountNonLocked() {
        return !isAccountLocked();
    }

    
    public boolean isCredentialsExpired() {
        return credentialsExpired;
    }
    
    /**
     * @see org.springframework.security.userdetails.UserDetails#isCredentialsNonExpired()
     */
    @Transient
    public boolean isCredentialsNonExpired() {
        return !credentialsExpired;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

//    public void setPhoneNumber(String phoneNumber) {
//        this.phoneNumber = phoneNumber;
//    }
//
//    public void setWebsite(String website) {
//        this.website = website;
//    }
//
//    public void setAddress(Address address) {
//        this.address = address;
//    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }


    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
//
    public void setAccountExpired(boolean accountExpired) {
        this.accountExpired = accountExpired;
    }
    
    public void setAccountLocked(boolean accountLocked) {
        this.accountLocked = accountLocked;
    }

    public void setCredentialsExpired(boolean credentialsExpired) {
        this.credentialsExpired = credentialsExpired;
    }
//    
    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }

        final User user = (User) o;

        return !(username != null ? !username.equals(user.getUsername()) : user.getUsername() != null);

    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (username != null ? username.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("username", this.username);

        Collection<GrantedAuthority> auths = this.getAuthorities();
        if (auths != null) {
            sb.append("Granted Authorities: ");

            for (GrantedAuthority auth:auths) {
                
                    sb.append(", ");
                
                sb.append(auth.toString());
            }
        } else {
            sb.append("No Granted Authorities");
        }
        return sb.toString().trim();
    }

	public String getWorkPhoneNumber() {
		return workPhoneNumber;
	}

	public void setWorkPhoneNumber(String workPhoneNumber) {
		this.workPhoneNumber = workPhoneNumber;
	}

	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}

	public void setHomePhoneNumber(String homePhoneNumber) {
		this.homePhoneNumber = homePhoneNumber;
	}

	public String getCellPhoneNumber() {
		return cellPhoneNumber;
	}

	public void setCellPhoneNumber(String cellPhoneNumber) {
		this.cellPhoneNumber = cellPhoneNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Long getSuperUserId() {
		return superUserId;
	}

	public void setSuperUserId(Long superUserId) {
		this.superUserId = superUserId;
	}


    
//    public Lab getLab() {
//		return lab;
//	}
//
//	public void setLab(Lab lab) {
//		this.lab = lab;
//	}

	public boolean getIsSuperUser(){
    	Set<Role>roles = this.getRoles();
    	Iterator<Role> ite = roles.iterator();
    	while(ite.hasNext()){
    		Role r = ite.next();
    		if(r.getId().equalsIgnoreCase(Constants.SUPER_USER_ROLE)){
    			return true;
    		}
    	}
    	return false;
    }

	public Set<ManagedProject> getOwnedProjects() {
		return ownedProjects;
	}

	public void setOwnedProjects(Set<ManagedProject> ownedProjects) {
		this.ownedProjects = ownedProjects;
	}

	public Set<ManagedProject> getParticipatedProjects() {
		return participatedProjects;
	}

	public void setParticipatedProjects(Set<ManagedProject> participatedProjects) {
		this.participatedProjects = participatedProjects;
	}

	public Set<ExperimentNote> getAccessedNotes() {
		return accessedNotes;
	}

	public void setAccessedNotes(Set<ExperimentNote> accessedNotes) {
		this.accessedNotes = accessedNotes;
	}

	public Set<LabMeeting> getMeetings() {
		return meetings;
	}

	public void setMeetings(Set<LabMeeting> meetings) {
		this.meetings = meetings;
	}
    
    
}
