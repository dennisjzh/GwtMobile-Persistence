package com.google.code.gwt.html5.database.persistence.client;


public interface Entity<T extends Persistable> {	
	public T newInstance();
	public Collection<T> all();
}
