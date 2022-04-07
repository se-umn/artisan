package de.unipassau.abc.generation.utils;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.DataNode;
import de.unipassau.abc.data.EnumConstant;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.data.ObjectInstanceFactory;
import de.unipassau.abc.data.PrimitiveValue;
import edu.emory.mathcs.backport.java.util.Arrays;

public class TypeUtils {

    private static Logger logger = LoggerFactory.getLogger(TypeUtils.class);

    public static String convertType(String actualType) {
        String replaceType;
        if (actualType.equals("java.util.Arrays$ArrayList")) {
            replaceType = "java.util.List";
        } else if (actualType.equals("java.util.TreeMap$KeySet")) {
            replaceType = "java.util.Set";
        } else if (actualType.equals("java.util.RegularEnumSet") || actualType.equals("java.util.JumboEnumSet")) {
            replaceType = "java.util.EnumSet";
        } else if (actualType.equals("android.app.SharedPreferencesImpl")) {
            replaceType = "android.content.SharedPreferences";
        } else if (actualType.equals("java.util.ArrayList$Itr")) {
            replaceType = "java.util.Iterator";
        } else {
            replaceType = null;
        }
        return replaceType;
    }

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

            String replaceType = convertType(actualType);

            // TODO Leave out for the moment this !
            if (actualType.contains("$")) {
                if (dataNode instanceof EnumConstant) {
                    replaceType = actualType.replaceAll("\\$", ".");
                } else {

                    throw new NotImplementedException("We cannot handle private inner classes, like " + actualType);
                }
            }
//			
            if (replaceType != null) {
                return replaceType;
            } else {
                //
                // If that's an inner class we might not be able to instantiate
                // it...
                // if (formalType.contains("$")) {
                // formalType = formalType.replaceAll("\\$", ".");
                // }
                //
                // private static class ArrayList<E> extends AbstractList<E>
                /*
                 * Inner classes are identified by $ replace this to but not always those are
                 * visible (here, tho, we do not have such information)
                 */
                // return formalType;
                // TODO I am not 100% sure this might be a good strategy...
                return actualType;
            }
        } else {
            // What if there's null ?!
            throw new RuntimeException("Cannot find type for " + dataNode);
        }
    }

    /**
     * Look up possible formal types that can be assigned to this dataNode. We
     * usually do this for internal/anonym classes
     * 
     * @param dataNode
     * @param carvedTest
     * @return
     */
    public static String getFormalTypeFor(ObjectInstance objectInstance, DataDependencyGraph dataDependencyGraph) {

        logger.info("Getting formal types for " + objectInstance);
        
        // If the object is annotated with resolved type, choose one
        if( ! objectInstance.getResolvedTypes().isEmpty() ) {
            String resolvedType = objectInstance.getResolvedTypes().iterator().next();
            logger.info("Returning one of the Resolved Types" + resolvedType);
            return resolvedType;
        }
        
        // Collects types for DataNode
        // This requires the knowledge on how types are organized and the select the
        // most precise type above the actual one.
        // We approximate this by looking at the methods that return or use the object.

        
        // Ideally one can also look at who uses it
        Set<String> alternativeTypesFromReturn = dataDependencyGraph.getMethodInvocationsWhichReturn(objectInstance)
                .stream().map(mi -> JimpleUtils.getReturnType(mi.getMethodSignature()))
                .filter(type -> !type.equals(objectInstance.getType())).collect(Collectors.toSet());

        // Extract the Type of the parameter at position
        Set<String> alternativeTypesFromUsage = dataDependencyGraph.getMethodInvocationsWhichUse(objectInstance)
                .stream()
                .map(mi -> JimpleUtils.getParameterList(mi.getMethodSignature())[mi.getActualParameterInstances()
                        .indexOf(objectInstance)])
                .filter(type -> !type.equals(objectInstance.getType())).collect(Collectors.toSet());

        logger.info("Getting formal types for " + objectInstance + ": " + alternativeTypesFromReturn);
        logger.info("Getting formal types for " + objectInstance + ": " + alternativeTypesFromUsage);

        Set<String> possibleAlternativeTypes = new HashSet<String>();
        possibleAlternativeTypes.addAll(alternativeTypesFromReturn);
        possibleAlternativeTypes.addAll(alternativeTypesFromUsage);
        // Type resolution might be needed. For now, just take whatever we find
        for (String possibleSuperType : possibleAlternativeTypes) {

            try {
                ObjectInstance castedInstance = ObjectInstanceFactory
                        .get(possibleSuperType + "@" + objectInstance.getHash());
                return getActualTypeFor(castedInstance);
            } catch (Exception e) {
                // continue
            }
        }

        // As a Last Resort
        if( objectInstance.getType().contains("$")) {
            return objectInstance.getType().replace("$", ".");
        }
        
        
        throw new RuntimeException("Cannot find Formal type for " + objectInstance);
    }

}
