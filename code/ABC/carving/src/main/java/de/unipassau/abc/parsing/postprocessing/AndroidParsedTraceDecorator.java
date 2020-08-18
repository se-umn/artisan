package de.unipassau.abc.parsing.postprocessing;

import de.unipassau.abc.parsing.ParsedTrace;

public class AndroidParsedTraceDecorator implements ParsedTraceDecorator {

	@Override
	public ParsedTrace decorate(ParsedTrace parsedTrace) {
		// Go over the parsed trace and replace each method invocation with the android
		// versions of it
		// TODO 
		return parsedTrace;
	}

}
