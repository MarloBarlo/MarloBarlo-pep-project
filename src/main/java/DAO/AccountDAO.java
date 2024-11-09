package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {
    
    public Account loginUser (Account account){
        Connection connection = ConnectionUtil.getConnection();

        try {
            //SQL logic
            String sql = "SELECT * FROM Account WHERE EXISTS(SELECT * FROM Account WHERE username = ? AND password =?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(1, account.getPassword());

            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account rsAccount = new Account(rs.getInt("account_id"), 
                                rs.getString("posted_by"), 
                                rs.getString("message_text"));
                return rsAccount;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void createAccount(Account account){
        Connection connection = ConnectionUtil.getConnection();

        try {
            //SQL logic
            String sql = "INSERT INTO Account (username, password) values (?, ?);";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //PreparedStatement methods
            preparedStatement.setString(1, account.getUsername());
            preparedStatement.setString(2, account.getPassword());

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }    

    public List<Account> getAllAccounts(){
        Connection connection = ConnectionUtil.getConnection();

        List<Account> accounts = new ArrayList<>();
        try {
            //SQL logic
            String sql = "SELECT * FROM Accounts;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(account);
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public Account getAccountByUsername(String user){
        Connection connection = ConnectionUtil.getConnection();

        try {
            //SQL logic
            String sql = "SELECT * FROM Account WHERE username = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, user);
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                Account account = new Account(rs.getInt("account_id"), 
                                rs.getString("username"), 
                                rs.getString("password"));
                return account;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean doesUserExist(int userId) {
        Connection connection = ConnectionUtil.getConnection();

        try {
            String sql = "SELECT COUNT(*) FROM Account WHERE account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, userId);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                return rs.getInt(1) > 0; // Return true if user exists
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false; // User does not exist
    }

    public Account deleteAccount(int id){
        Connection connection = ConnectionUtil.getConnection();

        try {
            //SQL logic
            String sql = "DELETE FROM Account WHERE account_id = ?;";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1,id);

            preparedStatement.executeUpdate();
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }
    
}
