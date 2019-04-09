package de.unipassau.abc.carving;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.unipassau.abc.utils.JimpleUtils;
import soot.Scene;
import soot.SootClass;

public class MethodInvocationMatcher {

    static class NoMatchMethodInvocationMatcher extends MethodInvocationMatcher {

        protected NoMatchMethodInvocationMatcher() {
            super();
        }

        @Override
        public boolean matches(MethodInvocation methodInvocation) {
            return false;
        }
    }

    static class AlwaysMatchMethodInvocationMatcher extends MethodInvocationMatcher {

        protected AlwaysMatchMethodInvocationMatcher() {
            super();
        }

        @Override
        public boolean matches(MethodInvocation methodInvocation) {
            return true;
        }
    }

    private final static Logger logger = LoggerFactory.getLogger(MethodInvocationMatcher.class);
    // Jimple methods: <org.employee.Validation: int
    // numberValidation(java.lang.String,org.employee.DummyObjectToPassAsParameter)>
    public final static Pattern jimpleMethodInvocationPattern = Pattern.compile("<" // Opening
            // Tag
            + "[a-zA_Z_][\\.\\w]*(\\[\\])?" // accept arrays as owners
            + ":\\s" //
            + "[a-zA_Z_][\\.\\w]*(\\[\\])?" // accept arrays as return type
            + "\\s"//
            + "([\\.\\w][\\.\\w]*|<init>|<clinit>)" // Method name or
                                                    // constructor or static
                                                    // initializer
            + "\\(([a-zA_Z_][\\.\\w]*(\\[\\])?)?(,[a-zA_Z_][\\.\\w]*(\\[\\])?)*\\)" // Parameter
            // list -> Does not match ARRAY !
            + ">"); // Closing Tag

    // Package and class name, package class and name for return type,
    // methodName, ( list of (package class and name for parameter)>
    // <[*.*] [*.*] [*]

    private /* final */ Pattern classPattern;
    private final Pattern returnPattern;
    private final Pattern methodPattern;
    private final Pattern[] parameterPatterns;
    //
    private int invocationID;
    //
    private final Pattern instancePattern;

    public static MethodInvocationMatcher byPackage(String string) {
        return byClass(string + ".*");
    }

    public static MethodInvocationMatcher byClass(String string) {
        return new MethodInvocationMatcher(string, null, null, null, -1, null);
    }

    public static MethodInvocationMatcher byReturnType(String string) {
        return new MethodInvocationMatcher(".*", string, null, null, -1, null);
    }

    public static MethodInvocationMatcher byMethod(String string) {
        // TODO Validate this is a well formed jimple method
        return fromJimpleMethod(string);
    }

    public static MethodInvocationMatcher byMethodLiteral(String jimpleMethod) {

        // Build the array for parameters
        String[] _params = JimpleUtils.getParameterList(jimpleMethod);
        Pattern[] params = new Pattern[_params.length];
        for (int i = 0; i < params.length; i++) {
            params[i] = Pattern.compile(Pattern.quote(_params[i]));
        }

        return new MethodInvocationMatcher(//
                Pattern.compile(Pattern.quote(JimpleUtils.getClassNameForMethod(jimpleMethod))), //
                Pattern.compile(Pattern.quote(JimpleUtils.getReturnType(jimpleMethod))), //
                Pattern.compile(Pattern.quote(JimpleUtils.getMethodName(jimpleMethod))), //
                params);
    }

    public static MethodInvocationMatcher byInstance(ObjectInstance objectInstance) {
        return new MethodInvocationMatcher(objectInstance);
    }

    // Testing mostly
    public static MethodInvocationMatcher fromJimpleMethod(String jimpleMethod) {
        return new MethodInvocationMatcher(//
                JimpleUtils.getClassNameForMethod(jimpleMethod), //
                JimpleUtils.getReturnType(jimpleMethod), //
                JimpleUtils.getMethodName(jimpleMethod), //
                JimpleUtils.getParameterList(jimpleMethod));
    }

    public static MethodInvocationMatcher fromMethodInvocation(MethodInvocation methodInvocation) {
        String jimpleMethod = methodInvocation.getMethodSignature();
        return new MethodInvocationMatcher(//
                JimpleUtils.getClassNameForMethod(jimpleMethod), //
                JimpleUtils.getReturnType(jimpleMethod), //
                JimpleUtils.getMethodName(jimpleMethod), //
                JimpleUtils.getParameterList(jimpleMethod), //
                methodInvocation.getInvocationCount(), null);
    }

