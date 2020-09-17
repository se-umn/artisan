package de.unipassau.abc.parsing.postprocessing;

import de.unipassau.abc.data.AndroidMethodInvocation;
import de.unipassau.abc.data.CallGraph;
import de.unipassau.abc.data.DataDependencyGraph;
import de.unipassau.abc.data.ExecutionFlowGraph;
import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.data.MethodInvocation;
import de.unipassau.abc.data.ObjectInstance;
import de.unipassau.abc.parsing.ParsedTrace;
import java.util.Set;
import java.util.function.Predicate;

public class AndroidParsedTraceDecorator implements ParsedTraceDecorator {

  @Override
  public ParsedTrace decorate(ParsedTrace parsedTrace) {
    // Go over the parsed trace and replace each method invocation with the android
    // versions of it
    parsedTrace.getParsedTrace().forEach((threadName, graphs) -> {
      ExecutionFlowGraph executionFlowGraph = graphs.getFirst();
      CallGraph callGraph = graphs.getThird();
      DataDependencyGraph dataDependencyGraph = graphs.getSecond();

      // First follow constructor call chains and check if somewhere an android super
      // constructor is called
      executionFlowGraph
          .getOrderedMethodInvocations().stream().filter(MethodInvocation::isConstructor)
          .forEach(methodInvocation -> {
            if (isAndroidActivity(methodInvocation, callGraph)) {
              dataDependencyGraph.getOwnerFor(methodInvocation).setAndroidActivity(true);
            } else if (isAndroidFragment(methodInvocation, callGraph)) {
              dataDependencyGraph.getOwnerFor(methodInvocation).setAndroidFragment(true);
            }
          });

      // Replace original method invocation instances with android ones in the graphs
      executionFlowGraph.getOrderedMethodInvocations().stream().filter(
          methodInvocation -> {
            ObjectInstance owner = dataDependencyGraph
                .getOwnerFor(methodInvocation);
            // Basically, if a method has an android package or is part of an activity/fragment and
            // is a constructor/lifecycle callback, then it is considered android related. But does
            // this cover everything?
            // TODO what about static methods?
            return JimpleUtils.isAndroidMethod(methodInvocation.getMethodSignature())
                || (owner.isAndroidActivity() && JimpleUtils
                .isActivityLifecycle(methodInvocation.getMethodSignature()))
                || (owner.isAndroidActivity() && methodInvocation.isConstructor())
                || (owner.isAndroidFragment() && JimpleUtils
                .isFragmentLifecycle(methodInvocation.getMethodSignature()))
                || (owner.isAndroidFragment() && methodInvocation
                .isConstructor());
          }).forEach(originalMethodInvocation -> {
        AndroidMethodInvocation androidMethodInvocation = new AndroidMethodInvocation(
            originalMethodInvocation);
        executionFlowGraph.replaceMethodInvocation(originalMethodInvocation, androidMethodInvocation);
        callGraph.replaceMethodInvocation(originalMethodInvocation, androidMethodInvocation);
        dataDependencyGraph
            .replaceMethodInvocation(originalMethodInvocation, androidMethodInvocation);
      });
    });

    return parsedTrace;
  }

  private boolean isAndroidActivity(MethodInvocation methodInvocation, CallGraph callGraph) {
    return isAndroidElement(methodInvocation, callGraph,
        className -> className.endsWith("Activity"));
  }

  private boolean isAndroidFragment(MethodInvocation methodInvocation, CallGraph callGraph) {
    return isAndroidElement(methodInvocation, callGraph,
        className -> className.endsWith("Fragment"));
  }

  /*
   * Lookup the sub method calls of method and check if there is an android related constructor call
   */
  private boolean isAndroidElement(MethodInvocation methodInvocation, CallGraph callGraph,
      Predicate<String> filterPredicate) {
    Set<MethodInvocation> subMethods = callGraph.getMethodInvocationsSubsumedBy(methodInvocation);
    if (methodInvocation.isConstructor() && JimpleUtils
        .isAndroidMethod(methodInvocation.getMethodSignature())
        && filterPredicate
        .test(JimpleUtils.getClassNameForMethod(methodInvocation.getMethodSignature()))) {
      return true;
    } else {
      boolean isAndroid = false;
      for (MethodInvocation subMethod : subMethods) {
        isAndroid |= isAndroidActivity(subMethod, callGraph);
      }
      return isAndroid;
    }
  }

}
