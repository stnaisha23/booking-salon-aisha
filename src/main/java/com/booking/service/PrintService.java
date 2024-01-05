package com.booking.service;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

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
        for (Service service : serviceList) {
            result += service.getServiceName() + ", ";
        }
        return result;
    }

    public void showRecentReservation(List<Reservation> reservationList) {
        System.out.printf("| %-11s | %-15s | %-15s | %-15s | %-20s |\n",
                "ID", "Nama Customer", "Service", "Total Biaya", "Workstage");
        System.out.println("+====================================================================+");
        for (Reservation reservation : reservationList) {
            if (reservation.getWorkstage().equalsIgnoreCase("Waiting") || reservation.getWorkstage().equalsIgnoreCase("In process")) {
                if (reservation.getServices().size() > 0) {
                    System.out.printf("| %11s ", reservation.getReservationId());
                    System.out.printf("| %-15s", reservation.getCustomer().getName());
                    System.out.printf("| %-15s", printServices(reservation.getServices()));
                    System.out.printf("| %-15s", formatCurrency(reservation.getReservationPrice()));
                    System.out.printf("| %-20s", reservation.getWorkstage());
                    System.out.println();
                }
            }
        }
    }

    public void showAllCustomer() {
        List<Person> personList = PersonRepository.getAllPerson();
        System.out.println("+================================================================================+");
        System.out.printf("| %11s | %-11s | %-15s| %-15s| %-15s |\n", "ID", "Nama", "Alamat", "Membership", "Uang");
        System.out.println("+================================================================================+");
        for (Person person : personList) {
            if (person instanceof com.booking.models.Customer) {
                com.booking.models.Customer customer = (com.booking.models.Customer) person;
                {                        
                    System.out.printf("| %11s ", customer.getId());
                    System.out.printf("| %-11s ", customer.getName());
                    System.out.printf("| %-15s ", customer.getAddress());
                    System.out.printf("| %-15s",  customer.getMember() != null ? customer.getMember().getMembershipName() : "None");
                    System.out.printf("| %-15s |", (formatCurrency(customer.getWallet())));
                    System.out.println();
                }
            }
        }
        System.out.println("+================================================================================+");
    }

    public void showAllEmployee() {
        List<Person> personList = PersonRepository.getAllPerson();
        System.out.println("+==============================================================+");
        System.out.printf("| %11s | %-11s | %-15s| %-15s \n", "ID", "Nama", "Alamat", "Pengalaman");
        System.out.println("+==============================================================+");
        for (Person person : personList) {
            if (person instanceof com.booking.models.Employee) {
                com.booking.models.Employee employee = (com.booking.models.Employee) person;
                {                        
                    System.out.printf("| %11s ", employee.getId());
                    System.out.printf("| %-11s ", employee.getName());
                    System.out.printf("| %-15s",  employee.getAddress());
                    System.out.printf("| %-15s |", employee.getExperience());
                    System.out.println();
                }
            }
        }
         System.out.println("===============================================================+");
    }

    public void showHistoryReservation(List<Reservation> reservationList) {
        double totalProfit = 0;
        System.out.printf("| %-11s | %-15s | %-15s | %-15s | %-20s |\n",
                "ID", "Nama Customer", "Service", "Total Biaya", "Workstage");
        System.out.println("+====================================================================+");
        for (Reservation reservation : reservationList) {
            if (reservation.getWorkstage().equalsIgnoreCase("Finish") || reservation.getWorkstage().equalsIgnoreCase("Cancel")) {
                if (reservation.getServices().size() > 0) {
                    System.out.printf("| %11s ", reservation.getReservationId());
                    System.out.printf("| %-15s", reservation.getCustomer().getName());
                    System.out.printf("| %-15s", printServices(reservation.getServices()));
                    System.out.printf("| %-15s", formatCurrency(reservation.getReservationPrice()));
                    System.out.printf("| %-20s", reservation.getWorkstage());
                    System.out.println();
                    totalProfit += reservation.getReservationPrice();
                }
            }
        }
        System.out.println("| Total Keuntungan : " + formatCurrency(totalProfit));
        System.out.println("+====================================================================+");
    }

    public static String formatCurrency(double formatCurrency) {
        Locale indonesiaLocale = new Locale("id", "ID");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(indonesiaLocale);
        return currencyFormat.format(formatCurrency);
    }

}
