package de.unipassau.abc.generation.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import de.unipassau.abc.data.JimpleUtils;
import de.unipassau.abc.generation.data.CarvedTest;

/**
 * Place carved tests that test the same method together TODO Far from being finished
 *
 * @author gambitemp
 */
public class ByClassUnderTest implements TestCaseOrganizer {

  @Override
  public Set<TestClass> organize(CarvedTest... carvedTests) {

    Map<String, Set<CarvedTest>> muts = new HashMap<>();
    for (CarvedTest carvedTest : carvedTests) {
      String className = JimpleUtils
          .getClassNameForMethod(carvedTest.getMethodUnderTest().getMethodSignature());
      muts.putIfAbsent(className, new HashSet<>());
      muts.get(className).add(carvedTest);
    }

    // Now make sure to define proper naming and packaging
    Set<TestClass> testSuite = new HashSet<>();

    for (Entry<String, Set<CarvedTest>> testGroup : muts.entrySet()) {
      String packageName = testGroup.getKey().substring(0, testGroup.getKey().lastIndexOf("."));
      TestClass testCase = new TestClass(packageName,
          testGroup.getKey().substring(testGroup.getKey().lastIndexOf(".") + 1),
          testGroup.getValue());
      testSuite.add(testCase);
    }

    return testSuite;
  }
}
