package com.bc;

// Bank account management
public class Account {
// Attributes of an account

    private double balance = 0;
    // current balance
    // Deposit some amount to the account
    public void deposit(double amount)
    {
        balance = balance + amount;
    }
    // Withdraw some amount from the account
    public boolean withdraw(double amount)
    {
        if (amount <= balance)
        {
            balance = balance - amount;
            return true;
        }
        else return false;
    }
    // Query the current balance of the account
    public double getBalance()
    {
        return balance;
    }
}

