package com.booking.service;

import java.util.List;

import com.booking.models.Person;
import com.booking.models.Reservation;
import com.booking.models.Service;
import com.booking.repositories.PersonRepository;

public class PrintService {
    public static void printMenu(String title, String[] menuArr){
        int num = 1;
        System.out.println(title);
        for (int i = 0; i < menuArr.length; i++) {
            if (i == (menuArr.length - 1)) {   
                num = 0;
            }
            System.out.println(num + ". " + menuArr[i]);   
            num++;
        }
    }

    public String printServices(List<Service> serviceList){
        String result = "";
        // Bisa disesuaikan kembali
        for (Service service : serviceList) {
            result += service.getServiceName() + ", ";
        }
        return result;
    }
    public void showRecentReservation(List<Reservation> reservationList){
        System.out.printf("| %-4s | %-15s | %-15s | %-15s | %-10s |\n",
                "ID", "Nama Customer", "Service", "Total Biaya", "Workstage");
        System.out.println("+==============================================================================+");
        for (Reservation reservation : reservationList) {
            if (reservation.getWorkstage().equalsIgnoreCase("Waiting") || reservation.getWorkstage().equalsIgnoreCase("In process")) {                        
                System.out.printf("| %11s | %-11s | %-15s| %-6s| %-20s| %-15s| %-15s| %-15s|\n", "ID", "Nama Customer", "Service", "Total Biaya", "Workstage", "Placement", "Salary", "Allowance");
                System.out.println("======================================================================================================================");
                System.out.printf("| %11s ", reservation.getReservationId());
                System.out.printf("| %-11s ", reservation.getCustomer().getName());
                System.out.printf("| %-15s",  printServices(reservation.getServices()));
                System.out.printf("| %-6s", reservation.getReservationPrice());
                System.out.printf("| %-20s", reservation.getWorkstage());
                System.out.println();
                System.out.println("======================================================================================================================");
            }
        }
    }

    public void showAllCustomer() {
        List<Person> personList = PersonRepository.getAllPerson();
        System.out.printf("| %-4s | %-7s | %-15s | %-8s |\n", "No.", "ID", "Nama", "Alamat", "Membership", "Uang");
        System.out.println("+========================================+");
        int num = 1;
        for (Person person : personList) {
            if (person instanceof com.booking.models.Customer) {
                com.booking.models.Customer customer = (com.booking.models.Customer) person;
                System.out.printf("| %-4s | %-7s | %-15s | %-8s |\n", num, customer.getId(), customer.getName(), customer.getAddress(),
                        customer.getMember() != null ? customer.getMember().getMembershipName() : "None", customer.getWallet());
                num++;
            }
        }
    }

    public void showAllEmployee() {
        List<Person> personList = PersonRepository.getAllPerson();
        System.out.printf("| %-4s | %-7s | %-15s | %-8s |\n", "No.", "ID", "Nama", "Alamat", "Pengalaman");
        System.out.println("+========================================+");
        int num = 1;
        for (Person person : personList) {
            if (person instanceof com.booking.models.Employee) {
                com.booking.models.Employee employee = (com.booking.models.Employee) person;
                System.out.printf("| %-4s | %-7s | %-15s | %-8s |\n", num, employee.getId(), employee.getName(), employee.getAddress(),
                        employee.getExperience());
                num++;
            }
        }
    }

    public void showHistoryReservation(List<Reservation> reservationList) {
        double totalProfit = 0;
        System.out.printf("| %-4s | %-15s | %-15s | %-15s | %-10s |\n",
                "ID", "Nama Customer", "Service", "Total Biaya", "Workstage");
        System.out.println("+==============================================================================+");
        System.out.println("+========================================================================================+");
        for (Reservation reservation : reservationList) {
            if (reservation.getWorkstage().equalsIgnoreCase("Waiting") || reservation.getWorkstage().equalsIgnoreCase("In process")) {                        
                System.out.printf("| %11s | %-11s | %-15s| %-6s| %-20s| %-15s| %-15s| %-15s|\n", "ID", "Nama Customer", "Service", "Total Biaya", "Workstage", "Placement", "Salary", "Allowance");
                System.out.println("======================================================================================================================");
                System.out.printf("| %11s ", reservation.getReservationId());
                System.out.printf("| %-11s ", reservation.getCustomer().getName());
                System.out.printf("| %-15s",  printServices(reservation.getServices()));
                System.out.printf("| %-6s", reservation.getReservationPrice());
                System.out.printf("| %-20s", reservation.getWorkstage());
                System.out.println();
                System.out.println("======================================================================================================================");
                System.out.println("|Total Keuntungan : Rp. " + totalProfit);
                System.out.println("======================================================================================================");
            }
        }
    }
}
