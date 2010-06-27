package com.google.code.gwt.html5.database.persistence.client;

public interface Collection<T extends Persistable>{

	public Collection<T> filter(String property, String operator, boolean value);
	public Collection<T> filter(String property, String operator, int value);
	public Collection<T> filter(String property, String operator, float value);
	public Collection<T> filter(String property, String operator, String value);
	public Collection<T> prefetch(String rel);
	public void list(Transaction tx, CollectionCallback<T> callback);
	public void list(CollectionCallback<T> callback);
	public void each(Transaction tx, ScalarCallback<T> callback);
	public void each(ScalarCallback<T> callback);
	public void add(T obj);
	public void remove(T obj);
	
}
