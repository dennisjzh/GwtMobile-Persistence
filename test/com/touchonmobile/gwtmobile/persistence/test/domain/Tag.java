package com.touchonmobile.gwtmobile.persistence.test.domain;

import com.touchonmobile.gwtmobile.persistence.client.Persistable;
import com.touchonmobile.gwtmobile.persistence.client.Collection;

public interface Tag extends Persistable {
	public String getName();
	public void setName(String name);	
	public Collection<Task> getTasks();		// Many to many relationship
}
