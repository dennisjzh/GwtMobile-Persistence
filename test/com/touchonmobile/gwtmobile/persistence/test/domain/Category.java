package com.touchonmobile.gwtmobile.persistence.test.domain;

import com.touchonmobile.gwtmobile.persistence.client.Persistable;
import com.touchonmobile.gwtmobile.persistence.client.Collection;

public interface Category extends Persistable {
	public String getName();				// Property
	public void setName(String name);
	public Collection<Task> getTasks();		// One to many relationship
}
