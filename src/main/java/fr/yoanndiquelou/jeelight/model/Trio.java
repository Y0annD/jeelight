package fr.yoanndiquelou.jeelight.model;

/**
 * Trio object to associate 3 object.
 * @author y0annD
 *
 * @param <K> 
 * @param <U>
 * @param <V>
 */
public class Trio<K,U,V> {
	/** K object. */
	private K mK;
	/** U object. */
	private U mU;
	/** V object. */
	private V mV;
	
	/**
	 * Trio constructor.
	 * 
	 * @param k
	 * @param u
	 * @param v
	 */
	public Trio(K k, U u, V v) {
		mK = k;
		mU = u;
		mV = v;
	}

	/**
	 * Return K object.
	 * 
	 * @return the K object
	 */
	public K getK() {
		return mK;
	}

	/**
	 * 
	 * Set K object.
	 * @param k the K object to set
	 */
	public void setK(K k) {
		mK = k;
	}

	/**
	 * Return the U object.
	 * @return U object
	 */
	public U getU() {
		return mU;
	}

	/**
	 * Set the U object.
	 * @param u the U object to set
	 */
	public void setU(U u) {
		mU = u;
	}

	/**
	 * Get the v object.
	 * 
	 * @return the V object
	 */
	public V getV() {
		return mV;
	}

	/**
	 * Set the v object.
	 * @param v the V object to set
	 */
	public void setV(V v) {
		mV = v;
	}
}
