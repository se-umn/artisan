package de.unipassau.abc.carving;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.NoTypePermission;
import com.thoughtworks.xstream.security.NullPermission;
import com.thoughtworks.xstream.security.PrimitiveTypePermission;

public class PrimitiveNodeFactory {

    private static AtomicInteger uniqueId = new AtomicInteger(0);

    public static PrimitiveValue get(String type, String value) {
        return new PrimitiveValue(uniqueId.incrementAndGet(), type, value);
    }

    public static PrimitiveValue createStringNode(String type, String stringContent) {
        if (!type.equals("java.lang.String")) {
            throw new RuntimeException("Cannot create a string node for type " + type);
        }

        // Value null or empty ?!
        // System.out.println("PrimitiveNodeFactory.createStringNode() node
        // value for: " + type + " -- " + value);

        String theValue = null;
        //
        if (stringContent != null) {
            //
            // // Read the value from value
            // XStream xStream = new XStream();
            // // clear out existing permissions and set own ones
            // xStream.addPermission(NoTypePermission.NONE);
            // // xstream.addPermission(AnyTypePermission.ANY);
            // xStream.allowTypeHierarchy(Object.class);
            //
            // // Allow ALL The objects
            // xStream.addPermission(NullPermission.NULL);
            // xStream.addPermission(PrimitiveTypePermission.PRIMITIVES);
            // xStream.allowTypesByWildcard(new String[] { "java.lang.**" });
            // xStream.allowTypesByWildcard(new String[] { "*.**" });
            //
            // try {
            // theValue = (String) xStream
            // .fromXML(new String(Files.readAllBytes(Paths.get(value)),
            // StandardCharsets.UTF_8));
            // } catch (IOException e) {
            // e.printStackTrace();
            // throw new RuntimeException("Cannot create NodeValue for " + type
            // + " -- " + value, e);
            // }
            /*
             * Convert strings back to their format. The catch is that a formal
             * parameter might be a super type of string, i.e., Object, and this
             * breaks the algorithm... So we rely on a convention: any object
             * which does not contains @ in its string representation is a
             * primitive
             */
            stringContent = stringContent.replace("[", "").replace("]", "");
            if (stringContent.isEmpty()) {
                stringContent = "";
            } else {
                String[] bytesAsString = stringContent.split(",");
                byte[] bytes = new byte[bytesAsString.length];
                for (int ii = 0; ii < bytes.length; ii++) {
                    bytes[ii] = new Byte(bytesAsString[ii].trim());
                }
                theValue = new String(bytes);
            }
        }

        PrimitiveValue pv = new PrimitiveValue(uniqueId.incrementAndGet(), type, theValue);
        if (stringContent == null) {
            pv.setRefid(type + "@0");
        }
        return pv;
    }

}
