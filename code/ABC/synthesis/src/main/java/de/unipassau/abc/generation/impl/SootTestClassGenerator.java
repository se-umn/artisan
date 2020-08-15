package de.unipassau.abc.generation.impl;

import java.util.Collection;

import de.unipassau.abc.data.MethodInvocation;
import soot.SootClass;

/**
 * TODO Does this have to be Soot specific? I thinkg Soot must be hidden as an
 * implementation detail
 * 
 * @author gambitemp
 *
 */
public interface SootTestClassGenerator {

	public SootClass getTestClassFor(MethodInvocation mut);

	public Collection<SootClass> getTestClasses();
}
