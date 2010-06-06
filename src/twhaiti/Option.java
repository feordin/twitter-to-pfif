package twhaiti;

import java.util.Iterator;
import java.util.NoSuchElementException;
 
/**
 * This is taken from: http://eng.kaching.com/2010/05/better-option-for-java.html
 * @param <T>
 */
public abstract class Option<T> implements Iterable<T>
{
    interface Visitor<T, S> {
        S onSome(T x);
        S onNone();
    }
    
    public abstract T getOrElse(T other); 
    public abstract <S> S accept(Visitor<T, S> visitor);
 
    private static abstract class ImmutableIterator<T>
     implements Iterator<T> {
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
     
    private static class None<T> extends Option<T> {
        Iterator<T> EMPTY = new ImmutableIterator<T>() {
            public boolean hasNext() { return false; }
 
            public T next() {
                throw new NoSuchElementException();
            }
        };
         
        public Iterator<T> iterator() { return EMPTY; }
 
        public T getOrElse(T other) {
        	return other;
        } 
        @Override
        public <S> S accept(Option.Visitor<T, S> visitor)
        {
            return visitor.onNone();
        }
    }
     
    private static None<Object> none = new None<Object>();
     
    private static class Some<T> extends Option<T> {
        private T value;
         
        Some(T value) {
            this.value = value;
        }
         
        public T getOrElse(T other) {
        	return value;
        } 

        public boolean equals(Object o) {
            return o instanceof Some<?> &&
              (((Some<?>) o).value).equals(value);
        }
 
        public int hashCode() {
            return value.hashCode();
        }
         
        public Iterator<T> iterator()
        {
            return new ImmutableIterator<T>() {
                boolean hasNext = true;
 
                public boolean hasNext() {
                    return hasNext;
                }
 
                public T next()
                {
                    if (!hasNext) throw new NoSuchElementException();
                    hasNext = false;
                    return value;
                }
            };
        }
 
        @Override
        public <S> S accept(Option.Visitor<T, S> visitor)
        {
            return visitor.onSome(value);
        }
    }
     
    static <T> Option<T> some(Option<T> value) {
        return value == null ? Option.<T> none() : value;
    }
 
    @SuppressWarnings("unchecked")
    static <T> Option<T> some(T value) {
        return value == null              ? Option.<T> none() :
               value instanceof Option<?> ? (Option<T>) value    :
                                             new Some(value);
    }
     
    @SuppressWarnings("unchecked")
    static <T> Option<T> none() {
        return (None<T>) none;
    }
}
