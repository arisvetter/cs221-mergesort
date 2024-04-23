import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;

/**
 * Creates a doubly linked list implementation of the IndexedUnsortedList
 * interface with a fully functional ListIterator
 * 
 * @author Aris Vetter
 */
public class IUDoubleLinkedList<T> implements IndexedUnsortedList<T> {
    private Node<T> head, tail;
    private int size;
    private int modCount;

    /**
     * Instantiates an empty indexed unsorted double linked list
     */
    public IUDoubleLinkedList() {
        size = 0;
        modCount = 0;
        head = null;
        tail = null;
    }

    @Override
    public void addToFront(T element) {
        // Starts the listIterator at the beginning of the list, immediatly adds element
        ListIterator<T> lit = listIterator();
        lit.add(element);
    }

    @Override
    public void addToRear(T element) {
        // calls to add with the index parameter being the next free indexx
        add(size, element);

    }

    @Override
    public void add(T element) {
        // equivalent to the addToRear method
        add(size, element);
    }

    @Override
    public void addAfter(T element, T target) {
        // loops through every item of the list until the target is found
        ListIterator<T> lit = listIterator();
        boolean found = false;
        while (lit.hasNext() && !found) {
            if (lit.next().equals(target)) {
                found = true;
                lit.add(element);
            }
        }

        // if that element is not in the list
        if (!found) {
            throw new NoSuchElementException();
        }

    }

    @Override
    public void add(int index, T element) {
        // starts the iterator right where the element needs to be inserted
        ListIterator<T> lit = listIterator(index);
        lit.add(element);
    }

    @Override
    public T removeFirst() {
        // must have at least one element
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        ListIterator<T> lit = listIterator();
        T retVal = lit.next();
        lit.remove();
        return retVal;

    }

    @Override
    public T removeLast() {
        // must have at least one element
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        // calls to remove at the index of the last element
        return remove(size - 1);
    }

    @Override
    public T remove(T element) {
        // loops through every element of the list until desired element is found
        ListIterator<T> lit = listIterator();
        boolean foundIt = false;
        T retVal = null;
        while (lit.hasNext() && !foundIt) {
            retVal = lit.next();
            if (retVal.equals(element)) {
                foundIt = true;
                lit.remove();
            }
        }

        // if target element is not in the list
        if (!foundIt) {
            throw new NoSuchElementException();
        }
        return retVal;
    }

    @Override
    public T remove(int index) {
        // ensure the index is of an existing element
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        // starts list iterator right before the desired index to be removed
        ListIterator<T> lit = listIterator(index);
        T retVal = lit.next();
        lit.remove();
        return retVal;
    }

    @Override
    public void set(int index, T element) {
        // make sure the index is of an element that does exist
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        // starts list iterator right before the desired index to be set
        ListIterator<T> lit = listIterator(index);
        lit.next();
        lit.set(element);
    }

    @Override
    public T get(int index) {
        // make sure the index is of an existing element
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }

