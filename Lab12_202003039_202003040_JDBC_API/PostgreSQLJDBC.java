import java.sql.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import java.math.BigDecimal;

import java.util.Scanner;

public class PostgreSQLJDBC 
{
	public static void main(String args[]) 
	{
		Connection c = null;
		try
		{
			// Load Postgresql Driver class
			Class.forName("org.postgresql.Driver");//this particular class with this particular name will be loaded 
			// Using Driver class connect to databased on localhost, port=5432, database=postgres, user=postgres, password=postgres. If cannot connect then exception will be generated (try-catch block)
			c = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres", "postgres");
			System.out.println("Opened database successfully");
			
			// Create instance of this class to call other methods as it not an absrtact class so to access other methods of the class we need to create objects of this class
			PostgreSQLJDBC p = new PostgreSQLJDBC();
			p.setSearchPath(c);
			
		
			//c.setAutoCommit(false);
		
			//p.insertDependentsTable(c);
			//p.deleteFromDependentsTable(c);
			//c.setAutoCommit(true);
			
			// Call method queryFranchisee_owned_branchTable to Create Run SELECT Query in franchisee_owned_branch Table
			//p.queryFranchisee_owned_branchTable(c);

			// Call method updEmployeeTable to Create Run UPDATE Query in Employee Table
			//p.updEmployeeSalary(c);
			
			// Call method ExecutingStoredProcedure to call function (stored procedure) 
			p.ExecutingStoredProcedure(c);
			
			c.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}
	
	void setSearchPath(Connection c)
	{
		Statement stmt = null;//creating statement object from connection object
		try
		{
			stmt = c.createStatement();//using connection object to createStatement() and returning the Statement object
			String sql = "SET search_path TO restaurantfranchisee;";
			stmt.executeUpdate(sql);
			stmt.close();
			System.out.println("Changed Search Path successfully to restaurantfranchisee schema");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}
	
	void insertDependentsTable(Connection c)
	{
		PreparedStatement stmt = null;
		String sql = "INSERT INTO dependents VALUES (?, ?, ?, ?, ?,?)";
		try
		{
			Scanner in = new Scanner(System.in);

			stmt = c.prepareStatement(sql);//using connection object you can create Statement object
			stmt.setBigDecimal(1,new BigDecimal(1000000050));
//insert into dependents values('1000000050','Shreya','Bijoy','Kakar','F','Wife');
			System.out.println("Enter dependent's first name:");
			String s = in.nextLine();
			System.out.println("You entered first_name ="+s);
			stmt.setString(2, s);
			
			System.out.println("Enter dependent's middle name:");
			String s2=in.nextLine();
			System.out.println("You entered middle name= "+s2);
			stmt.setString(3,s2);
			
			System.out.println("Enter dependent's last name:");
			String s3=in.nextLine();
			System.out.println("You entered last name = "+s3);
			stmt.setString(4,s3);
			
			stmt.setString(5,"F");
			stmt.setString(6,"Wife");
			
			int affectedRows = stmt.executeUpdate();
			stmt.close();
			System.out.println("Table Inserted successfully: Rows Affected: " + affectedRows);
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}
	
	void queryFranchisee_owned_branchTable(Connection c)
	{
		Statement stmt = null;
		try
		{
			stmt = c.createStatement();
			ResultSet rs=stmt.executeQuery("select fob.trading_license_no,fob.royalty_fees,b.rating,b.locality,b.city,b.region,f.cin,f.company_name as company_controlling"+
			" from franchisee_owned_branch fob natural join branch b natural join franchiseecompany f ");
			
			System.out.println("The following are franchisee-owned branches:- ");
			System.out.println(" ");
			
			while(rs.next())//rs.next() returns bool value
			{
				// Datatype mapping and functions to use with JDBC is found at https://www.tutorialspoint.com/jdbc/jdbc-data-types.htm
				String cin,region,city,locality,company_controlling;
				BigDecimal trading_license_no,royalty_fees;
				int rating;
				// Employee Infor
				cin = rs.getString("cin");
				region = rs.getString("region");
				city = rs.getString("city");
				locality = rs.getString("locality");
				company_controlling = rs.getString("company_controlling");
				
				trading_license_no = rs.getBigDecimal("trading_license_no");
				royalty_fees=rs.getBigDecimal("royalty_fees");
				rating=rs.getInt("rating");//getting integer for this particular 
				
				System.out.println(" trading_license_no: " + trading_license_no + ", royalty_fees= " + royalty_fees + ", rating:" + rating + " company_controlling=" + company_controlling + ", region=" + region + ", locality=" + locality);
				System.out.println(" ");
			}
			
			stmt.close();
			System.out.println("Franchisee_owned _branch table Queried successfully");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}

   }
   
   	void updEmployeeSalary(Connection c)
	{
		PreparedStatement stmt = null;
		String sql = "UPDATE restaurantfranchisee.employee SET monthly_salary = ? WHERE eid = ?";//'?' are parameters in the sql statement
		try
		{
			stmt = c.prepareStatement(sql);
			stmt.setBigDecimal(1, new BigDecimal(22000));//new BigDecimal()
			stmt.setBigDecimal(2, new BigDecimal(1000000004));
			int affectedRows = stmt.executeUpdate();
			stmt.close();
			System.out.println("Table Updated successfully: Rows Updated: " + affectedRows);

		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}

	void deleteFromDependentsTable(Connection c)
	{
		PreparedStatement stmt=null;
		String sql= " delete from dependents where eid=? and relationship=?";
		try{
			stmt =c.prepareStatement(sql);//insert into dependents values('1000000050','Sarika','Mani','Kakar','F','Grand Mother');
			stmt.setBigDecimal(1,new BigDecimal(1000000050));
			stmt.setString(2,"Grand Mother");
			int affectedRows=stmt.executeUpdate();
			stmt.close();
			System.out.println("Row deleted successfully: Rows delete"+affectedRows);
			
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
		
		
		
		
		
		
	}


   	void ExecutingStoredProcedure(Connection c)
	{
		CallableStatement cstmt = null;
		try
		{
			
			BigDecimal eid = new BigDecimal(1000000007);
			String sql = "{?=call restaurantfranchisee.get_dependent_details(?)}";
			cstmt = c.prepareCall(sql);//prepareCall()
			
			cstmt.registerOutParameter(1,java.sql.Types.INTEGER);
			cstmt.setBigDecimal(2, eid);
			
			cstmt.executeUpdate();
			
			
			// Datatype mapping and functions to use with JDBC is found at https://www.tutorialspoint.com/jdbc/jdbc-data-types.htm
			int retval;
			retval = cstmt.getInt(1);
			System.out.println("Returned value is: "+retval);
			/*if (retval == true)
				System.out.println("Employee No: " + eno + " and Project No: " + pno + " are from same dept");
			else
				System.out.println("Employee No: " + eno + " and Project No: " + pno + " are in different dept");*/
			cstmt.close();
			System.out.println("Function Called successfully");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
			System.err.println(e.getClass().getName()+": "+e.getMessage());
			System.exit(0);
		}
	}

}


