package co.xarx.trix.util;

import com.google.common.collect.ImmutableList;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ImmutablePage<T> implements Collection<T> {

	private ImmutableList<T> items;
	private Integer index;
	private Integer totalSize;

	public ImmutablePage(List<T> items, Integer index, Integer totalSize) {
		this.items = ImmutableList.copyOf(items == null ? new ArrayList<>() : items);

		if(totalSize < this.items.size())
			throw new IllegalArgumentException("Total size is smaller than items size");

		this.index = index;
		this.totalSize = totalSize;
	}

	public List<T> items() {
		return items;
	}

	public Integer getIndex() {
		return index;
	}

	public Integer totalSize() {
		return totalSize;
	}

	public Integer totalPages() {
		BigDecimal total = BigDecimal.valueOf(totalSize());
		BigDecimal pageSize = BigDecimal.valueOf(size());
		return total.divide(pageSize, RoundingMode.HALF_UP).intValue();
	}

	@Override
	public int size() {
		return items.size();
	}

	@Override
	public boolean isEmpty() {
		return items.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return items.contains(o);
	}

	@Override
	public Iterator<T> iterator() {
		return items.iterator();
	}

	@Override
	public Object[] toArray() {
		return items.toArray();
	}

	@Override
	public <T1> T1[] toArray(T1[] t1s) {
		return items.toArray(t1s);
	}

	@Override
	@Deprecated
	public boolean add(T t) {
		return items.add(t);
	}

	@Override
	@Deprecated
	public boolean remove(Object o) {
		return items.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> collection) {
		return items.containsAll(collection);
	}

	@Override
	@Deprecated
	public boolean addAll(Collection<? extends T> collection) {
		return items.addAll(collection);
	}

	@Override
	@Deprecated
	public boolean removeAll(Collection<?> collection) {
		return items.removeAll(collection);
	}

	@Override
	@Deprecated
	public boolean retainAll(Collection<?> collection) {
		return items.retainAll(collection);
	}

	@Override
	@Deprecated
	public void clear() {
		items.clear();
	}

	@Override
	public boolean equals(Object o) {
		return items.equals(o);
	}

	@Override
	public int hashCode() {
		return items.hashCode();
	}

	@Override
	public Spliterator<T> spliterator() {
		return items.spliterator();
	}

	@Override
	public boolean removeIf(Predicate<? super T> predicate) {
		return items.removeIf(predicate);
	}

	@Override
	public Stream<T> stream() {
		return items.stream();
	}

	@Override
	public Stream<T> parallelStream() {
		return items.parallelStream();
	}

	@Override
	public void forEach(Consumer<? super T> consumer) {
		items.forEach(consumer);
	}
}
