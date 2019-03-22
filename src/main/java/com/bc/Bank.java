package com.bc;

// Demo of bank account management
public class Bank {
    // Main program
    public static void main(String[] args)
    {
// Open an account for Zhang3
        Account zhang3 = new Account();
// Deposit 500 yuan to Zhang3's account,
// then withdraw 100 yuan from his account
        zhang3.deposit(500);
        if (!zhang3.withdraw(100))
            System.out.println("Not enough balance !");
// Open an account for Li4
        Account li4 = new Account();
// Transfer 150 yuan from Zhang3's account to Li4's account
        if (!zhang3.withdraw(150))
            System.out.println("Not enough balance to transfer !");
        else li4.deposit(150);
// Query new balances
        System.out.println("Zh3\'s balance: " + zhang3.getBalance());
        System.out.println("Li4\'s balance: " + li4.getBalance());
    }
}