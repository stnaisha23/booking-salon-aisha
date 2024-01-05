package com.booking.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;
import com.booking.repositories.PersonRepository;
import com.booking.repositories.ServiceRepository;

public class MenuService {
    private static List<Person> personList = PersonRepository.getAllPerson();
    private static List<Service> serviceList = ServiceRepository.getAllService();
    private static List<Reservation> reservationList = new ArrayList<>();
    private static Scanner input = new Scanner(System.in);

    // Deklarasikan objek PrintService
    private static PrintService printService = new PrintService();

    public static void mainMenu() {
        String[] mainMenuArr = {"Tampilkan Data", "Membuat Reservation", "Finish/Cancel Reservasi", "Exit"};
        String[] subMenuArr = {"Tampilkan Recent Reservation", "Tampilkan Customer", "Tampilkan Employee", "Tampilkan History Reservation + Total Keuntungan", "Back to main menu"};

        int choice;
        int subChoice;
        do {
            PrintService.printMenu("\nMain Menu", mainMenuArr);
            System.out.println();
            System.out.print("Pilih menu : ");
            choice = Integer.valueOf(input.nextLine());

            switch (choice) {
                case 1:
                    do {
                        PrintService.printMenu("Show Data", subMenuArr);
                        System.out.println();
                        System.out.print("Pilih submenu : ");
                        subChoice = Integer.valueOf(input.nextLine());
                        // Sub menu - menu 1
                        switch (subChoice) {
                            case 1:
                                printService.showRecentReservation(reservationList);
                                break;
                            case 2:
                                printService.showAllCustomer();
                                break;
                            case 3:
                                printService.showAllEmployee();
                                break;
                            case 4:
                                printService.showHistoryReservation(reservationList);
                                break;
                            case 0:
                                System.out.println("\nKembali ke menu utama");
                                break;
                            default:
                                System.out.println("Pilihan tidak valid. Silakan coba lagi.");
                                break;
                        }
                    } while (subChoice != 0);
                    break;
                case 2:
                    ReservationService.createReservation();
                    break;
                case 3:
                    ReservationService.editReservationWorkstage();
                    break;
                case 0:
                    System.out.println("Keluar dari program!!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }

        } while (choice != 0);

    }
}