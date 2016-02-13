package co.xarx.trix.util;

import org.springframework.util.Assert;

import java.util.AbstractList;
import java.util.List;

public class ListUtil {

	public static <T> List<List<T>> partition(List<T> list, int size) {
		Assert.notEmpty(list, "List must not be empty");
		Assert.isTrue(size > 0, "Size must be greater than zero");

		return new Partition<>(list, size);
	}

	private static class Partition<T> extends AbstractList<List<T>> {

		final List<T> list;
		final int size;

		Partition(List<T> list, int size) {
			this.list = list;
			this.size = size;
		}

		@Override
		public List<T> get(int index) {
			int listSize = size();
			Assert.isTrue(listSize > 0, "negative size: " + listSize);
			Assert.isTrue(index >= 0, "index " + index + " must not be negative");
			Assert.isTrue(index < listSize, "index " + index + " must be less than size " + listSize);

			int start = index * size;
			int end = Math.min(start + size, list.size());
			return list.subList(start, end);
		}

		@Override
		public int size() {
			return (list.size() + size - 1) / size;
		}

		@Override
		public boolean isEmpty() {
			return list.isEmpty();
		}
	}
}
