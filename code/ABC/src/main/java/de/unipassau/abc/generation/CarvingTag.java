package de.unipassau.abc.generation;

import soot.tagkit.AttributeValueException;
import soot.tagkit.Tag;

public class CarvingTag implements Tag{

		private final String name;
		
		public CarvingTag(String name) {
			this.name = "carving:" + name;
		}
		
		@Override
		public String getName() {
			return name;
		}

		@Override
		public byte[] getValue() throws AttributeValueException {
			return null;
	}
}
