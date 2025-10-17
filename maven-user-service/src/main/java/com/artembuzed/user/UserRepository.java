package com.artembuzed.user;

public interface UserRepository {
    boolean existsByUserId(String userId);
}
