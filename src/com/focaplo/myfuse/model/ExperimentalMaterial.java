package com.focaplo.myfuse.model;

import java.io.Serializable;
import javax.persistence.*;
@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="experimental_material")
public class ExperimentalMaterial extends BaseObject implements Serializable {
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) 
	private Long id;
	@Column(nullable=false,length=50)
	private String name;
	
	@Override
	public boolean equals(Object o) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
