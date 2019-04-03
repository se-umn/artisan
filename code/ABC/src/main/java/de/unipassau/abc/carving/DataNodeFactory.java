package de.unipassau.abc.carving;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.utils.JimpleUtils;

public class DataNodeFactory {

    private static final Logger logger = LoggerFactory.getLogger(DataNodeFactory.class);
    // TODO Possibly build a cache ?

    // TODO Defense against NPE ?
    public static DataNode get(String formalParameter, String actualParameterAsString) {
        DataNode node = null;

        if (actualParameterAsString == null) {
            node = NullNodeFactory.get(formalParameter);
        } else if (JimpleUtils.isPrimitive(formalParameter)) {
            node = PrimitiveNodeFactory.get(formalParameter, actualParameterAsString);
        } else if (JimpleUtils.isString(formalParameter)) {
            /*
             * We need to explicitly use java.lang.String here and not the
             * formal parameter, since that might be a super type (i.e.,
             * java.lang.Object)
             */
            node = PrimitiveNodeFactory.get("java.lang.String", actualParameterAsString);
        } else if (JimpleUtils.isStringContent(actualParameterAsString)) {
            /*
             * Note that checking about String content requires a not null
             * string. We need to explicitly use java.lang.String here and not
             * the formal parameter, since that might be a super type (i.e.,
             * java.lang.Object)
             */
            node = PrimitiveNodeFactory.get("java.lang.String", actualParameterAsString);
        } else if (actualParameterAsString.split("@").length == 2) {
            // actualParameterAsString can be null if its not specified... for
            // example for strings ?
            if (JimpleUtils.isNull(actualParameterAsString)) {
                // TODO Remove the log entry...
                if (actualParameterAsString == null) {
                    logger.warn("Object instance is null for " + formalParameter);
                }
                node = NullNodeFactory.get(formalParameter);
            } else {
                node = ObjectInstanceFactory.get(actualParameterAsString);
            }
        } else {
            throw new RuntimeException("Cannot create a DataNote from " + actualParameterAsString
                    + " with formal parameter " + formalParameter);
        }
        return node;
    }

}