    // This is only for the SubClasses
    protected MethodInvocationMatcher() {
        classPattern = null;
        returnPattern = null;
        methodPattern = null;
        parameterPatterns = null;
        invocationID = -1;
        instancePattern = null;
    }

    // Avoid this to be called from outside
    protected MethodInvocationMatcher(String classRegEx, String returnRegEx, String methodNameRegEx,
            String[] parameterRegExs, int invocationID, String objectId) {
        classPattern = Pattern.compile(classRegEx);
        returnPattern = returnRegEx != null ? Pattern.compile(returnRegEx) : null;
        methodPattern = methodNameRegEx != null ? Pattern.compile(methodNameRegEx) : null;
        //
        if (parameterRegExs != null && parameterRegExs.length > 0) {
            List<Pattern> _parameterPatterns = new ArrayList<>();
            for (String parameterRegEx : parameterRegExs) {
                _parameterPatterns.add(Pattern.compile(parameterRegEx));
            }
            parameterPatterns = _parameterPatterns.toArray(new Pattern[] {});
        } else {
            parameterPatterns = new Pattern[] {};
        }
        this.invocationID = invocationID;
        this.instancePattern = objectId != null ? Pattern.compile(Pattern.quote(objectId)) : null;
    }

    // Avoid this to be called from outside
    protected MethodInvocationMatcher(Pattern classPattern, Pattern returnPattern, Pattern methodPattern,
            Pattern[] parameterPatterns) {
        this.classPattern = classPattern;
        this.returnPattern = returnPattern;
        this.methodPattern = methodPattern;
        this.parameterPatterns = parameterPatterns;
        //
        this.invocationID = -1;
        this.instancePattern = null;
    }

    protected MethodInvocationMatcher(String classRegEx, String returnRegEx, String methodNameRegEx,
            String... parameterRegExs) {
        this(classRegEx, returnRegEx, methodNameRegEx, parameterRegExs, -1, null);
    }
    // Probably better to use a Builder pattern here...

    protected MethodInvocationMatcher(ObjectInstance objectInstance) {
        // Any value, since those will not be used
        this(".*", ".*", ".*", new String[] {}, -1, objectInstance.getObjectId());

    }

    public boolean matches(MethodInvocation methodInvocation) {
        return instancePattern == null ? matchByRegEx(methodInvocation) : matchByInstanceId(methodInvocation);
    }

    private boolean matchByInstanceId(MethodInvocation methodInvocation) {
        if (methodInvocation.isStatic()) {
            return false;
        }

        if (methodInvocation.getOwner() == null) {
            throw new RuntimeException("Invalid Method invocation " + methodInvocation);
        }
        String objectId = methodInvocation.getOwner().getObjectId();

        final Matcher instanceMatcher = instancePattern.matcher(objectId);
        if (!instanceMatcher.find()) {
            // logger.trace(methodInvocation + " with owner " + objectId + "
            // does not match instanceMatcher");
            return false;
        }

        return true;

    }

    private boolean matchByRegEx(MethodInvocation methodInvocation) {
        // Basic chain of command pattern
        String jimpleMethod = methodInvocation.getMethodSignature();
        final Matcher jimpleMatcher = jimpleMethodInvocationPattern.matcher(jimpleMethod);

        if (!jimpleMatcher.find()) {
            // logger.trace(methodInvocation + " does not match jimpleMatcher");
            return false;
        }

        final Matcher classMatcher = classPattern.matcher(JimpleUtils.getClassNameForMethod(jimpleMethod));
        if (!classMatcher.find()) {
            // logger.trace(methodInvocation + " does not match
            // classPatternMatcher " + classPattern);
            return false;
        } else if (returnPattern == null) {
            return true;
        }

        // From this point on, we consider matching exaclty !

        final Matcher returnMatcher = returnPattern.matcher(JimpleUtils.getReturnType(jimpleMethod));
        if (!returnMatcher.find()) {
            // logger.trace(methodInvocation + " does not match
            // returnPatternMatcher " + returnPattern);
            return false;
        } else if (methodPattern == null) {
            return true;
        }

        final Matcher methodMatcher = methodPattern.matcher(JimpleUtils.getMethodName(jimpleMethod));
        if (!methodMatcher.find()) {
            // logger.trace(methodInvocation + " does not match
            // methodPatternMatcher " + methodPattern);
            return false;
        }

        // Matching parametes positionally
        String[] formalParams = JimpleUtils.getParameterList(jimpleMethod);
        if (formalParams.length != parameterPatterns.length) {
            // logger.trace(methodInvocation + " does not match
            // methodParameterCountMatcher");
            return false;
        }

        for (int pos = 0; pos < formalParams.length; pos++) {
            final Matcher parameterMatcher = parameterPatterns[pos].matcher(formalParams[pos]);
            if (!parameterMatcher.find()) {
                // logger.trace(methodInvocation + " does not match
                // parameterCountMatcher for " + formalParams[pos]);
                return false;
            }
        }

        if (invocationID != -1) {
            if (methodInvocation.getInvocationCount() != this.invocationID) {
                // logger.trace(methodInvocation + " does not match
                // invocationCount for " + this.invocationID);
                return false;
            }
        }
        return true;
    }

