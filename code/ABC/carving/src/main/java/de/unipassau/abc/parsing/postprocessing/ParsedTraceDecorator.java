package de.unipassau.abc.parsing.postprocessing;

import de.unipassau.abc.parsing.ParsedTrace;

public interface ParsedTraceDecorator {

	public ParsedTrace decorate(ParsedTrace parsedTrace);
}
