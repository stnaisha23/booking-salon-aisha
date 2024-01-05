package com.booking.service;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.booking.models.Customer;
import com.booking.models.Employee;
import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;
import com.booking.repositories.PersonRepository;
import com.booking.repositories.ServiceRepository;

public class ReservationService {
    private static Scanner input = new Scanner(System.in);

    public static void createReservation() {
        String reservationId = "Rsv-" + System.currentTimeMillis();
        Customer customer = getCustomerByCustomerId();
        Employee employee = chooseEmployee();
        List<Service> selectedServices = chooseServices();
        String workstage = "In Process";
        double reservationPrice = calculateReservationPrice(customer, selectedServices);

        Reservation reservation = new Reservation(reservationId, customer, employee, selectedServices, workstage);
        reservation.setReservationPrice(reservationPrice);

        System.out.println("Booking Berhasil!");
        System.out.println("Total Biaya Booking : Rp. " + reservationPrice);
    }

    private static Customer getCustomerByCustomerId() {
        System.out.println("No\tID\tNama\tAlamat\tMembership\tUang");
        List<Person> persons = PersonRepository.getAllPerson();
        persons.stream()
                .filter(person -> person instanceof Customer)
                .map(person -> (Customer) person)
                .forEach(customer -> System.out.println(customer.getId() + "\t" +
                        customer.getName() + "\t" + customer.getAddress() + "\t" +
                        (customer.getMember() != null ? customer.getMember().getMembershipName() : "none") + "\t" +
                        "Rp" + customer.getWallet()));

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
        System.out.println("No\tID\tNama\tAlamat\tPengalaman");
        List<Person> persons = PersonRepository.getAllPerson();
        persons.stream()
                .filter(person -> person instanceof Employee)
                .map(person -> (Employee) person)
                .forEach(employee -> System.out.println(employee.getId() + "\t" +
                        employee.getName() + "\t" + employee.getAddress() + "\t" +
                        employee.getExperience()));

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
            System.out.println("No\tID\tNama\tHarga");
            serviceList.forEach(service -> System.out.println(service.getId() + "\t" +
                    service.getServiceName() + "\tRp" + service.getPrice()));

            System.out.print("Silahkan Masukkan Service Id: ");
            String serviceId = input.nextLine();

            Service selectedService = serviceList.stream()
                    .filter(service -> service.getId().equals(serviceId))
                    .findFirst().orElse(null);

            if (selectedService == null) {
                System.out.println("Service yang dicari tidak tersedia");
            } else if (selectedServices.contains(selectedService)) {
                System.out.println("Service sudah dipilih");
            } else {
                selectedServices.add(selectedService);
            }

            System.out.print("Ingin pilih service yang lain (Y/T)? ");
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
            System.out.print("Selesaikan reservasi : ");
            String newWorkstage = input.nextLine();
            
            if (newWorkstage.equalsIgnoreCase("Finish") || newWorkstage.equalsIgnoreCase("Cancel")) {
                reservationToEdit.setWorkstage(newWorkstage);
                System.out.println("Reservasi dengan id " + reservationId + " sudah " + newWorkstage);
            } else {
                System.out.println("Tidak valid");
            }
        } else if (reservationToEdit != null && !reservationToEdit.getWorkstage().equalsIgnoreCase("In Process")) {
            System.out.println("Reservasi dengan id " + reservationId + " tidak dalam tahap 'In Process' dan tidak dapat diedit");
        } else {
            System.out.println("Reservasi dengan id " + reservationId + "tidak tersedia atau sudah selesai");
        }
    }
    
    private static Reservation findReservationById(String reservationId) {
        List<Reservation> reservationList = new ArrayList<>();  // Placeholder, replace with actual data retrieval
    
        return reservationList.stream()
                .filter(reservation -> reservation.getReservationId().equals(reservationId))
                .findFirst().orElse(null);
    }
}