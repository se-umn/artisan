package de.unipassau.abc.generation.utils;

import org.apache.commons.lang.NotImplementedException;

import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.PrimitiveValue;

public class TypeUtils {

    /**
     * There are some weird cases here. For example Arrays.asList() returns an
     * "java.util.Arrays$ArrayList" which is not a "real" type. So we cannot
     * directly instantiate that. We need somehow to resolve this to some valid
     * class (i.e. LIST)... We can look at aliases ?
     * 
     * 
     * @param dataNode
     * @param aliasToClass
     * @return
     */
    public static String getActualTypeFor(DataNode dataNode) {
        // TODO DO we need to extend DataNode with formal and actual type information?
        if (dataNode instanceof PrimitiveValue) {
            return ((PrimitiveValue) dataNode).getType();
        } else if (dataNode instanceof ObjectInstance) {
            String actualType = ((ObjectInstance) dataNode).getType();

            // TODO Patched
            if (actualType.equals("java.util.Arrays$ArrayList")) {
                System.out.println("TypeUtils.getActualTypeFor() PATCHED java.util.Arrays$ArrayList");
                actualType = "java.util.List";
            } else if (actualType.equals("java.util.TreeMap$KeySet")) {
                System.out.println("TypeUtils.getActualTypeFor() PATCHED java.util.TreeMap$KeySet");
                actualType = "java.util.Set";
            }

            if (actualType.contains("$")) {
                throw new NotImplementedException("We cannot handle private inner classes, like " + actualType);
            }
//			
            //
            // If that's an inner class we might not be able to instantiate
            // it...
//			if (formalType.contains("$")) {
//				formalType = formalType.replaceAll("\\$", ".");
//			}
            //
            // private static class ArrayList<E> extends AbstractList<E>
            /*
             * Inner classes are identified by $ replace this to but not always those are
             * visible (here, tho, we do not have such information)
             */
//			return formalType;
            // TODO I am not 100% sure this might be a good strategy...
            return actualType;
        } else {
            // What if there's null ?!
            throw new RuntimeException("Cannot find type for " + dataNode);
        }
    }
}
