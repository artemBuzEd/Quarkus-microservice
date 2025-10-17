package com.artembuzed.registration;

import java.util.List;

public interface RegistrationRepository {
    List<Registration> findAll();
    Registration findById(Long id);
    Registration save(Registration registration);
}
