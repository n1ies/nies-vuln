package org.n1es.vuln.jndi;

import javax.naming.InitialContext;
import javax.naming.NamingException;

public class JNDILookup {

	public static void main(String[] args) {
		try {
//			System.setProperty("com.sun.jndi.rmi.object.trustURLCodebase", "true");
			Object ret = new InitialContext().lookup("rmi://127.0.0.1:1099/Foo");
			System.out.println("ret: " + ret);
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}
}
