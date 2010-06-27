package com.google.code.gwt.html5.database.persistence.client;

import com.google.gwt.core.client.JavaScriptObject;


public abstract class CollectionBase<T extends Persistable> implements Collection<T> {
	protected JavaScriptObject nativeObject;

	public CollectionBase(JavaScriptObject nativeObject) {
		this.nativeObject = nativeObject;
	}
	
	public abstract Collection<T> newCollection(JavaScriptObject nativeObject);

	@Override
	public Collection<T> filter(String property, String operator, boolean value) {
		return newCollection(filter(nativeObject, property, operator, value));
	}
	private native JavaScriptObject filter(JavaScriptObject nativeObject, String property, String operator, boolean value) /*-{
		return nativeObject.filter(property, operator, value);			
	}-*/;
	@Override
	public Collection<T> filter(String property, String operator, int value) {
		return newCollection(filter(nativeObject, property, operator, value));
	}
	private native JavaScriptObject filter(JavaScriptObject nativeObject, String property, String operator, int value) /*-{
		return nativeObject.filter(property, operator, value);			
	}-*/;
	@Override
	public Collection<T> filter(String property, String operator, float value) {
		return newCollection(filter(nativeObject, property, operator, value));
	}
	private native JavaScriptObject filter(JavaScriptObject nativeObject, String property, String operator, float value) /*-{
		return nativeObject.filter(property, operator, value);			
	}-*/;
	@Override
	public Collection<T> filter(String property, String operator, String value) {
		return newCollection(filter(nativeObject, property, operator, value));
	}
	private native JavaScriptObject filter(JavaScriptObject nativeObject, String property, String operator, String value) /*-{
		return nativeObject.filter(property, operator, value);			
	}-*/;
	
	@Override
	public Collection<T> prefetch(String rel) {
		return newCollection(prefetch(nativeObject, rel));
	}
	private native JavaScriptObject prefetch(JavaScriptObject nativeObject, String rel) /*-{
		return nativeObject.prefetch(rel);
	}-*/;
	
	@Override
	public void add(T obj) {
		add(nativeObject, ((PersistableInternal)obj).getNativeObject());
	}
	private native void add(JavaScriptObject nativeObject, JavaScriptObject obj) /*-{
		nativeObject.add(obj);
	}-*/;

	@Override
	public void remove(T obj) {
		remove(nativeObject, ((PersistableInternal)obj).getNativeObject());
	}
	private native void remove(JavaScriptObject nativeObject, JavaScriptObject obj) /*-{
		nativeObject.remove(obj);
	}-*/;

}
