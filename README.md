gwt-html5-persistence
===============

gwt-html5-persistence is an GWT wrapper of the Javascript object-relational mapper library [persistence.js](http://github.com/zefhemel/persistencejs). It provides client-side object persistence capability to GWT applications, a feature similar to what Hibernate provides for GWT applications on the server-side.

Schema definition
-----------------

The schema is defined by declaring an interface for each entity. The interface needs to extend the `Persistable` marker interface.

	public interface Category extends Persistable {
		public String getName();				// Property
		public void setName(String name);
		public Collection<Task> getTasks();		// One to many relationship
	}
 
Each getter method defines a property on the entity. Setter method is optional. If a getter method returns a `Collection<T extends Persistable>` type, that getter method defines a "one to many" or "many to many" relationship. The actual relationship type is determined by the definition on the other entity, `Task` in the example.

	public interface Task extends Persistable {
		public String getName();
		public void setName(String name);
		public String getDescription();
		public void setDescription(String description);
		public boolean getDone();	
		public void setDone(boolean done);
		public Category getCategory();				//many to one relationship
		public void setCategory(Category category);
		public Collection<Tag> getTags();			//many to many relationship	
	}

If the return type of a getter method extends the `Persistable` marker interface, then the getter method defines an inverse "one to many" relationship.

	public interface Tag extends Persistable {
		public String getName();
		public void setName(String name);	
		public Collection<Task> getTasks();			// Many to many relationship
	}

If the getter methods on both ends of the relationship return a `Collection<T extends Persistable>`, that is a "many to many" relationship.

Objct creation and persistence
------------------------------

The entity is created with the `GWT.create` call. For example, `GWT.create(Task.class)` creates an entity of `Task`. The type of the created entity is `Entity<Task>`.
To create an object of the entity, use the `Entity<T>.newInstance` method. The return type of the `newInstance` method is `T`. For example, `Entity<Category>.newInstance` returns an object of type `Category`. 
	
	final Entity<Task> taskEntity = GWT.create(Task.class);
	final Entity<Tag> tagEntity = GWT.create(Tag.class);
	final Entity<Category> categoryEntity = GWT.create(Category.class);
	
	Persistence.schemaSync(new Callback() {
		public void onSuccess() {
			final Category c = categoryEntity.newInstance();
			c.setName("Main");
			final Tag tag = tagEntity.newInstance();
			tag.setName("Urgent");
			for (int i = 0; i < 5; i++) {
				Task t = taskEntity.newInstance();
				t.setName("Task" + Integer.toString(i));
				t.setDescription("Task No #" + Integer.toString(i));
				t.setDone(i % 2 == 0);
				t.setCategory(c);
				t.getTags().add(tag);
			}
		
			Persistence.flush(new Callback() {
				public void onSuccess() {
					Collection<Task> allTasks = c.getTasks().filter("Done", "==", true);
					allTasks.list(new CollectionCallback<Task>() {
						public void onSuccess(Task[] results) {
							for (Task task : results) {
								RootPanel.get("taskNameContainer").add(new Label(task.getName()));
							}
						}
					});					
					Persistence.reset();
				}					
			});
		}
	});

How to use
----------

1. Import gwt-html5-persistence project into Eclipse.

* Copy [persistence.js](http://github.com/zefhemel/persistencejs) into folder `gwt-html5-persistence/src/com/google/code/gwt/html5/database/persistence/public`. Create the folder if it does not exist.

* In Eclipse, export gwt-html5-persistence as a JAR file.

* Create your GWT project in Eclipse.

* Add the gwt-html5-persistence JAR file as a library to your project.

* Update the project configure file to include the following:

		<script src="http://code.google.com/apis/gears/gears_init.js" />  <!-- Include this line only if you use Google Gears database.-->
		<inherits name='com.google.code.gwt.html5.database.persistence.gwt_html5_persistence' />
  
* Now your GWT project is ready to persist objects to the brower database!
 