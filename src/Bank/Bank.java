package Bank;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

public class Bank {
    private List<BankAccount> bankAccounts;
    private String branchName;
    private Map<String, Double> vault; // The total amount of money this bank is holding

    public Bank(String branchName) {
        this.branchName = branchName;
        this.bankAccounts = new ArrayList<>();
        this.vault = new ConcurrentHashMap<>();

        Timer bankNightTime = new Timer();
        Timer bankDayTime = new Timer();

        // Gets started immediately
        bankDayTime.schedule(new BankNightlyTask(this), 0, 10000);
        // This starts 5 seconds after
        bankNightTime.schedule(new BankDailyTask(this), 5000 , 10000);

    }

    /**
     * Every night the vault gets loaded with the sum total of all it users bank account holding
     * // For safety purposes
     */
    public void updateVault() {
        for (BankAccount bA : this.bankAccounts) {
            vault.put(bA.getUser(), bA.getBalance());
        }
    }

    /**
     * Every Morning the bank vault contents are counted to see how much money the bank is holding
     */
    public void displayBankHoldings() {
        double bankTotalHoldings = 0d;

        for (Map.Entry entry : this.vault.entrySet()) {
            bankTotalHoldings += (double) entry.getValue();
        }

        System.out.println("Total Holdings: " + bankTotalHoldings);
    }

    private void storeNewBankAccount(BankAccount bA){
        this.bankAccounts.add(bA);
    }

    public void createUserAccount(User user, double deposit){
        this.storeNewBankAccount(new BankAccount(user, deposit));
    }


    private abstract class BankTask extends TimerTask{
        private Bank bank;
        public BankTask(Bank bank){
            this.bank = bank;
        }

        public Bank getBank(){
            return this.bank;
        }
    }

    private class BankNightlyTask extends BankTask{
        public BankNightlyTask(Bank bank){
            super(bank);
        }

        public void run(){
            super.getBank().updateVault();
        }
    }

    private class BankDailyTask extends BankTask{
        public BankDailyTask(Bank bank){
            super(bank);
        }

        public void run(){
            super.getBank().displayBankHoldings();
        }
    }
}
