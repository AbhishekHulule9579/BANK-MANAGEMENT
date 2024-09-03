package FirstProject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {

	private Connection connection;
	private Scanner scanner;
	
	public AccountManager(Connection connection,Scanner scanner) {
		this.connection=connection;
		this.scanner=scanner;
	}
	
	public void credit_money(long account_number)throws SQLException {
		scanner.nextLine();
		System.out.println("Enter Amount: ");
		double amount=scanner.nextDouble();
		scanner.nextLine();
		System.out.println("Enter security pin: ");
		String security_pin=scanner.nextLine();
		try {
			connection.setAutoCommit(false);
			if(account_number!=0) {
				PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM Accounts WHERE account_number= ? AND security_pin = ? ");
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet=preparedStatement.executeQuery();
				
				if(resultSet.next()) {
				        String Credit_query="UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
				        PreparedStatement preparedStatement2=connection.prepareStatement(Credit_query);
							preparedStatement2.setDouble(1, amount);
							preparedStatement2.setLong(2, account_number);
							int rowaffected=preparedStatement2.executeUpdate();
							if(rowaffected>0) {
								System.out.println("Rs."+amount+"Credited Succesfully!");
								connection.commit();
								connection.setAutoCommit(true);
								return;
							}
							else {
								System.out.println("Transaction Failed");
								connection.rollback();
								connection.setAutoCommit(true);
							}
					
					
				}
				else {
					System.out.println("Invalid Security pin");
				}
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
		
	
	}
	
	public void debit_money(long account_number)throws SQLException {
		
		scanner.nextLine();
		System.out.println("Enter Amount: ");
		double amount=scanner.nextDouble();
		scanner.nextLine();
		System.out.println("Enter security pin: ");
		String security_pin=scanner.nextLine();
		try {
			connection.setAutoCommit(false);
			if(account_number!=0) {
				PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM Accounts WHERE account_number= ? AND security_pin = ? ");
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet=preparedStatement.executeQuery();
				
				if(resultSet.next()) {
					double current_balance=resultSet.getDouble("balance");
					if(amount<=current_balance) {
						String debit_query="UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
						PreparedStatement preparedStatement1=connection.prepareStatement(debit_query);
							preparedStatement1.setDouble(1, amount);
							preparedStatement1.setLong(2, account_number);
							int rowaffected=preparedStatement1.executeUpdate();
							if(rowaffected>0) {
								System.out.println("Rs."+amount+"Debited Succesfully!");
								connection.commit();
								connection.setAutoCommit(true);
								return;
							}
							else {
								System.out.println("Transaction Failed");
								connection.rollback();
								connection.setAutoCommit(true);
							}
						
					}
					else {
						System.out.println("Insufficient Balance!");
					}
					
				}
				else {
					System.out.println("Invalid Security pin");
				}
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
		
	}
	
	public void transfer_money(long sender_account_number) throws SQLException {
		
		scanner.nextLine();
		System.out.println("Enter Receiver Account Number: ");
		long receiver_account_number=scanner.nextLong();
		System.out.println("Enter Amount: ");
		double amount=scanner.nextDouble();
		scanner.nextLine();
		System.out.println("Enter Security Pin: ");
		String security_pin=scanner.nextLine();
		
		try {
			connection.setAutoCommit(false);
			
			if(sender_account_number!=0 && receiver_account_number!=0 ) {
				PreparedStatement preparedStatement=connection.prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND security_pin = ?");
				preparedStatement.setLong(1, sender_account_number);
				preparedStatement.setString(2, security_pin);
				
				ResultSet resultSet=preparedStatement.executeQuery();
				if(resultSet.next()) {
					double current_balance=resultSet.getDouble("balance");
					if(amount<=current_balance) {
						String credit_query="UPDATE Accounts SET balance= balance + ? WHERE account_number = ?";
						String debit_query="UPDATE Accounts SET balance= balance - ? WHERE account_number = ?";
						PreparedStatement creditPreparedStatement=connection.prepareStatement(credit_query);
						PreparedStatement debitPreparedStatement=connection.prepareStatement(debit_query);
						creditPreparedStatement.setDouble(1, amount);
						creditPreparedStatement.setLong(2, receiver_account_number);
						debitPreparedStatement.setDouble(1, amount);
						debitPreparedStatement.setLong(2, sender_account_number);
						
						int rowaffected1=debitPreparedStatement.executeUpdate();
						int rowaffected2=creditPreparedStatement.executeUpdate();
						if(rowaffected1>0 && rowaffected2>0) {
							System.out.println("Transaction Successfull!");
							System.out.println("Rs"+amount+" Transfer Succesfully!");
							connection.commit();
							connection.setAutoCommit(true);
						}
						else {
							System.out.println("Transaction failed!");
							connection.rollback();
							connection.setAutoCommit(true);
						}
						
					}
					else {
						System.out.println("Insufficient Balance!");
					}
				}
				else {
					System.out.println("Invalid Security Pin!");
				}
			}
			else {
				System.out.println("Invalid Account number!");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		connection.setAutoCommit(true);
		
	}
	
	public void getBalance(long account_number) {
		scanner.nextLine();
		System.out.println("Enter Security pin: ");
		String security_pin=scanner.nextLine();
		try {
			PreparedStatement preparedStatement=connection.prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND security_pin = ?");
			preparedStatement.setLong(1, account_number);
			preparedStatement.setString(2, security_pin);
			ResultSet resultSet=preparedStatement.executeQuery();
			if(resultSet.next()) {
				double balance=resultSet.getDouble("balance");
				System.out.println("Balance: "+balance);
			}
			else {
				System.out.println("Invalid pin");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void delete_account(long account_number) {
		scanner.nextLine();
		System.out.println("Enter Security pin: ");
		String security_pin=scanner.nextLine();
		try {
			PreparedStatement preparedStatement=connection.prepareStatement("DELETE FROM Accounts WHERE account_number = ? AND security_pin = ?");
			preparedStatement.setLong(1, account_number);
			preparedStatement.setString(2, security_pin);
			int rowsaffected=preparedStatement.executeUpdate();
			if(rowsaffected>0) {
				System.out.println("Account Delete Successfully!");
			}
			else {
				System.out.println("check your account number and security pin");
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
