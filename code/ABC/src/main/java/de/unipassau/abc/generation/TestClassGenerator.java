package de.unipassau.abc.generation;

import java.util.Collection;

import de.unipassau.abc.carving.MethodInvocation;
import soot.SootClass;

public interface TestClassGenerator {

	public SootClass getTestClassFor(MethodInvocation mut);

	public Collection<SootClass> getTestClasses();
}
