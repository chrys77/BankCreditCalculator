import java.io.FileWriter;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    static final int MONTHS_IN_YEAR = 12;
    static final int PERCENT = 100;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        CsvWriter csvWriter;

        int amount;
        int period;
        double interestRate;

        try {
            FileWriter writer = new FileWriter(FileProvider.getFile());
            csvWriter = new CsvWriter(writer);
            csvWriter.writeHeader();
        } catch (IOException e) {
            System.out.println("Some error occured when inializing the CsvWriter: " + e.getMessage());
            return;
        }


        try {
            System.out.print("Enter the amount of money: ");
            amount = sc.nextInt();

            System.out.print("Enter the loan period in years: ");
            period = sc.nextInt();

            System.out.print("Enter the annual interest rate: ");
            interestRate = sc.nextDouble();

        } catch (InputMismatchException e) {
            System.out.println("Enter only numbers");
            return;
        }

        double balance = amount;
        for(int month = 1; month <= period * MONTHS_IN_YEAR; month++) {
            double lastMonthBalance = balance;
            double monthlyRate = calculateRate(amount, period, interestRate);
            double monthlyInterestRate = calculateInterest(lastMonthBalance, interestRate);
            double paidAmount = monthlyRate - monthlyInterestRate;

            balance = lastMonthBalance - paidAmount;

            //System.out.printf("\nMonth: %d | Rate: %f | Interest: %f | Paid Amount: %f | Balance: %f", month, monthlyRate, monthlyInterestRate, paidAmount, balance);

            try {
                csvWriter.writeRecord(month, monthlyRate, monthlyInterestRate, paidAmount, period);
            } catch (IOException e) {
                System.out.println("Error while writing the csv file: " + e.getMessage());
            }
        }

        try {
            csvWriter.closeFile();
        } catch (IOException e) {
            System.out.println("Something went wrong when trying to close the csv file: " + e.getMessage());
        }


    }

    //Rate calculator
    private static double calculateRate(int amount, int period, double interestRate) {
        double monthlyRate = interestRate / PERCENT / MONTHS_IN_YEAR;
        return (monthlyRate * amount) / (1 - Math.pow(1 + monthlyRate, (-period * MONTHS_IN_YEAR)));
    }

    //Calculation of interest per month
    private static double calculateInterest(double balance, double interestRate) {
        double interestPerYear = balance * interestRate / PERCENT;
        return interestPerYear / MONTHS_IN_YEAR;
    }


}