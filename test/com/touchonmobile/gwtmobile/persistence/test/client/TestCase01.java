package com.touchonmobile.gwtmobile.persistence.test.client;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.junit.client.GWTTestCase;
import com.touchonmobile.gwtmobile.persistence.client.Callback;
import com.touchonmobile.gwtmobile.persistence.client.Collection;
import com.touchonmobile.gwtmobile.persistence.client.CollectionCallback;
import com.touchonmobile.gwtmobile.persistence.client.Entity;
import com.touchonmobile.gwtmobile.persistence.client.Persistence;
import com.touchonmobile.gwtmobile.persistence.test.domain.Category;
import com.touchonmobile.gwtmobile.persistence.test.domain.Tag;
import com.touchonmobile.gwtmobile.persistence.test.domain.Task;

public class TestCase01 extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.touchonmobile.gwtmobile.persistence.test.gwtmobile_persistence_test";
	}

	public void test01() {
		Persistence.connect("MyDB", "My DB", 5 * 1024 * 1024);
		Persistence.setAutoAdd(true);
		
		final Entity<Task> taskEntity = GWT.create(Task.class);
		final Entity<Tag> tagEntity = GWT.create(Tag.class);
		final Entity<Category> categoryEntity = GWT.create(Category.class);
		
		Persistence.schemaSync(new Callback() {
			public void onSuccess() 
			{
				final Category c = categoryEntity.newInstance();
				c.setName("Main");
				final Tag tag = tagEntity.newInstance();
				tag.setName("Urgent");
				for (int i = 0; i < 5; i++) {
					Task t = taskEntity.newInstance();
					t.setName("Task" + Integer.toString(i));
					//t.setName(null);
					t.setDescription("Task No #" + Integer.toString(i));
					if (i % 2 == 0) {
						t.setDone(true);
						t.setCompleteDate(new Date());
						t.setPriority(i);
						t.setPercentage((float)i / 10);
						t.setProfit((double)i * 12.34);
						t.setAlphabet((char) ('A' + i));
						t.setJson((JSONObject) JSONParser.parse("{\"symbol\": \"ABC\", \"price\": 96.204659543522}"));
					}
					t.setCategory(c);
					t.getTags().add(tag);
				}
			
				System.out.println("flush");
				Persistence.flush(new Callback() {
					public void onSuccess() {
						try {
							Collection<Task> allTasks = taskEntity.all();
							allTasks.list(new CollectionCallback<Task>() {
								public void onSuccess(Task[] results) {
									for (Task task : results) {
										String date = task.getCompleteDate() == null ? "null" : task.getCompleteDate().toString();
										System.out.println(date);
									}
								}
							});
						}
						finally {
							Persistence.reset();
							finishTest();
						}
					}
				});				
			}
		});
		
		delayTestFinish(10000);
	}

	public void test02() {
		assertEquals('b', 'b');
	}
}
