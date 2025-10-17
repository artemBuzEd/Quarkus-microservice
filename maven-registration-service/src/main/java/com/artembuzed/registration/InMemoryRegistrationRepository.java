package com.artembuzed.registration;

import jakarta.inject.Singleton;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
public class InMemoryRegistrationRepository implements RegistrationRepository {
    private final AtomicLong ids = new AtomicLong(0);

    private final List<Registration> registrations = new CopyOnWriteArrayList<>();

    @Override
    public List<Registration> findAll() {
        return Collections.unmodifiableList(registrations);
    }

    @Override
    public Registration findById(Long id) {
        Optional<Registration> registration = registrations.stream().filter(r -> r.idIsValid(id)).findFirst();
        return registration.orElse(null);
    }

    @Override
    public Registration save(Registration registration) {
        registration.id = ids.incrementAndGet();
        registrations.add(registration);
        return registration;
    }
}
