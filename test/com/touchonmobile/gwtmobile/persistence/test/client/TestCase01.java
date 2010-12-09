package com.touchonmobile.gwtmobile.persistence.test.client;

import com.google.gwt.junit.client.GWTTestCase;

public class TestCase01 extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "com.touchonmobile.gwtmobile.persistence.test.gwtmobile_persistence";
	}

	public void test01() {
		assertEquals('a', 'a');
	}

	public void test02() {
		assertEquals('b', 'b');
	}
}
