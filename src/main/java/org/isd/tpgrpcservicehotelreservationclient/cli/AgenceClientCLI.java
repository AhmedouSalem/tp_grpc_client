package org.isd.tpgrpcservicehotelreservationclient.cli;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Scanner;

import org.isd.stubs.Consulteroffre;
import org.isd.stubs.Reserverchambre;
import org.isd.tpgrpcservicehotelreservationclient.service.ReservationServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;



import io.grpc.StatusRuntimeException;

@Component
public class AgenceClientCLI implements CommandLineRunner {
	@Autowired
	private ReservationServiceClient reservationServiceClient;

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
		String choice = "";
		
		System.out.println("Bienvenue à : Hotel Reservation Service CLI");

		do {
			menu();
			choice = scanner.nextLine().trim();

			switch(choice) {
				case "1":
					listDesOffresDisponibles(scanner);
					break;

				case "2":
					effectuerUneReservation(scanner);
					break;

				case "q":
					System.out.println("Exiting CLI...");
					System.exit(0);
					break;

				default:
					System.out.println("Invalid option. Please try again.");
			}

		} while (!choice.equalsIgnoreCase("q"));
	}
	
	private void menu() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("\nMenu:")
			.append("\n1. Chercher des offres disponibles")
			.append("\n2. Effectuer une reservation")
			.append("\nq. Quitter")
			.append("\nEntrez votre choix: ");
		
		System.out.println(buffer);
	}

	private void listDesOffresDisponibles(Scanner scanner) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
		try {
			String dateDebut = readInput(scanner, "Entrez la date de début (yyyy-MM-dd) : ");
			String dateFin = readInput(scanner, "Entrez la date de fin (yyyy-MM-dd) : ");
			Integer nbPersonne = Integer.parseInt(readInput(scanner, "Entrez le nombre de personnes : "));

			if (!isValidDate(dateDebut) || !isValidDate(dateFin)) {
				System.out.println("Les dates fournies sont invalides.");
				return;
			}

			Consulteroffre.ConsultRequest request = Consulteroffre.ConsultRequest.newBuilder()
					.setDateDebut(dateDebut)
					.setDateFin(dateFin)
					.setNbPersonne(nbPersonne)
					.build();

			Consulteroffre.ConsultResponse consultResponse = reservationServiceClient.getOffreDiponibles(request);
			System.out.printf("%-10s %-15s %-20s %-10s %-30s\n", "ID Offre", "Type Chambre", "Date Disponibilité", "Prix", "Image URL");
			consultResponse.getOffresList().forEach(offre -> {
				System.out.printf("%-10d %-15s %-20s %-10.2f %-30s\n",
						offre.getIdOffre(), offre.getTypeChambre(),
						offre.getDateDisponibilite(), offre.getPrix(), offre.getImageUrl());
			});

		} catch (NumberFormatException e) {
			System.err.println("Erreur : Nombre de personnes invalide.");
		} catch (StatusRuntimeException e) {
			System.err.println("Aucune offre disponible : " + e.getStatus().getDescription());
		}
	}




	private void effectuerUneReservation(Scanner scanner) {
		try {
			Integer idOffre = Integer.parseInt(readInput(scanner, "Entrez l'identifaint de l'offre : "));
			String prenom = readInput(scanner, "Entrez votre prenom : ");
			String nom = readInput(scanner, "Entrez votre nom : ");
			String email = readInput(scanner, "Entrez votre email : ");
			
			Reserverchambre.ReserverRequest request = Reserverchambre.ReserverRequest.newBuilder()
					.setIdOffre(idOffre)
					.setPrenom(prenom)
					.setNom(nom)
					.setEmail(email)
					.build();
			
			Reserverchambre.ReserverResponse reserverResponse = reservationServiceClient.reserverUnChambre(request);
			
			System.out.printf("success : %s,  message: %s, reference : %s",
					reserverResponse.getReservationReservation().getMessage(),
					reserverResponse.getReservationReservation().getMessage(),
					reserverResponse.getReservationReservation().getReservationReference());
		} catch (StatusRuntimeException e) {
            System.err.println("Error: " + e.getStatus().getDescription());
        }
	}

	private String readInput(Scanner scanner, String prompt) {
		System.out.print(prompt);
		return scanner.nextLine().trim();
	}

	private boolean isValidDate(String dateStr) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
			formatter.setLenient(false);
			formatter.parse(dateStr);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
