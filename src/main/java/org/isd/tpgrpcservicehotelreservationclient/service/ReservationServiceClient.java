package org.isd.tpgrpcservicehotelreservationclient.service;

import io.grpc.Metadata;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.isd.stubs.Consulteroffre;
import org.isd.stubs.HotelServiceConsulterGrpc;
import org.isd.stubs.HotelServiceReserverGrpc;
import org.isd.stubs.Reserverchambre;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceClient {
    // Automatically inject the gRPC client stub using the @GrpcClient annotation
    @GrpcClient("consulter-offre-service")
    private HotelServiceConsulterGrpc.HotelServiceConsulterBlockingStub blockingStubConsulter;
    @GrpcClient("reserver-chambre-service")
    private HotelServiceReserverGrpc.HotelServiceReserverBlockingStub blockingStubReserver;


    public Consulteroffre.ConsultResponse getOffreDiponibles(Consulteroffre.ConsultRequest request) {
        return blockingStubConsulter.consulterDisponibilites(request);
    }

    public Reserverchambre.ReserverResponse reserverUnChambre(Reserverchambre.ReserverRequest request) {
        return  blockingStubReserver.effectuerReservation(request);
    }
}
