package chapter25;

@FunctionalInterface
public interface CacheLoader<K, V> {

	 V load(K key);

}
