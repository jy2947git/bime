package com.focaplo.myfuse.model;

public interface Securable {


	public static final String edit="edit";
	public static final String delete="delete";
	public static final String authorize="authorize";
	

	public boolean getCanEdit();
	public void setCanEdit(boolean value);
	
	public boolean getCanDelete();
	public void setCanDelete(boolean value);
	
	
	public boolean getCanAuthorize();
	public void setCanAuthorize(boolean value);
	
	public boolean getIsLocked();
}
