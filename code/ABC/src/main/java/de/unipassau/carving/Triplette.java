package de.unipassau.carving;

import java.io.Serializable;

public class Triplette<S1, S2, S3> implements Serializable, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -382654258679387185L;

	private S1 first;
	private S2 second;
	private S3 third;

	public Triplette(S1 first, S2 second, S3 third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public void setFirst(S1 first) {
		this.first = first;
	}

	public void setSecond(S2 second) {
		this.second = second;
	}

	public void setTriplette(S1 no1, S2 no2, S3 no3) {
		first = no1;
		second = no2;
		third = no3;
	}

	public S1 getFirst() {
		return first;
	}

	public S2 getSecond() {
		return second;
	}

	public S3 getThird() {
		return third;
	}

	public String toString() {
		return "Triplette " + first + "," + second + "," + third;
	}

	public void setThird(S3 third) {
		this.third = third;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((first == null) ? 0 : first.hashCode());
		result = prime * result + ((second == null) ? 0 : second.hashCode());
		result = prime * result + ((third == null) ? 0 : third.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Triplette other = (Triplette) obj;
		if (first == null) {
			if (other.first != null)
				return false;
		} else if (!first.equals(other.first))
			return false;
		if (second == null) {
			if (other.second != null)
				return false;
		} else if (!second.equals(other.second))
			return false;
		if (third == null) {
			if (other.third != null)
				return false;
		} else if (!third.equals(other.third))
			return false;
		return true;
	}

}
