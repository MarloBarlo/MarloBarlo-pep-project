package Service;

import Model.Account;
import DAO.AccountDAO;

import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;

    //No arg constructor
    public AccountService(){
        accountDAO = new AccountDAO();
    }

    //arg construtor
    public AccountService(AccountDAO accountDAO){
        this.accountDAO = accountDAO;
    }

    public Account register(Account account) {
        // Validate account data before creating
        if (account.getUsername() == null || account.getUsername().isEmpty() ||
            account.getPassword() == null || account.getPassword().length() < 4) {
            throw new IllegalArgumentException("Username or password is invalid");
        }

        // Check if account with the same username already exists
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) {
            throw new IllegalArgumentException("Username already exsits");
        }
        // Creates account if all checks pass SQL also checks for all the exceptions for saftey
        accountDAO.createAccount(account);
        return account;
    }

    public Account login(String username, String password) {
        // Validate login credentials
        Account account = accountDAO.getAccountByUsername(username);
        if (account != null && account.getPassword().equals(password)) {
            return account; // Login successful
        }
        throw new IllegalArgumentException("Username or password is incorrect");
    }
    

    public List<Account> getAllAccounts() {
        return accountDAO.getAllAccounts();
    }

    public boolean deleteAccount(int id) {
        accountDAO.deleteAccount(id);
        return true;
    }
       
}
