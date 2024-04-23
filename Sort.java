import java.util.Comparator;

/**
 * Class for sorting lists that implement the IndexedUnsortedList interface,
 * using ordering defined by class of objects in list or a Comparator.
 * As written uses Mergesort algorithm.
 *
 * @author CS221, Aris Vetter
 */
public class Sort {
	/**
	 * Returns a new list that implements the IndexedUnsortedList interface.
	 * As configured, uses WrappedDLL. Must be changed if using
	 * your own IUDoubleLinkedList class.
	 * 
	 * @return a new list that implements the IndexedUnsortedList interface
	 */
	private static <T> IndexedUnsortedList<T> newList() {
		return new IUDoubleLinkedList<T>(); // replaced wth my own IUDoubleLinkedList
	}

	/**
	 * Sorts a list that implements the IndexedUnsortedList interface
	 * using compareTo() method defined by class of objects in list.
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param <T>
	 *             The class of elements in the list, must extend Comparable
	 * @param list
	 *             The list to be sorted, implements IndexedUnsortedList interface
	 * @see IndexedUnsortedList
	 */
	public static <T extends Comparable<T>> void sort(IndexedUnsortedList<T> list) {
		mergesort(list);
	}

	/**
	 * Sorts a list that implements the IndexedUnsortedList interface
	 * using given Comparator.
	 * DO NOT MODIFY THIS METHOD
	 * 
	 * @param <T>
	 *             The class of elements in the list
	 * @param list
	 *             The list to be sorted, implements IndexedUnsortedList interface
	 * @param c
	 *             The Comparator used
	 * @see IndexedUnsortedList
	 */
	public static <T> void sort(IndexedUnsortedList<T> list, Comparator<T> c) {
		mergesort(list, c);
	}

	/**
	 * Mergesort algorithm to sort objects in a list
	 * that implements the IndexedUnsortedList interface,
	 * using compareTo() method defined by class of objects in list.
	 * DO NOT MODIFY THIS METHOD SIGNATURE
	 * 
	 * @param <T>
	 *             The class of elements in the list, must extend Comparable
	 * @param list
	 *             The list to be sorted, implements IndexedUnsortedList interface
	 */
	private static <T extends Comparable<T>> void mergesort(IndexedUnsortedList<T> list) {

		// break the list into two parts
		if (list.size() > 1) {
			IndexedUnsortedList<T> leftList = newList();
			IndexedUnsortedList<T> rightList = newList();
			int midPoint = list.size() / 2;

			for (int i = 0; !list.isEmpty(); i++) {
				if (i < midPoint) {
					leftList.add(list.removeFirst());
				} else {
					rightList.add(list.removeFirst());
				}
			}

			// recursively calls mergesort to continue to break down the lists
			mergesort(leftList);
			mergesort(rightList);

			/*
			 * Compares the first element of right list and left list, moves the one that
			 * should be first from its current right/left list to list.
			 */
			while (!leftList.isEmpty() && !rightList.isEmpty()) {
				if (leftList.first().compareTo(rightList.first()) <= 0) { // uses compareTo rather than compare
					list.add(leftList.removeFirst());
				} else {
					list.add(rightList.removeFirst());
				}

			}

			// identifies the final element left in left or right list, adds to list.
			if (leftList.isEmpty()) {
				while (!rightList.isEmpty()) {
					list.add(rightList.removeFirst());
				}
			} else {
				while (!leftList.isEmpty()) {
					list.add(leftList.removeFirst());
				}
			}
		}

	}

	/**
	 * Mergesort algorithm to sort objects in a list
	 * that implements the IndexedUnsortedList interface,
	 * using the given Comparator.
	 * DO NOT MODIFY THIS METHOD SIGNATURE
	 * 
	 * @param <T>
	 *             The class of elements in the list
	 * @param list
	 *             The list to be sorted, implements IndexedUnsortedList interface
	 * @param c
	 *             The Comparator used
	 */
	private static <T> void mergesort(IndexedUnsortedList<T> list, Comparator<T> c) {
		if (list.size() > 1) {
			// break the list into two parts
			IndexedUnsortedList<T> leftList = newList();
			IndexedUnsortedList<T> rightList = newList();
			int midPoint = list.size() / 2;

			for (int i = 0; !list.isEmpty(); i++) {
				if (i < midPoint) {
					leftList.add(list.removeFirst());
				} else {
					rightList.add(list.removeFirst());
				}
			}

			// recursively calls mergesort to continue to break down the lists
			mergesort(leftList, c);
			mergesort(rightList, c);

			/*
			 * Compares the first element of right list and left list, moves the one that
			 * should be first from its current right/left list to list.
			 */
			while (!leftList.isEmpty() && !rightList.isEmpty()) {
				// uses the comparator's compare rather than the list's compare
				if (c.compare(leftList.first(), rightList.first()) <= 0) {
					list.add(leftList.removeFirst());
				} else {
					list.add(rightList.removeFirst());
				}

			}

			// identifies the final element left in left or right list, adds to list.
			if (leftList.isEmpty()) {
				while (!rightList.isEmpty()) {
					list.add(rightList.removeFirst());

				}
			} else {
				while (!leftList.isEmpty()) {
					list.add(leftList.removeFirst());
				}
			}

		}

	}

}
