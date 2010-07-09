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

public class InstanceGenerator implements ClassGenerator {

	final GenUtils utils;
	final String requestedClassName;
	final String generatedClassName;
	final List<JMethod> getters;
	final List<JMethod> hasManyRels;
	final List<JMethod> hasOneRels;

	public InstanceGenerator(GenUtils utils, String requestedClassName, String generatedClassName, List<JMethod> getters, List<JMethod> hasManyRels, List<JMethod> hasOneRels) {
		this.utils = utils;
		this.requestedClassName = requestedClassName;
		this.generatedClassName = generatedClassName;
		this.getters = getters;
		this.hasManyRels = hasManyRels;
		this.hasOneRels = hasOneRels;
	}
	
	@Override
	public void classSetup() {
		utils.addVariable("private", "JavaScriptObject", "nativeObject");								
	}
	@Override
	public void generateClass() {
		utils.generateMethod("public", null, requestedClassName + "Impl", 
				new String[][] {{"JavaScriptObject", "nativeObject"}},
				new MethodGenerator() {
					@Override
					public void generateMethod() {
						utils.println("this.nativeObject = nativeObject;");
					}
				});

		utils.generateMethod("public", "JavaScriptObject", "getNativeObject", null,
				new MethodGenerator() {
			@Override
			public void generateMethod() {
				utils.println("return nativeObject;");
			}
		});
		
		for (final JMethod getter : getters) {
			//TODO: is getter method always public?
			utils.generateMethod("public", getter.getReturnType().getSimpleSourceName(), getter.getName(), 
					null, new MethodGenerator(){
						@Override
						public void generateMethod() {
							utils.println("return " + getter.getName() + "(nativeObject);"); 											
						}});
			utils.generateNativeMethod("private", getter.getReturnType().getSimpleSourceName(), getter.getName(), 
					new String[][]{{"JavaScriptObject", "nativeObject"}},
					new MethodGenerator(){
						@Override
						public void generateMethod() {
							utils.println("return nativeObject." + getter.getName().substring(3) + ";"); 											
						}});
			utils.generateMethod("public", "void", "set" + getter.getName().substring(3), 
					new String[][]{{getter.getReturnType().getSimpleSourceName(), "value"}},
					new MethodGenerator(){
						@Override
						public void generateMethod() {
							utils.println("set" + getter.getName().substring(3) + "(value, nativeObject);"); 											
						}});
			utils.generateNativeMethod("private", "void", "set" + getter.getName().substring(3), 
					new String[][]{
						{getter.getReturnType().getSimpleSourceName(), "value"},
						{"JavaScriptObject", "nativeObject"}},
					new MethodGenerator(){
						@Override
						public void generateMethod() {
							utils.println("nativeObject." + getter.getName().substring(3) + " = value;"); 											
						}});
		}
		
		for (final JMethod hasManyRel : hasManyRels) {
			utils.generateMethod("public", hasManyRel.getReturnType().getParameterizedQualifiedSourceName(), hasManyRel.getName(), 
					null, new MethodGenerator(){
						@Override
						public void generateMethod() {
							utils.println("return hasMany" + hasManyRel.getName().substring(3) + 
									".newCollection(" + hasManyRel.getName() + "(nativeObject));"); 											
						}});							
			utils.generateNativeMethod("private", "JavaScriptObject", hasManyRel.getName(), 
					new String[][]{{"JavaScriptObject", "nativeObject"}},
					new MethodGenerator(){
						@Override
						public void generateMethod() {
							utils.println("return nativeObject." + hasManyRel.getName().substring(3) + ";"); 											
						}});							
		}
		
		for (final JMethod hasOneRel : hasOneRels) {
			utils.generateMethod("public", hasOneRel.getReturnType().getSimpleSourceName(), hasOneRel.getName(), 
					null, new MethodGenerator(){
						@Override
						public void generateMethod() {
							utils.println("return hasOne" + hasOneRel.getName().substring(3) + 
									".newInstance(" + hasOneRel.getName() + "(nativeObject));"); 											
						}});
			utils.generateNativeMethod("private", "JavaScriptObject", hasOneRel.getName(), 
					new String[][]{{"JavaScriptObject", "nativeObject"}},
					new MethodGenerator(){
						@Override
						public void generateMethod() {
							utils.println("return nativeObject." + hasOneRel.getName().substring(3) + ";"); 											
						}});
			utils.generateMethod("public", "void", "set" + hasOneRel.getName().substring(3), 
					new String[][]{{hasOneRel.getReturnType().getSimpleSourceName(), "value"}},
					new MethodGenerator(){
						@Override
						public void generateMethod() {
							utils.println("set" + hasOneRel.getName().substring(3) + "(((PersistableInternal)(value)).getNativeObject(), nativeObject);"); 											
						}});
			utils.generateNativeMethod("private", "void", "set" + hasOneRel.getName().substring(3), 
					new String[][]{
						{"JavaScriptObject", "value"},
						{"JavaScriptObject", "nativeObject"}},
					new MethodGenerator(){
						@Override
						public void generateMethod() {
							utils.println("nativeObject." + hasOneRel.getName().substring(3) + " = value;"); 											
						}});
		}
	}	


}