    // Return a Matcher that do not match anything, this is mostly to avoid
    // having around null objects
    public static MethodInvocationMatcher noMatch() {
        return new NoMatchMethodInvocationMatcher();
    }

    public static MethodInvocationMatcher byMethodInvocation(String regEx) {
        MethodInvocation methodInvocation = new MethodInvocation(regEx.split("_")[0],
                Integer.parseInt(regEx.split("_")[1]));
        return fromMethodInvocation(methodInvocation);
    }

    public static MethodInvocationMatcher byClassLiteral(String regEx) {
        MethodInvocationMatcher literalClass = new MethodInvocationMatcher();
        literalClass.classPattern = Pattern.compile(Pattern.quote(regEx) + "$");
        return literalClass;
    }

    public static MethodInvocationMatcher alwaysMatch() {
        return new AlwaysMatchMethodInvocationMatcher();
    }

    // Parameters must be matched one by one ?!
    public static MethodInvocationMatcher byMethodName(String methodNameRegex) {
        // Parameters regex, one for each parameter ?
        return new ByNameOnlyMethodInvocationMatcher(methodNameRegex);

    }

    static class LibCallMethodInvocationMatcher extends MethodInvocationMatcher {

        protected LibCallMethodInvocationMatcher() {
            super();
        }

        @Override
        public boolean matches(MethodInvocation methodInvocation) {
            return methodInvocation.isLibraryCall();
        }
    }

    public static MethodInvocationMatcher libCall() {
        return new LibCallMethodInvocationMatcher();
    }

    public static Set<MethodInvocation> getMethodInvocationsMatchedBy(MethodInvocationMatcher filter,
            Collection<MethodInvocation> methodInvocationsToBeFiltered) {

        Set<MethodInvocation> matching = new HashSet<>();
        for (MethodInvocation methodInvocation : methodInvocationsToBeFiltered) {
            if (filter.matches(methodInvocation)) {
                matching.add(methodInvocation);
            }
        }
        return matching;
    }
    
    /**
     * Remove from the collection the matching elements.
     * 
     * @param filter
     * @param methodInvocationsToBeFiltered
     */
    public static void filterByInPlace(MethodInvocationMatcher filter,
            Collection<MethodInvocation> methodInvocationsToBeFiltered) {

        for (Iterator<MethodInvocation> iterator = methodInvocationsToBeFiltered.iterator(); iterator.hasNext();) {
            MethodInvocation methodInvocation = iterator.next();
            if (filter.matches(methodInvocation)) {
                iterator.remove();
            }
        }
    }

    static class CLInitMethodInvocationMatcher extends MethodInvocationMatcher {

        protected CLInitMethodInvocationMatcher() {
            super();
        }

        @Override
        public boolean matches(MethodInvocation methodInvocation) {
            // TODO I do not really trust this...
            // return methodInvocation.isStatic() &&
            // methodInvocation.isConstructor();
            return JimpleUtils.isClassConstructor(methodInvocation.getMethodSignature());
        }
    }

    public static MethodInvocationMatcher clinit() {
        return new CLInitMethodInvocationMatcher();
    }

    static class StaticCall extends MethodInvocationMatcher {

        protected StaticCall() {
            super();
        }

        @Override
        public boolean matches(MethodInvocation methodInvocation) {
            return methodInvocation.isStatic();
        }
    }

