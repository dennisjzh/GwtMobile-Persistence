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

import java.util.Map;

import com.google.gwt.core.client.JavaScriptObject;

public class Persistence {
	
	private static Boolean _autoAdd = true;
	
	static {
		initializeConsole();
	}
	
	private static native void initializeConsole() /*-{
		if (!$wnd.console) $wnd.console = {};
		$wnd.console.log = $wnd.console.log || function(){};
		$wnd.console.warn = $wnd.console.warn || function(){};
		$wnd.console.error = $wnd.console.error || function(){};
		$wnd.console.info = $wnd.console.info || function(){};
	}-*/;
	
	public static Boolean getAutoAdd() {
		return _autoAdd;
	}

	public static void setAutoAdd(Boolean autoAdd) {
		_autoAdd = autoAdd;
	}

	public static native void connect(String dbname, String description, int size) /*-{
		$wnd.persistence.connect(dbname, description, size);
	}-*/;
	
	public static JavaScriptObject define(String entityName, Map<String, String> fields) {
		JavaScriptObject assoArray = Map2AssociativeArray(fields);
		return define(entityName, assoArray);
	};

	public static native void transaction(TransactionCallback callback) /*-{
		$wnd.persistence.transaction(
			function(transaction) {
				callback.@com.google.code.gwt.html5.database.persistence.client.TransactionCallback::onSuccess(Lcom/google/code/gwt/html5/database/persistence/client/Transaction;)(transaction);
			}
		);
	}-*/;
	
	public static native void schemaSync(TransactionCallback callback) /*-{
		$wnd.persistence.schemaSync(
			function(transaction) {
				callback.@com.google.code.gwt.html5.database.persistence.client.TransactionCallback::onSuccess(Lcom/google/code/gwt/html5/database/persistence/client/Transaction;)(transaction);
			}
		);
	}-*/;

	public static native void schemaSync(Callback callback) /*-{
		$wnd.persistence.schemaSync(
			function(transaction) {
				callback.@com.google.code.gwt.html5.database.persistence.client.Callback::onSuccess()();
			}
		);
	}-*/;

	public static void add(Persistable persistable) {
		add(((PersistableInternal)persistable).getNativeObject());
	}

	private static native void add(JavaScriptObject obj) /*-{
		$wnd.persistence.add(obj);
	}-*/;

	public static void remove(Persistable persistable) {
		remove(((PersistableInternal)persistable).getNativeObject());
	}

	private static native void remove(JavaScriptObject obj) /*-{
		$wnd.persistence.remove(obj);
	}-*/;

	public static native void flush(Transaction transaction, Callback callback) /*-{
		$wnd.persistence.flush(transaction,
			function() {
				callback.@com.google.code.gwt.html5.database.persistence.client.Callback::onSuccess()();
			}
		);
	}-*/;

	public static native void flush(Callback callback) /*-{
		$wnd.persistence.flush(null,
			function() {
				callback.@com.google.code.gwt.html5.database.persistence.client.Callback::onSuccess()();
			}
		);
	}-*/;

	public static native void reset(Transaction transaction) /*-{
		$wnd.persistence.reset(transaction);
	}-*/;

	public static native void reset() /*-{
		$wnd.persistence.transaction(function (transaction) {
			$wnd.persistence.reset(transaction);
		});
	}-*/;

	private static JavaScriptObject Map2AssociativeArray(
			Map<String, String> fields) {
		JavaScriptObject assoArray = JavaScriptObject.createObject();
		for (Map.Entry<String, String> field : fields.entrySet()) {
			setAssoArray(assoArray, field.getKey(), field.getValue());
		}
		return assoArray;
	}

	private static native void setAssoArray(JavaScriptObject assoArray, String key,
			String value) /*-{
		assoArray[key] = value;		
	}-*/;

	public static native JavaScriptObject define(String entityName, JavaScriptObject fields) /*-{
		return $wnd.persistence.define(entityName, fields);
	}-*/;	
}
