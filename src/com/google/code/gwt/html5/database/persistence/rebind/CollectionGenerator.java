package com.google.code.gwt.html5.database.persistence.rebind;

import java.util.List;

import com.google.gwt.core.ext.typeinfo.JMethod;

public class CollectionGenerator implements ClassGenerator {

	final GenUtils utils;
	final String requestedClassName;
	final String generatedClassName;
	final List<JMethod> getters;
	final List<JMethod> hasManyRels;
	final List<JMethod> revHasManyRels;

	public CollectionGenerator(GenUtils utils, String requestedClassName, String generatedClassName, List<JMethod> getters, List<JMethod> hasManyRels, List<JMethod> revHasManyRels) {
		this.utils = utils;
		this.requestedClassName = requestedClassName;
		this.generatedClassName = generatedClassName;
		this.getters = getters;
		this.hasManyRels = hasManyRels;
		this.revHasManyRels = revHasManyRels;
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
