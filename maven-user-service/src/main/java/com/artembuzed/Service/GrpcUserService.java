package com.artembuzed.Service;

import com.artembuzed.user.UserRepository;
import com.artembuzed.CheckUserExistsRequest;
import com.artembuzed.CheckUserExistsResponse;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;

@GrpcService
public class GrpcUserService implements com.artembuzed.UserService {

    @Inject
    UserRepository userRepository;

    @Override
    public Uni<CheckUserExistsResponse> checkUserExists(CheckUserExistsRequest request) {
        var exist = userRepository.existsByUserId(request.getUserId());
        return Uni.createFrom().item(CheckUserExistsResponse.newBuilder()
                .setExists(exist)
                .build());
    }
}
