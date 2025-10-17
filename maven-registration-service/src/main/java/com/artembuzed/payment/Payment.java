package com.artembuzed.payment;

import java.time.LocalDate;

public record Payment(
        Long id,
        String userId,
        Long registrationId,
        LocalDate CreatedAt
) {
}
