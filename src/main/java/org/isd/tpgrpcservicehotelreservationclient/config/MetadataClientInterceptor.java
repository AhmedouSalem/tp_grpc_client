package org.isd.tpgrpcservicehotelreservationclient.config;

import io.grpc.ClientInterceptor;
import io.grpc.ClientCall;
import io.grpc.Metadata;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ForwardingClientCall.SimpleForwardingClientCall;
import io.grpc.MethodDescriptor;
import org.springframework.stereotype.Component;

@Component
public class MetadataClientInterceptor implements ClientInterceptor {

    private static final Metadata.Key<String> IDENTIFIANT_KEY =
            Metadata.Key.of("identifiant", Metadata.ASCII_STRING_MARSHALLER);
    private static final Metadata.Key<String> PASSWORD_KEY =
            Metadata.Key.of("password", Metadata.ASCII_STRING_MARSHALLER);

    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> method,
                                                               CallOptions callOptions,
                                                               Channel next) {
        return new SimpleForwardingClientCall<ReqT, RespT>(next.newCall(method, callOptions)) {
            @Override
            public void start(Listener<RespT> responseListener, Metadata headers) {
                // Ajouter les métadonnées
                headers.put(IDENTIFIANT_KEY, "dream123");
                headers.put(PASSWORD_KEY, "password234");
                super.start(responseListener, headers);
            }
        };
    }
}
