package com.google.code.gwt.html5.database.persistence.client;

public interface CollectionCallback<T extends Persistable> {
	public void onSuccess(T[] results);
}
