package com.google.code.gwt.html5.database.persistence.rebind;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.ext.Generator;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.typeinfo.JMethod;

public class GWTPersistenceGenerator extends Generator {

	@Override
	public String generate(TreeLogger logger, GeneratorContext context,
			final String typeName) throws UnableToCompleteException {

		final GenUtils utils = new GenUtils(logger, context);
		final String requestedClassName = utils.getClassName(typeName);
		final String generatedClassName = requestedClassName + "EntityImpl";
		final List<JMethod> getters = new ArrayList<JMethod>();
		final List<JMethod> hasManyRels = new ArrayList<JMethod>();
		final List<JMethod> revHasManyRels = new ArrayList<JMethod>();
		
		utils.inspectType(typeName, getters, hasManyRels, revHasManyRels);
		
		return utils.generateClass(typeName, "EntityImpl",
				new EntityGenerator(utils, requestedClassName, generatedClassName, getters, hasManyRels, revHasManyRels));
	}
}
