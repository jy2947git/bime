package com.focaplo.myfuse.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.springframework.security.GrantedAuthority;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

/**
 * This class is used to represent available roles in the database.
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *         Version by Dan Kibler dan@getrolling.com
 *         Extended to implement Acegi GrantedAuthority interface
 *         by David Carter david@carter.net
 */
@Entity
@Table(name="role")
@NamedQueries ({
    @NamedQuery(
        name = "findRoleByName",
        query = "select r from Role r where r.id = :name "
        )
})
public class Role implements Serializable, GrantedAuthority {
    private static final long serialVersionUID = 3690197650654049848L;
//    private Long id;
    @Id 
    private String id;
    private String description;
    @Version
    private Integer version;
    public Integer getVersion() {
        return version;
    }
    public void setVersion(Integer version) {
        this.version = version;
    }
    /**
     * Default constructor - creates a new instance with no values set.
     */
    public Role() {
    }

    /**
     * Create a new instance and set the id.
     * @param id id of the role.
     */
    public Role(final String id) {
        this.id = id;
    }

//    @Id  @GeneratedValue(strategy=GenerationType.IDENTITY)
//    public Long getId() {
//        return id;
//    }

    /**
     * @see org.springframework.security.GrantedAuthority#getAuthority()
     * @return the id property (getAuthority required by Acegi's GrantedAuthority interface)
     */
    @Transient
    public String getAuthority() {
        return getId();
    }

    @Column(length=20)
    public String getId() {
        return this.id;
    }

    @Column(length=64)
    public String getDescription() {
        return this.description;
    }

//    public void setId(Long id) {
//        this.id = id;
//    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }

        final Role role = (Role) o;

        return !(id != null ? !id.equals(role.id) : role.id != null);

    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        return (id != null ? id.hashCode() : 0);
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SIMPLE_STYLE)
                .append(this.id)
                .toString();
    }

    public int compareTo(Object o) {
        return (equals(o) ? 0 : -1);
    }
}