    public static MethodInvocationMatcher staticCall() {
        return new StaticCall();
    }

    /**
     * Return all the invocations before this one
     * 
     * @param context
     * @return
     */
    public static MethodInvocationMatcher before(final MethodInvocation context) {
        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
                return methodInvocation.getInvocationCount() < context.getInvocationCount();
            }
        };
    }

    public static MethodInvocationMatcher after(final MethodInvocation context) {
        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
                return methodInvocation.getInvocationCount() > context.getInvocationCount();
            }
        };
    }

    public static MethodInvocationMatcher asParameter(final ObjectInstance objectInstanceToCarve) {
        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
                return methodInvocation.getActualParameterInstances().contains(objectInstanceToCarve);
            }
        };
    }

    /*
     * The opposite of libraryCall
     */
    public static MethodInvocationMatcher userAppCalls() {

        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
                return !methodInvocation.isLibraryCall();
            }
        };
    }

    public static MethodInvocationMatcher not(final MethodInvocationMatcher matcher) {
        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
                return !matcher.matches(methodInvocation);
            }
        };
    }
    public static MethodInvocationMatcher or(final MethodInvocationMatcher first, final MethodInvocationMatcher second,
            final MethodInvocationMatcher... others) {
        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
                // Simulate a shortcut evaluation of "AND"
                List<MethodInvocationMatcher> all = new ArrayList<>();
                all.add(first);
                all.add(second);
                all.addAll(Arrays.asList(others));
                // As soon as one matcher matches we return the match
                for (MethodInvocationMatcher matcher : all) {
                    if (matcher.matches(methodInvocation)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
    
    public static MethodInvocationMatcher and(final MethodInvocationMatcher first, final MethodInvocationMatcher second,
            final MethodInvocationMatcher... others) {

        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
                // Simulate a shortcut evaluation of "AND"
                List<MethodInvocationMatcher> all = new ArrayList<>();
                all.add(first);
                all.add(second);
                all.addAll(Arrays.asList(others));
                for (MethodInvocationMatcher matcher : all) {
                    if (!matcher.matches(methodInvocation)) {
                        return false;
                    }
                }
                return true;
            }
        };
    }

    // Null objectInstance do not mathc
    public static MethodInvocationMatcher byOwner(final ObjectInstance objectInstance) {
        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
                return objectInstance != null && objectInstance.equals(methodInvocation.getOwner());
            }
        };
    }

    public static MethodInvocationMatcher byOwnerAndAliases(final ObjectInstance objectInstance,
            final Set<ObjectInstance> aliasesOf) {
        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
                return objectInstance != null && (objectInstance.equals(methodInvocation.getOwner())
                        || aliasesOf.contains(methodInvocation.getOwner()));
            }
        };
    }

    public static MethodInvocationMatcher isSynthetic() {
        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
                return methodInvocation.isSynthetic();
            }
        };
    }

    /*
     * This matches private method invocations
     */
    public static MethodInvocationMatcher visibilityPrivate() {
        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
//                if( methodInvocation.isPrivate() ){
//                    System.out.println(
//                            "MethodInvocationMatcher.visibilityPrivate() " + methodInvocation + " is private ");
//                }
                return methodInvocation.isPrivate();
            }
        };
    }

    /*
     * Would this call be visible from the given package ?
     */
    public static MethodInvocationMatcher isVisibleFromPackage(final String packageName) {
        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
                return methodInvocation.isPublic() || JimpleUtils.getPackage(methodInvocation.getMethodSignature()).equals(packageName);
            }
        };
    }

    public static MethodInvocationMatcher lambda() {
        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
                return JimpleUtils.getClassNameForMethod(methodInvocation.getMethodSignature()).contains("$$Lambda$");
            }
        };
    }

    public static MethodInvocationMatcher isRootCall(final CallGraph callGraph) {
        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
                return callGraph.getOrderedSubsumingMethodInvocationsFor(methodInvocation).isEmpty();
            }
        };
    }

    public static MethodInvocationMatcher isActivityLifeCycle() {
        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
                return methodInvocation.isAndroidActivityCallback();
            }
        };
    }
    
    public static MethodInvocationMatcher isFragmentLifeCycle() {
        return new MethodInvocationMatcher() {
            @Override
            public boolean matches(MethodInvocation methodInvocation) {
                return methodInvocation.isAndroidFragmentCallback();
            }
        };
    }


}
