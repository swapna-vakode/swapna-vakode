package com.cg.cli;

import java.time.YearMonth;
import java.util.Scanner;

import com.cg.entity.Insurance;
import com.cg.service.InsuranceService;
import com.cg.service.InsuranceServiceImpl;

public class MainMenu {
    static Scanner scanner = new Scanner(System.in);
    private static InsuranceService iService;

    public static String getInsuranceOption() {
        System.out.println(
                "Select one of the below insurance options:\n 1.Third party insurance \n 2.Comprehensive insurance ");
        System.out.print("Your option: ");
        int option = scanner.nextInt();
        if (option == 1) {
            return "Third Party";
        } else if (option == 2) {
            return "Comprehensive";
        } else {
            System.out.println("Invalid option. Please enter a valid option.");
            return getInsuranceOption();
        }
    }

    public static void insureBike() {
        System.out.println("Please enter the below details for your bike insurance.");
        System.out.print("Enter Bike Registration number: ");
        String regNo = scanner.nextLine();
        scanner.nextLine();

        double onRoadPrice = getPrice();

        int purchaseYear = getBikeYear();

        String insuranceType = getInsuranceOption();

        Insurance insurance = new Insurance();
        insurance.setRegistrationNo(regNo);
        insurance.setOnRoadPrice(onRoadPrice);
        insurance.setPurchaseYear(purchaseYear);
        insurance.setInsuranceType(insuranceType);
        // System.out.println(insurance);
        iService.calculateInsurance(insurance);
    }

    public static Double getPrice() {
        try {
            System.out.print("Enter Bike on road price: ");
            return Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.print("Price should be a number.");
            return getPrice();
        }
    }

    public static Integer getBikeYear() {
        int year = 0;
        try {
            System.out.print("Enter Bike purchase year: ");
            year = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.print("Year should be a number.");
            return getBikeYear();
        }
        if (year > YearMonth.now().getYear()) {
            System.out.print("Purchase year cannot be greater than current year.");
            return getBikeYear();
        } else
            return year;
    }

    public static void getMenuOption() {
        System.out.println("Menu:\n 1.Insure Bike \n 2.Search by Id \n 3.Search by Registration No \n 4.Exit");
        System.out.print("Please select one option from the above displayed Menu options.\n Your option: ");
        int menuOption = scanner.nextInt();
        switch (menuOption) {
            case 1:
                insureBike();
                break;
            case 2:
                searchInsuranceById();
                break;
            case 3:
                searchInsuranceByRegNo();
                break;
            case 4:
                System.out.println("Good bye! Thank your for using our Bike Insurance services.");
                break;
            default:
                System.out.println("Invalid option.  Please select a valid menu option.");
                getMenuOption();
                break;
        }
    }

    public static void searchInsuranceById() {
        System.out.print("Please enter your Insurance Id: ");
        int insuranceId = scanner.nextInt();
        Insurance insurance = iService.fetchInsuranceById(insuranceId);
        if (insurance != null)
            printInsuranceData(insurance);
        else {
            System.out.println("Invalid Insurance Id.");
        }
    }

    public static void searchInsuranceByRegNo() {
        System.out.print("Please enter your Bike registration number: ");
        String regNo = scanner.nextLine();
        scanner.nextLine();
        Insurance insurance = iService.fetchInsuranceByRegistrationNo(regNo);
        if (insurance != null)
            printInsuranceData(insurance);
        else {
            System.out.println("Invalid registration number.");
        }
    }

    public static void printInsuranceData(Insurance insurance) {
        System.out.println("Your bike insurance details are: ");
        System.out.print("   Insurance Id: " + insurance.getInsuranceId());
        System.out.print("   Insurance amount: " + insurance.getInsuranceAmount());
        System.out.print("   Insurance type: " + insurance.getInsuranceType());
        System.out.print("   Bike Registration No: " + insurance.getRegistrationNo());
        System.out.print("   Bike Purchase year: " + insurance.getPurchaseYear());
        System.out.print("   Bike on road price: " + insurance.getOnRoadPrice());
        System.out.print("   Insurance expiry date: " + insurance.getExpiryDate());
    }

    public static void main(String[] args) {
        iService = new InsuranceServiceImpl();
        System.out.println("Hello User!.\nWelcome to Bike Insurance service.");
        getMenuOption();

    }

}
