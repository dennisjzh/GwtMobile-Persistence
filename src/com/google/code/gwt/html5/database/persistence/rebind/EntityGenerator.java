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

public class EntityGenerator implements ClassGenerator {

	final GenUtils utils;
	final String requestedClassName;
	final String generatedClassName;
	final List<JMethod> getters;
	final List<JMethod> hasManyRels;
	final List<JMethod> invHasManyRels;

	public EntityGenerator(GenUtils utils, String requestedClassName, String generatedClassName, List<JMethod> getters, List<JMethod> hasManyRels, List<JMethod> invHasManyRels) {
		this.utils = utils;
		this.requestedClassName = requestedClassName;
		this.generatedClassName = generatedClassName;
		this.getters = getters;
		this.hasManyRels = hasManyRels;
		this.invHasManyRels = invHasManyRels;
	}

	@Override
	public void classSetup() {
		AddImports();
		SetSuperClass();
		AddImplementedInterfaces();
	}
	private void AddImports() {
		utils.factory().addImport("java.util.HashMap");
		utils.factory().addImport("com.google.gwt.core.client.JavaScriptObject");
		utils.factory().addImport("com.google.gwt.core.client.JsArray");
		utils.factory().addImport("com.google.code.gwt.html5.database.persistence.client.*");
		utils.factory().addImport("com.google.gwt.core.client.GWT");
	}
	private void SetSuperClass() {
		//utils.factory().setSuperclass(typeName);
	}
	private void AddImplementedInterfaces() {
		utils.factory().addImplementedInterface("EntityInternal<" + requestedClassName + ">");
	}			
	private void AddVariables() {
		StringBuilder sb = new StringBuilder();
		sb.append(String.format("Persistence.define(\"%s\", new HashMap<String, String> () {{\n", requestedClassName));
		for (JMethod getter : getters) {
			String propertyName = getter.getName().substring(3);
			String propertyType = utils.getSQLiteType(getter.getReturnType());
			sb.append(String.format("\tput(\"%s\", \"%s\");\n", propertyName, propertyType));
		}
		sb.append("}});");
		utils.addVariable("private static", "JavaScriptObject", "nativeEntity", sb.toString());
		utils.addVariable("private static", generatedClassName, "entity", String.format("new %s()", generatedClassName));
		for (JMethod hasManyRel : hasManyRels) {
			String hasManyRelName = hasManyRel.getName().substring(3);
			String hasManyRelEntity = utils.getGenericTypeShortName(hasManyRel.getReturnType().getParameterizedQualifiedSourceName());
			utils.addVariable("private static", "EntityInternal<" + hasManyRelEntity + ">", 
					"hasMany" + hasManyRelName, "GWT.create(" + hasManyRelEntity + ".class)");
		}
		for (JMethod invHasManyRel : invHasManyRels) {
			String invHasManyRelEntity = invHasManyRel.getName().substring(3);
			utils.addVariable("private static", "EntityInternal<" + invHasManyRelEntity + ">", 
					"hasManyInverse" + invHasManyRelEntity, "GWT.create(" + invHasManyRelEntity + ".class)");
		}
	}
	@Override
	public void generateClass() {				
		AddVariables();
		utils.genrateStaticConstructor(new MethodGenerator() {

			@Override
			public void generateMethod() {
				for (JMethod hasManyRel : hasManyRels) {
					String hasManyRelName = hasManyRel.getName().substring(3);
					String hasManyRelEntity = utils.getGenericTypeShortName(hasManyRel.getReturnType().getParameterizedQualifiedSourceName());
					utils.println("hasMany(nativeEntity, \"%s\", hasMany%ss.getNativeObject(), hasMany%ss.getInverseRelationName(\"%s\"));", 
							hasManyRelName, hasManyRelEntity, hasManyRelEntity, requestedClassName);
				}
			}
		});
		
		utils.generateMethod("public", "JavaScriptObject", "getNativeObject", null,
				new MethodGenerator() {
			@Override
			public void generateMethod() {
				utils.println("return nativeEntity;");
			}
		});

		utils.generateMethod("public", requestedClassName, "newInstance", 
				new String[][] {{"JavaScriptObject", "nativeObject"}},
				new MethodGenerator() {
					@Override
					public void generateMethod() {
						utils.println("return new %sImpl(nativeObject);", requestedClassName);  
					}
				});

		utils.generateMethod("public", requestedClassName, "newInstance", null,
				new MethodGenerator() {
			@Override
			public void generateMethod() {
				utils.println("JavaScriptObject nativeObject = %s.newInstanceNative(nativeEntity, Persistence.getAutoAdd());", generatedClassName);
				utils.println("return new %sImpl(nativeObject);", requestedClassName);
			}
		});

		utils.generateMethod("public", "Collection<" + requestedClassName + ">", "all", null,
				new MethodGenerator() {
			@Override
			public void generateMethod() {
				utils.println("JavaScriptObject nativeObject = %s.all(nativeEntity);", generatedClassName);
				utils.println("return new Collection%sImpl(nativeObject);", requestedClassName);
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

		utils.generateMethod("public", "String", "getInverseRelationName",
				new String[][] {{"String", "rel"}}, 
				new MethodGenerator(){
					public void generateMethod() {
						for (JMethod hasManyRel : hasManyRels) {
							String hasManyRelName = hasManyRel.getName().substring(3);
							String hasManyRelEntity = utils.getGenericTypeShortName(hasManyRel.getReturnType().getParameterizedQualifiedSourceName());
							utils.println("if (rel.equals(\"%s\")) {", hasManyRelEntity);
							utils.sw().indent();
							utils.println("return \"%s\";", hasManyRelName);
							utils.sw().outdent();
							utils.println("}");
						}
						for (JMethod invHasManyRel : invHasManyRels) {
							String inverseRelationName = invHasManyRel.getName().substring(3);
							String inverseRelEntity = invHasManyRel.getName().substring(3);
							utils.println("if (rel.equals(\"%s\")) {", inverseRelEntity);
							utils.sw().indent();
							utils.println("return \"%s\";", inverseRelationName);
							utils.sw().outdent();
							utils.println("}");
						}
						utils.println("return null;");
					}});

		utils.generateNativeMethod("private static", "JavaScriptObject", "newInstanceNative",
				new String[][] {
					{"JavaScriptObject", "nativeEntity"}, 
					{"Boolean", "autoAdd"}},
				new MethodGenerator() {
					@Override
					public void generateMethod() {
						utils.println("var instance = new nativeEntity();");
						utils.println("if (autoAdd) {");
						utils.println("\t$wnd.persistence.add(instance);");
						utils.println("}");
						utils.println("return instance;");
					}
				});

		utils.generateNativeMethod("private static", "JavaScriptObject", "all",
				new String[][] {{"JavaScriptObject", "nativeEntity"}},
				new MethodGenerator() {
					@Override
					public void generateMethod() {
						utils.println("return nativeEntity.all();");
					}
				});

		utils.generateNativeMethod("private static", "void", "hasMany",
				new String[][] {
					{"JavaScriptObject", "nativeEntity"},
					{"String", "collName"},
					{"JavaScriptObject", "otherEntity"},
					{"String", "invRel"}},
				new MethodGenerator() {
					@Override
					public void generateMethod() {
						utils.println("nativeEntity.hasMany(collName, otherEntity, invRel);");
					}
				});

		utils.generateMethod("public static", "void", "processCallback",
				new String[][] {
				{"JsArray<JavaScriptObject>", "results"},
				{"CollectionCallback<" + requestedClassName + ">", "callback"}},
				new MethodGenerator() {
					@Override
					public void generateMethod() {
						utils.println("%s[] array = new %s[results.length()];", requestedClassName, requestedClassName);
						utils.println("for (int i = 0; i < array.length; i++) {");
						utils.println("\tarray[i] = entity.newInstance(results.get(i));");
						utils.print("}");
						utils.print("callback.onSuccess(array);");
					}
				});
		
		utils.generateMethod("public static", "void", "processCallback",
				new String[][] {
				{"JavaScriptObject", "result"},
				{"ScalarCallback<" + requestedClassName + ">", "callback"}}, 						
				new MethodGenerator() {
					@Override
					public void generateMethod() {
						utils.print("callback.onSuccess(entity.newInstance(result));");
					}
				});

		utils.generateInnerClass("private", requestedClassName + "Impl", requestedClassName, new String[]{"PersistableInternal"},
				new InstanceGenerator(utils, requestedClassName, generatedClassName, getters, hasManyRels, invHasManyRels));

		utils.generateInnerClass("private", "Collection" + requestedClassName + "Impl", 
				"CollectionBase<" + requestedClassName + ">", null,
				new CollectionGenerator(utils, requestedClassName, generatedClassName, getters, hasManyRels, invHasManyRels));

	}
}
