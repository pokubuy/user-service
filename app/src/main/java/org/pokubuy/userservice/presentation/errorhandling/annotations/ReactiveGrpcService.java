package org.pokubuy.userservice.presentation.errorhandling.annotations;

import org.springframework.grpc.server.service.GrpcService;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@GrpcService
public @interface ReactiveGrpcService {}
