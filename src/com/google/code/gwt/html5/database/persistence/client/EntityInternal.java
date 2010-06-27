package com.google.code.gwt.html5.database.persistence.client;

import com.google.gwt.core.client.JavaScriptObject;

public interface EntityInternal<T extends Persistable> extends Entity<T> {	
	public T newInstance(JavaScriptObject nativeObject);
	public JavaScriptObject getNativeObject();
	public Collection<T> newCollection(JavaScriptObject nativeCollection);
	public String getInverseRelationName(String rel);
}
