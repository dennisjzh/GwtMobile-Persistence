/*
 * Copyright 2010 Zhihua (Dennis) Jiang.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

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
