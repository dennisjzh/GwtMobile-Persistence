package com.google.code.gwt.html5.database.persistence.client;

import com.google.gwt.core.client.JavaScriptObject;

public interface PersistableInternal extends Persistable{
	public JavaScriptObject getNativeObject();
}
