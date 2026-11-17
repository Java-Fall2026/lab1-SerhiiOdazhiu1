package ua.hotel.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GenericRepository<T> {

    private static final Logger log = LoggerFactory.getLogger(GenericRepository.class);
    private final String name;
    private final IdentityExtractor<T> extractor;
    private final Map<String, T> items = new LinkedHashMap<>();

    public GenericRepository(String name, IdentityExtractor<T> extractor) {
        this.name = name;
        this.extractor = extractor;
    }

    public void add(T item) {
        String id = identityOf(item);
        if (items.containsKey(id)) {
            log.warn("Duplicate rejected in {}: {}", name, id);
            throw new DuplicateIdentityException(name, id);
        }
        items.put(id, item);
        log.info("Added to {}: {}", name, id);
    }

    public boolean addIfAbsent(T item) {
        String id = identityOf(item);
        if (items.containsKey(id)) {
            log.warn("Duplicate ignored in {}: {}", name, id);
            return false;
        }
        items.put(id, item);
        log.info("Added to {}: {}", name, id);
        return true;
    }

    public void addAll(Collection<T> newItems) {
        for (T item : newItems) {
            add(item);
        }
    }

    public boolean removeByIdentity(String identity) {
        if (items.remove(identity) != null) {
            log.info("Removed from {}: {}", name, identity);
            return true;
        }
        log.warn("Not found for removal in {}: {}", name, identity);
        return false;
    }

    public Optional<T> findByIdentity(String identity) {
        Optional<T> found = Optional.ofNullable(items.get(identity));
        if (found.isEmpty()) {
            log.warn("Not found in {}: {}", name, identity);
        }
        return found;
    }

    public List<T> findAll() {
        return new ArrayList<>(items.values());
    }

    public int size() {
        return items.size();
    }

    private String identityOf(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item must not be null in " + name);
        }
        String id = extractor.identityOf(item);
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Identity must not be blank in " + name);
        }
        return id;
    }
}