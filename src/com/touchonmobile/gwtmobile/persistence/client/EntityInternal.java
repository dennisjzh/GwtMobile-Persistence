/*
 * Copyright 2010 Zhihua (Dennis) Jiang
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

package com.touchonmobile.gwtmobile.persistence.client;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;

public abstract class EntityInternal<T extends Persistable> implements Entity<T> {	
	public abstract  T newInstance(JavaScriptObject nativeObject);
	public abstract T[] newInstanceArray(int size);	//Cannot create a generic array in Java...
	public abstract JavaScriptObject getNativeObject();
	public abstract Collection<T> newCollection(JavaScriptObject nativeCollection);
	public abstract String getInverseRelationName(String rel);
	public abstract String getEntityName();
	
	public void load(Transaction transaction, String id, ScalarCallback<T> callback) {
		load(transaction, id, callback, getNativeObject(), this);
	}	
	private native void load(Transaction transaction, String id, ScalarCallback<T> callback, JavaScriptObject nativeObject, EntityInternal<T> self) /*-{
		nativeObject.load($wnd.persistence, transaction, id, function(result) {
			self.@com.touchonmobile.gwtmobile.persistence.client.EntityInternal::processwLoadCallback(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/touchonmobile/gwtmobile/persistence/client/ScalarCallback;)(result, callback);
		});
	}-*/;
	@SuppressWarnings("unused")
	private void processwLoadCallback(JavaScriptObject result, ScalarCallback<T> callback) {
		callback.onSuccess(result == null ? null : newInstance(result));
	}
	
	public void findBy(Transaction transaction, String property, boolean value, ScalarCallback<T> callback) {
		findBy(transaction, property, value, callback, getNativeObject(), this);
	}
	public void findBy(Transaction transaction, String property, char value, ScalarCallback<T> callback) {
		findBy(transaction, property, new String(new char[] {value}), callback, getNativeObject(), this);
	}
	public void findBy(Transaction transaction, String property, int value, ScalarCallback<T> callback) {
		findBy(transaction, property, value, callback, getNativeObject(), this);
	}
	public void findBy(Transaction transaction, String property, double value, ScalarCallback<T> callback) {
		findBy(transaction, property, value, callback, getNativeObject(), this);
	}
	public void findBy(Transaction transaction, String property, String value, ScalarCallback<T> callback) {
		findBy(transaction, property, value, callback, getNativeObject(), this);
	}
	public void findBy(Transaction transaction, String property, Date value, ScalarCallback<T> callback) {
		findBy(transaction, property, value.getTime(), callback, getNativeObject(), this);
	}
	private native void findBy(Transaction transaction, String property, boolean value, ScalarCallback<T> callback, JavaScriptObject nativeObject, EntityInternal<T> self) /*-{
		nativeObject.findBy($wnd.persistence, transaction, property, value, function(result) {
			self.@com.touchonmobile.gwtmobile.persistence.client.EntityInternal::processwLoadCallback(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/touchonmobile/gwtmobile/persistence/client/ScalarCallback;)(result, callback);
		});
	}-*/;
	private native void findBy(Transaction transaction, String property, String value, ScalarCallback<T> callback, JavaScriptObject nativeObject, EntityInternal<T> self) /*-{
		nativeObject.findBy($wnd.persistence, transaction, property, value, function(result) {
			self.@com.touchonmobile.gwtmobile.persistence.client.EntityInternal::processwLoadCallback(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/touchonmobile/gwtmobile/persistence/client/ScalarCallback;)(result, callback);
		});
	}-*/;
	// Can't pass long to JSNI...
	private native void findBy(Transaction transaction, String property, double value, ScalarCallback<T> callback, JavaScriptObject nativeObject, EntityInternal<T> self) /*-{
		nativeObject.findBy($wnd.persistence, transaction, property, value, function(result) {
			self.@com.touchonmobile.gwtmobile.persistence.client.EntityInternal::processwLoadCallback(Lcom/google/gwt/core/client/JavaScriptObject;Lcom/touchonmobile/gwtmobile/persistence/client/ScalarCallback;)(result, callback);
		});
	}-*/;
	
}
