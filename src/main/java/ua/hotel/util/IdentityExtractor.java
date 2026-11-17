package ua.hotel.util;

@FunctionalInterface
public interface IdentityExtractor<T> {
    String identityOf(T item);
}