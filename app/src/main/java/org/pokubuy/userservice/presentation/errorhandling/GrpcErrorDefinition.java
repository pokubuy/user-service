package org.pokubuy.userservice.presentation.errorhandling;

import io.grpc.Status;
import org.jspecify.annotations.NonNull;
import org.slf4j.event.Level;

public abstract class GrpcErrorDefinition {

    private Class<? extends Throwable> cls;

    final void setThrowableClass(Class<? extends Throwable> cls) {
        this.cls = cls;
    }

    public final @NonNull Class<? extends Throwable> throwableClass() {
        assert cls != null;
        return cls;
    }

    public abstract Status grpcStatus();

    public abstract Level logLevel();

    public String logMessage(Throwable ex) {
        return ex.getMessage();
    }

    public String clientMessage(Throwable ex) {
        return ex.getMessage();
    }
}
