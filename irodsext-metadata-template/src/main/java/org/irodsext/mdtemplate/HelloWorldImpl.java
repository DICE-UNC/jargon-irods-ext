package org.irodsext.template;

import org.springframework.stereotype.Component;

public class HelloWorldImpl implements HelloWorld {

	public void printHelloWorld(String msg) {

		System.out.println("Hello : " + msg);
	}

}
