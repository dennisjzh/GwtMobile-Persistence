package com.google.code.gwt.html5.database.persistence.client;

public interface ScalarCallback<T extends Persistable> {
	public void onSuccess(T result);
}
