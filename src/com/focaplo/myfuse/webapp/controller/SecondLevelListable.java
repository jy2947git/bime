package com.focaplo.myfuse.webapp.controller;

import java.util.List;

import com.focaplo.myfuse.model.BaseObject;

public interface SecondLevelListable {
	List<? extends BaseObject> getListOfChildModels(Long parentId);
}