        // start list iterator right before element to be returned
        ListIterator<T> lit = listIterator(index);
        return lit.next();
    }

    @Override
    public int indexOf(T element) {
        // loop through the list until the element is found
        boolean found = false;
        Node<T> current = head;
        int i = 0;
        while (current != null && !found) {
            if (element.equals(current.getElement())) {
                found = true;
            } else {
                current = current.getNext();
                i++;
            }
        }

        // if element is never found
        if (!found) {
            return -1;
        }
        return i;
    }

    @Override
    public T first() {
        // list must have at least one element
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return head.getElement();
    }

    @Override
    public T last() {
        // List must have at least one element
        if (size == 0) {
            throw new NoSuchElementException();
        }
        return tail.getElement();
    }

    @Override
    public boolean contains(T target) {
        /*
         * Loops through the list until the target element is found or every element has
         * been checked
         */
        boolean found = false;
        Node<T> current = head;
        while (current != null && !found) {
            if (target.equals(current.getElement())) {
                found = true;
            } else {
                current = current.getNext();
            }
        }
        return found;
    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Iterator<T> iterator() {
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator() {
        return new DLLIterator();
    }

    @Override
    public ListIterator<T> listIterator(int startingIndex) {
        return new DLLIterator(startingIndex);

    }

    @Override
    public String toString() {
        // If the list is empty
        if (size == 0) {
            return "[]";
        }

        StringBuilder str = new StringBuilder();
        str.append("[");

        // Adds all elements to the string
        Node<T> current = head;
        while (current != null) {
            str.append(current.getElement().toString());
            str.append(", ");
            current = current.getNext();
        }

        // Eleminates the extra comma and space after last element
        str.delete(str.length() - 2, str.length());
        str.append("]");
        return str.toString();
    }

    /** Acts as both a basic Iterator and a ListIterator for IUDLL. */
    private class DLLIterator implements ListIterator<T> {

        private int nextIndex;
        private int iterModCount;
        private Node<T> nextNode;
        private Node<T> prevReturnedNode;

        /**
         * Instantiates the list iterator starting at the beginning of the list.
         */
        public DLLIterator() {
            this(0);
        }

        /**
         * Instantiates the list iterator starting at the given index.
         * 
         * @param startingIndex
         */
        public DLLIterator(int startingIndex) {
            // Index can be between 0 and the size of the list
            if ((startingIndex < 0) || (startingIndex > size)) {
                throw new IndexOutOfBoundsException();
            }

            // To keep track of iterator concurrency
            iterModCount = modCount;

            // If the index is in the second half of the list, to improve efficiency
            if (startingIndex > (size / 2)) {

                // If the index is at the very end of the list
                if (startingIndex == size) {
                    nextNode = null;
                    nextIndex = size;

                // All other indexes in the second half of the list
                } else {
                    nextNode = tail;
                    for (int i = size - 1; i > startingIndex; i--) {
                        nextNode = nextNode.getPrevious();
                        nextIndex--;
                    }
                }

            // If the index is in the first half of the list
            } else {
                nextNode = head;
                for (nextIndex = 0; nextIndex < startingIndex; nextIndex++) {
                    nextNode = nextNode.getNext();
                }
            }

            nextIndex = startingIndex;

            // So that remove() or set() cannot immediately be called
            prevReturnedNode = null;
        }

        @Override
        public boolean hasNext() {
            // ensure fail-fast if list has been externally modified since the list
            // iterator's creation
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return (nextNode != null);
        }

        @Override
        public T next() {
            // if the iterator is at the end of the list
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            prevReturnedNode = nextNode;
            nextNode = nextNode.getNext();
            nextIndex++;
            return prevReturnedNode.getElement();
        }

        @Override
        public void remove() {
            // ensure fail-fast if list has been externally modified since the list
            // iterator's creation
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            // if not following next, previous, or set
            if (prevReturnedNode == null) {
                throw new IllegalStateException();
            }

            // if removing the only element
            if (size == 1) {
                head = tail = null;

                // if removing the head
            } else if (prevReturnedNode == head) {
                head = prevReturnedNode.getNext();
                head.setPrevious(null);

                // if remocing the tail
            } else if (prevReturnedNode == tail) {
                tail = prevReturnedNode.getPrevious();
                tail.setNext(null);

                // if removing any other element
            } else {
                prevReturnedNode.getPrevious().setNext(prevReturnedNode.getNext());
                prevReturnedNode.getNext().setPrevious(prevReturnedNode.getPrevious());
            }

            // if last call was to next, removing node to the left, so current index has
            // changed
            if (prevReturnedNode != nextNode) {
                nextIndex--;

                // if last call was to previous, the nextNode will need to be updated
            } else {
                nextNode = nextNode.getNext();
            }

            // updates the modCounts, index, size, and ensures remove/set cannot be called
            // next
            modCount++;
            iterModCount++;
            size--;
            prevReturnedNode = null;
        }

        @Override
        public boolean hasPrevious() {
            // ensure fail-fast if list has been externally modified since the list
            // iterator's creation
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return (nextNode != head);
        }

        @Override
        public T previous() {
            // this eliminates the possibility of the iterator being before the first node
            if (!hasPrevious()) {
                throw new NoSuchElementException();
            }

            // if the iterator is after the tail, previous will be the tail
            if (nextNode == null) {
                nextNode = tail;

                // in all other iterator positions
            } else {
                nextNode = nextNode.getPrevious();
            }

            // update the nextIndex
            nextIndex--;

            // so that the iterator can remove/set following previous
            prevReturnedNode = nextNode;
            return nextNode.getElement();

        }

        @Override
        public int nextIndex() {
            // ensure fail-fast if list has been externally modified since the list
            // iterator's creation
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextIndex;
        }

        @Override
        public int previousIndex() {
            // ensure fail-fast if list has been externally modified since the list
            // iterator's creation
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            return nextIndex - 1;
        }

        @Override
        public void set(T e) {
            // ensure fail-fast if list has been externally modified since the list
            // iterator's creation
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }

            // if we are following a call to remove() or haven't yet called next() or
            // previous()
            if (prevReturnedNode == null) {
                throw new IllegalStateException();
            }

            // change the value
            prevReturnedNode.setElement(e);

            // updates the mod count, and no need to set prevReturnedNode to null
            iterModCount++;
            modCount++;
        }

        @Override
        public void add(T e) {
            // ensure fail-fast if list has been externally modified since the list
            // iterator's creation
            if (iterModCount != modCount) {
                throw new ConcurrentModificationException();
            }
            Node<T> newNode = new Node<T>(e);

            // if the list has no elements
            if (isEmpty()) {
                head = tail = newNode;

                // if the iterator is at the end
            } else if (nextNode == null) {
                tail.setNext(newNode);
                newNode.setPrevious(tail);
                tail = newNode;

                // if adding to the beginning
            } else if (nextNode == head) {
                newNode.setNext(head);
                head.setPrevious(newNode);
                head = newNode;

                // all other cases
            } else {
                newNode.setPrevious(nextNode.getPrevious());
                nextNode.getPrevious().setNext(newNode);
                newNode.setNext(nextNode);
                nextNode.setPrevious(newNode);

            }

            // updates the modCounts, index, size, and ensures remove/set cannot be called
            // next
            nextIndex++;
            iterModCount++;
            modCount++;
            size++;
            prevReturnedNode = null;
        }
    }

}
