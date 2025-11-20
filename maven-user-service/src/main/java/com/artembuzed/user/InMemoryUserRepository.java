package com.artembuzed.user;
import jakarta.inject.Singleton;
import java.util.Dictionary;
import java.util.Hashtable;

@Singleton
public class InMemoryUserRepository implements UserRepository {
    private static final Dictionary<String, User> userDictionary = new Hashtable<>();
    public InMemoryUserRepository() {
        userDictionary.put("alice", new User("alice","johndoe@gmail.com","0990990999"));
        userDictionary.put("bob", new User("bob","tarasShevchenko@gmail.com","0950842334"));
        userDictionary.put("asdfl33fds", new User("asdfl33fds","ivanFranko@gmail.com","0983842351"));
    }
    @Override
    public boolean existsByUserId(String userId) {
        var user = userDictionary.get(userId);
        return user != null;
    }
}

