package com.booking.service;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import com.booking.models.Customer;
import com.booking.models.Employee;
import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;
import com.booking.repositories.PersonRepository;
import com.booking.repositories.ServiceRepository;

public class ReservationService {
    private static List<Reservation> reservationList = new ArrayList<>();
    private static Scanner input = new Scanner(System.in);
    private static PrintService printService = new PrintService();

    public static void createReservation() {
        String reservationId = "Rsv-" + System.currentTimeMillis();
        Customer customer = getCustomerByCustomerId();
        Employee employee = chooseEmployee();
        List<Service> selectedServices = chooseServices();
        String workstage = "In Process";
        double reservationPrice = calculateReservationPrice(customer, selectedServices);

        Reservation reservation = new Reservation(reservationId, customer, employee, selectedServices, workstage);
        reservation.setReservationPrice(reservationPrice);

        reservationList.add(reservation);

        System.out.println("\nBooking Berhasil!");
        System.out.println("Total Biaya Booking : Rp. " + (printService.formatCurrency(reservationPrice)));
    }

    private static Customer getCustomerByCustomerId() {
        System.out.println("==================================================================================");
        System.out.printf("| %-12s | %-12s | %-15s | %-12s | %-15s |\n",
        "ID", "Nama", "Alamat", "Membership", "Uang");
        System.out.println("==================================================================================");
        List<Person> persons = PersonRepository.getAllPerson();
        persons.stream()
                .filter(person -> person instanceof Customer)
                .map(person -> (Customer) person)
                .forEach(customer -> System.out.printf("| %-12s | %-12s | %-15s | %-12s | %-15s |\n", customer.getId(),
                customer.getName(), customer.getAddress(), 
                customer.getMember() != null ? customer.getMember().getMembershipName() : "None", (formatCurrency(customer.getWallet()))));

        System.out.println("==================================================================================");
        System.out.print("Silahkan Masukkan Customer Id: ");
        Customer customer = null;
        do {
            final String customerId = input.nextLine();
            customer = (Customer) persons.stream()
                    .filter(person -> person instanceof Customer && person.getId().equals(customerId))
                    .findFirst().orElse(null);

            if (customer == null) {
                System.out.println("Customer yang dicari tidak tersedia");
                System.out.print("Masukkan ulang customer id : ");
            }
        } while (customer == null);

        return customer;
    }

    private static Employee chooseEmployee() {
        System.out.println("==============================================================");
        System.out.printf("| %-12s | %-12s | %-15s | %-10s |\n",
        "ID", "Nama", "Alamat", "Pengalaman");
        System.out.println("==============================================================");
        List<Person> persons = PersonRepository.getAllPerson();
        persons.stream()
                .filter(person -> person instanceof Employee)
                .map(person -> (Employee) person)
                .forEach(employee -> System.out.printf("| %-12s | %-12s | %-15s | %-10s |\n", employee.getId(),
                        employee.getName(), employee.getAddress(),
                        employee.getExperience()));
        System.out.println("==============================================================");
        System.out.print("Silahkan Masukkan Employee Id: ");
        Employee employee = null;
        do {
            final String employeeId = input.nextLine();
            employee = (Employee) persons.stream()
                    .filter(person -> person instanceof Employee && person.getId().equals(employeeId))
                    .findFirst().orElse(null);

            if (employee == null) {
                System.out.println("Employee yang dicari tidak tersedia");
                System.out.print("Masukkan ulang employee id : ");
            }
        } while (employee == null);

        return employee;
    }

    private static List<Service> chooseServices() {
        List<Service> serviceList = ServiceRepository.getAllService();
        List<Service> selectedServices = new ArrayList<>();

        while (true) {
            System.out.println("=========================================================");
            System.out.printf("| %-12s | %-20s | %-15s |\n",
            "ID", "Nama Service", "Harga");
            System.out.println("=========================================================");
            serviceList.forEach(service -> System.out.printf("| %-12s | %-20s | %-15s |\n", service.getId(),
                service.getServiceName(), (formatCurrency(service.getPrice()))));
            System.out.println("=========================================================");
            System.out.print("Silahkan Masukkan Service Id: ");
            String serviceId = input.nextLine();

            Service selectedService = serviceList.stream()
                    .filter(service -> service.getId().equals(serviceId))
                    .findFirst().orElse(null);

            if (selectedService == null) {
                System.out.println("Service yang dicari tidak tersedia");
            } else if (selectedServices.contains(selectedService)) {
                System.out.println("Service sudah dipilih. Pilih Service yang lain");
            } else {
                selectedServices.add(selectedService);
            }

            System.out.print("\nIngin pilih service yang lain (Y/T)? ");
            String continueChoice = input.nextLine().toUpperCase();

            if (!continueChoice.equals("Y")) {
                break;
            }
        }

        return selectedServices;
    }

    private static double calculateReservationPrice(Customer customer, List<Service> selectedServices) {
        double basePrice = selectedServices.stream().mapToDouble(Service::getPrice).sum();
        double discount = 0;

        if (customer.getMember() != null) {
            switch (customer.getMember().getMembershipName().toLowerCase()) {
                case "none":
                    discount = 0;
                    break;
                case "silver":
                    discount = 0.05;
                    break;
                case "gold":
                    discount = 0.10;
                    break;
            }
        }

        return basePrice - (basePrice * discount);
    }

    public static void editReservationWorkstage() {
        System.out.print("Silahkan Masukkan Reservasi Id : ");
        String reservationId = input.nextLine();

        Reservation reservationToEdit = findReservationById(reservationId);

        if (reservationToEdit != null && reservationToEdit.getWorkstage().equalsIgnoreCase("In Process")) {
            System.out.print("Selesaikan reservasi (Finish/Cancel): ");
            String newWorkstage = input.nextLine();

            if (newWorkstage.equalsIgnoreCase("Finish") || newWorkstage.equalsIgnoreCase("Cancel")) {
                reservationToEdit.setWorkstage(newWorkstage);
                System.out.println("Reservasi dengan id " + reservationId + " sudah " + newWorkstage);
            } else {
                System.out.println("Tidak valid. Reservasi tidak dapat diubah.");
            }
        } else if (reservationToEdit != null && !reservationToEdit.getWorkstage().equalsIgnoreCase("In Process")) {
            System.out.println("Reservasi dengan id " + reservationId + " tidak dalam tahap 'In Process' dan tidak dapat diedit");
        } else {
            System.out.println("Reservasi dengan id " + reservationId + " tidak tersedia atau sudah selesai");
        }
    }

    private static Reservation findReservationById(String reservationId) {
        return reservationList.stream()
                .filter(reservation -> reservation.getReservationId().equals(reservationId))
                .findFirst().orElse(null);
    }

    public static String formatCurrency(double formatCurrency) {
        Locale indonesiaLocale = new Locale("id", "ID");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(indonesiaLocale);
        return currencyFormat.format(formatCurrency);
    }
}