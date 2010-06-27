package com.google.code.gwt.html5.database.persistence.rebind;

import java.io.PrintWriter;
import java.util.List;

import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JMethod;
import com.google.gwt.core.ext.typeinfo.JPrimitiveType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class GenUtils {
	private TreeLogger logger;
	private GeneratorContext context;
	private SourceWriter sw;
	private ClassSourceFileComposerFactory factory;
	
	public GenUtils(TreeLogger logger, GeneratorContext context) {
		this.logger = logger;
		this.context = context;
	}
	
	public String generateClass(String requestedTypeName, String gennedTypeNameSuffix, 
			ClassGenerator classGenerator) {
        // The package the class is going to be in.
        String packageName = getPackageName(requestedTypeName);
        String gennedTypeName = getClassName(requestedTypeName) + gennedTypeNameSuffix;
        PrintWriter pw = context.tryCreate(logger, packageName, gennedTypeName);
       
        // No PrintWriter means we've generated the same code before.
        if (pw != null) {
            factory = new ClassSourceFileComposerFactory(packageName, gennedTypeName);
            classGenerator.classSetup();
            sw = factory.createSourceWriter(context, pw);                        
            classGenerator.generateClass();
            sw.commit(logger);
        }
        return packageName + "." + gennedTypeName;
	}

	public void generateInnerClass(String modifiers, String className, String superClass, String[] interfaces,
			ClassGenerator classGenerator) {
		sw.println();
		print("%s class %s", modifiers, className);
		if (superClass != null) {
			print(" extends %s", superClass);
		}
		if (interfaces != null) {
			print(" implements ");
			for (int i = 0; i < interfaces.length; i++) {
				if (i > 0) {
					print(",");
				}
				print(interfaces[i]);
			}
		}
		println(" {");
		sw.indent();
		classGenerator.classSetup();
		classGenerator.generateClass();
		sw.outdent();
		println("}");
	}
	
	public void genrateStaticConstructor(MethodGenerator bodyGenerator) {
		sw.println();
		sw.println("static {");
		sw.indent();
		bodyGenerator.generateMethod();
		sw.outdent();
		sw.println("}");
	}

	public boolean generateMethod(String modifiers, String returnType, 
			String methodName, String[][] params, MethodGenerator methodGenerator) {
		return generateMethod(modifiers, returnType, methodName, params, false, methodGenerator);	
	}
	
	public boolean generateNativeMethod(String modifiers, String returnType, 
			String methodName, String[][] params, MethodGenerator methodGenerator) {
		return generateMethod(modifiers, returnType, methodName, params, true, methodGenerator);	
	}
	
	private boolean generateMethod(String modifiers, String returnType, 
			String methodName, String[][] params, boolean isNative, MethodGenerator methodGenerator) {
		
		sw.println();
		sw.print(modifiers + " ");
		if (isNative) {
			sw.print("native ");
		}
		if (returnType != null) {
			sw.print(returnType + " ");
		}
		sw.print(methodName + "(");
		if (params != null ) {
			for (int i = 0; i < params.length; i++) {
				print("%s %s", params[i][0], params[i][1]);
				if (i < params.length - 1) {
					print(", ");
				}
			}
		}
		sw.print(") ");
		if (isNative) {
			sw.print("/*-");
		}
		sw.println("{");
		sw.indent();
		if (methodGenerator != null) {
			methodGenerator.generateMethod();			
		}
		sw.outdent();
		sw.print("}");
		if (isNative) {
			sw.print("-*/;");
		}
		sw.println();
		return true;		
	}
	
	public SourceWriter sw() {
		return sw;
	}
	
	ClassSourceFileComposerFactory factory() {
		return factory;
	}

	public void print(String format, Object... args) {
		sw.print(String.format(format, args));
	}

	public void println(String format, Object... args) {
		sw.println(String.format(format, args));
	}
	
	public void addVariable(String modifiers, String type, String name) {
		sw.println(String.format("%s %s %s;", modifiers, type, name));
	}

	public void addVariable(String modifiers, String type, String name, String initValue) {
		sw.println(String.format("%s %s %s = %s;", modifiers, type, name, initValue));
	}

	public String getPackageName(String typeName) {
        TypeOracle typeOracle = context.getTypeOracle();
        final JClassType classType = typeOracle.findType(typeName);
        if (classType != null) {
        	return classType.getPackage().getName();
        }
        return null;
	}

	public String getClassName(String typeName) {
        TypeOracle typeOracle = context.getTypeOracle();
        final JClassType classType = typeOracle.findType(typeName);
        if (classType != null) {
        	return classType.getName();
        }
        return null;
	}
	
	public JClassType getClassType(String typeName) {
        TypeOracle typeOracle = context.getTypeOracle();
        return typeOracle.findType(typeName);
	}
	
	public String getSQLiteType(JType returnType) {
		String sqliteType = null;
		JPrimitiveType primitiveReturnType = returnType.isPrimitive();
		if (primitiveReturnType != null) {
			if (primitiveReturnType == JPrimitiveType.INT)
			{
				sqliteType = "INTEGER";
			}
			else if (primitiveReturnType == JPrimitiveType.BOOLEAN)
			{
				sqliteType = "BOOL"; 
			}
			else {
				sqliteType = primitiveReturnType.getSimpleSourceName();
			}
		}
		else
		{
			sqliteType = "TEXT";
		}
		return sqliteType;
	}
	
	public void inspectType(String typeName, List<JMethod> getters, List<JMethod> hasManyRels, List<JMethod> revHasManyRels) {
		JClassType classType = getClassType(typeName);
		for (JMethod method : classType.getOverridableMethods()) {
//			if (!method.isAbstract()) {
//				continue;
//			}
			String methodName = method.getName();
			if (methodName.startsWith("get")) {
				JType returnType = method.getReturnType();					
				if (returnType.isPrimitive() != null || returnType.getSimpleSourceName().equals("String")) {
					getters.add(method);
					continue;
				}
				if (returnType.getSimpleSourceName().startsWith("Collection")) {
					hasManyRels.add(method);
					continue;
				}
				
				JClassType returnClassType = returnType.isClass();
				if (returnClassType != null) {
					JClassType[] interfaces = returnClassType.getImplementedInterfaces();
					boolean interfaceFound = false;
					for (JClassType impInterface : interfaces) {
						if (impInterface.getSimpleSourceName().equals("Persistable")) {
							revHasManyRels.add(method);
							interfaceFound = true;
							break;
						}
					}
					if (interfaceFound) {
						continue;
					}
				}

				// TODO: error condition, don't know how to gen with the return type.
			}
			else {
				// TODO: check if method is a setter. ignore if so, error if not.
			}
				
		}
	}
	
	public String getGenericTypeShortName(String collectionTypeName) {
		int beginIndex = collectionTypeName.indexOf('<') + 1;
		int endIndex = collectionTypeName.indexOf('>');
		String genericTypeName = collectionTypeName.substring(beginIndex, endIndex);
		beginIndex = genericTypeName.lastIndexOf('.');
		if (beginIndex >= 0) {
			return genericTypeName.substring(beginIndex + 1);
		}
		else {
			return genericTypeName;
		}		
	}
}
