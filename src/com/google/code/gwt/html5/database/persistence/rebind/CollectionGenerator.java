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

package com.google.code.gwt.html5.database.persistence.rebind;

import java.util.List;

import com.google.gwt.core.ext.typeinfo.JMethod;

public class CollectionGenerator implements ClassGenerator {

	final GenUtils utils;
	final String requestedClassName;
	final String generatedClassName;
	final List<JMethod> getters;
	final List<JMethod> hasManyRels;
	final List<JMethod> hasOneRels;

	public CollectionGenerator(GenUtils utils, String requestedClassName, String generatedClassName, List<JMethod> getters, List<JMethod> hasManyRels, List<JMethod> hasOneRels) {
		this.utils = utils;
		this.requestedClassName = requestedClassName;
		this.generatedClassName = generatedClassName;
		this.getters = getters;
		this.hasManyRels = hasManyRels;
		this.hasOneRels = hasOneRels;
	}

	@Override
	public void classSetup() {
	}
	@Override
	public void generateClass() {
		utils.generateMethod("public", null, "Collection" + requestedClassName + "Impl", 
				new String[][] {{"JavaScriptObject", "nativeObject"}}, 						
				new MethodGenerator() {
			@Override
			public void generateMethod() {
				utils.println("super(nativeObject);");
			}
		});

		utils.generateMethod("public", "Collection<" + requestedClassName + ">", "newCollection", 
				new String[][] {{"JavaScriptObject", "nativeObject"}}, 						
				new MethodGenerator() {
			@Override
			public void generateMethod() {
				utils.println("return new Collection%sImpl(nativeObject);", requestedClassName);
			}
		});

		utils.generateMethod("public", "void", "list", 
				new String[][] {
				{"Transaction", "tx"},
				{"CollectionCallback<" + requestedClassName + ">", "callback"}}, 						
				new MethodGenerator() {
			@Override
			public void generateMethod() {
				utils.println("list(nativeObject, tx, callback);");
			}
		});

		utils.generateMethod("public", "void", "list", 
				new String[][] {
				{"CollectionCallback<" + requestedClassName + ">", "callback"}}, 						
				new MethodGenerator() {
			@Override
			public void generateMethod() {
				utils.println("list(nativeObject, null, callback);");
			}
		});
		utils.generateNativeMethod("private", "void", "list",
				new String[][] {
				{"JavaScriptObject", "nativeObject"},
				{"Transaction", "tx"},
				{"CollectionCallback<" + requestedClassName + ">", "callback"}},
				new MethodGenerator() {
			@Override
			public void generateMethod() {
				utils.println("nativeObject.list(tx, function(result) {");
				String packageName = utils.factory().getCreatedPackage();
				utils.println("\t@%s.%s::processCallback(" +
						"Lcom/google/gwt/core/client/JsArray;Lcom/google/code/gwt/html5/database/persistence/client/CollectionCallback;) (result, callback);", 
						packageName, generatedClassName);
				utils.println("});");
			}
		});

		utils.generateMethod("public", "void", "each", 
				new String[][] {
				{"Transaction", "tx"},
				{"ScalarCallback<" + requestedClassName + ">", "callback"}}, 						
				new MethodGenerator() {
			@Override
			public void generateMethod() {
				utils.println("each(nativeObject, tx, callback);", requestedClassName);
			}
		});

		utils.generateMethod("public", "void", "each", 
				new String[][] {
				{"ScalarCallback<" + requestedClassName + ">", "callback"}}, 						
				new MethodGenerator() {
			@Override
			public void generateMethod() {
				utils.println("each(nativeObject, null, callback);", requestedClassName);
			}
		});

		utils.generateNativeMethod("private", "void", "each",
				new String[][] {
				{"JavaScriptObject", "nativeObject"},
				{"Transaction", "tx"},
				{"ScalarCallback<" + requestedClassName + ">", "callback"}},
				new MethodGenerator() {
			@Override
			public void generateMethod() {
				utils.println("nativeObject.each(tx, function(result) {");
				String packageName = utils.factory().getCreatedPackage();
				utils.println("\t@%s.%s::processCallback(" +
						"Lcom/google/gwt/core/client/JavaScriptObject;Lcom/google/code/gwt/html5/database/persistence/client/ScalarCallback;) (result, callback);", 
						packageName, generatedClassName);
				utils.println("});");
			}
		});
	}	

}
