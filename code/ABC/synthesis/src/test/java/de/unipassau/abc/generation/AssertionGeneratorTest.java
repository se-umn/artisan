package de.unipassau.abc.generation;

import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.event.Level;

import de.unipassau.abc.utils.Slf4jSimpleLoggerRule;
import soot.G;
import soot.Scene;
import soot.options.Options;

public class AssertionGeneratorTest {

	@Rule
	public Slf4jSimpleLoggerRule loggerLevelRule = new Slf4jSimpleLoggerRule(Level.TRACE);

	@Test
	public void listHamcrestMatchers() {
		G.reset();
		Options.v().set_allow_phantom_refs(true);
		Options.v().set_whole_program(true);
//		System.getProperties().forEach((k, v) -> System.out.println(k + " - " + v));
		Options.v().set_soot_classpath(System.getProperty("java.class.path"));
		// LIST ALL THE HAMCREST METHODS !
		Scene.v().loadClassAndSupport(Matcher.class.getName()).getMethods().forEach(sm -> System.out.println(sm));
		Scene.v().loadClassAndSupport(Matchers.class.getName()).getMethods().forEach(sm -> System.out.println(sm));
		Scene.v().loadClassAndSupport(MatcherAssert.class.getName()).getMethods().forEach(sm -> System.out.println(sm));

	}
}
