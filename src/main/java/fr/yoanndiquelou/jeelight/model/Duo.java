package fr.yoanndiquelou.jeelight.model;

public class Duo<K,V> {
	/** K value. */
	private K mK;
	/** V value. */
	private V mV;
	
	/**
	 * Duo constructor.
	 * 
	 * @param k k value
	 * @param v v value
	 */
	public Duo(K k, V v) {
		mK = k;
		mV = v;
	}
	
	/**
	 * Get k value.
	 * @return k value
	 */
	public K getK() {
		return mK;
	}
	
	/**
	 * Set K parameter
	 * @param k k parameter
	 * @return Duo instance
	 */
	public Duo<K,V> setK(K k) {
		mK = k;
		return this;
	}
	
	/**
	 * Get v value
	 * @return v value
	 */
	public V getV() {
		return mV;
	}
	
	/**
	 * Set V parameter.
	 * @param v v parameter
	 * @return duo instance
	 */
	public Duo<K,V> setV(V v) {
		mV = v;
		return this;
	}
}
