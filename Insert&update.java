import java.util.*;
import java.sql.*;
public class Covid{
public void insert() {  
	Scanner sc = new Scanner(System.in);
	try{  
	Class.forName("com.mysql.jdbc.Driver");  
	Connection con=DriverManager.getConnection(  
	"jdbc:mysql://localhost:3306/covid","root","myfavehazard");
	System.out.println("Enter values to patients table");
	int pid,age,did;
	String status,area,district,state;
	System.out.println("Enter pid:");
	pid=sc.nextInt();
	System.out.println("Enter age:");
	age=sc.nextInt();
	System.out.println("Enter Status:");
	status=sc.next();
	System.out.println("Enter Area:");
	area=sc.next();
	System.out.println("Enter District:");
	district=sc.next();
	System.out.println("Enter State:");
	state=sc.next();
	System.out.println("Enter District_id:");
	did=sc.nextInt();
	PreparedStatement stmt=con.prepareStatement("Insert into Patients values(?,?,?,?,?,?,?)");  
	stmt.setInt(1, pid);
	stmt.setInt(2, age);
	stmt.setString(3, status);
	stmt.setString(4, area);
	stmt.setString(5, district);
	stmt.setString(6, state);
	stmt.setInt(7, did);

	int i=stmt.executeUpdate();  
	System.out.println(i+" records inserted");
	Statement s = con.createStatement();
	s.executeUpdate("Delete from District;");
	s.executeUpdate("Insert into District(did,pid,Area,Status,Disrict) Select did,pid,Area,Status,District from Patients ORDER BY did ASC;");
	s.executeUpdate("Delete from State;");
	s.executeUpdate("Insert into State(did,Total_patients,District) Select distinct did,count(did),Disrict from district where district.did=district.did GROUP BY did;");
	s.executeUpdate("Update State set Active = (Select count(*) from district where state.did=district.did AND Status='Active' GROUP BY did);");
	s.executeUpdate("Update State set Recovered = (Select count(*) from district where state.did=district.did AND Status='Recovered' GROUP BY did);");
	s.executeUpdate("Update State set Deaths = (Select count(*) from district where state.did=district.did AND Status='Death' GROUP BY did);");
	s.executeUpdate("Delete from Containment;");
	s.executeUpdate("Insert into Containment(did,District) Select distinct did,Disrict from district where district.did=district.did GROUP BY did;");
	s.executeUpdate("Update Containment set Total_Areas = (Select count(DISTINCT Area) from district where containment.did=district.did GROUP BY did);");
	s.executeUpdate("Update Containment set Active = (Select count(*) from district where containment.did=district.did AND Status='Active' GROUP BY did);");
	con.close();  
	}catch(Exception e){ System.out.println(e);}  
}

public void Change_Status()
{
	try{  
		Class.forName("com.mysql.jdbc.Driver");  
		Connection con=DriverManager.getConnection(  
		"jdbc:mysql://localhost:3306/covid","root","myfavehazard");
		
		Scanner sc = new Scanner(System.in);
		int pid;
		String stat;
		System.out.println("Enter pid of Patient:");
		pid=sc.nextInt();
		System.out.println("Enter status of "+pid+" Patient:");
		stat=sc.next();
		Statement s = con.createStatement();
		s.executeUpdate("Update Patients set Status = '"+stat+"' where Patients.pid="+pid+";");
		s.executeUpdate("Delete from District;");
		s.executeUpdate("Insert into District(did,pid,Area,Status,Disrict) Select did,pid,Area,Status,District from Patients ORDER BY did ASC;");
		s.executeUpdate("Delete from State;");
		s.executeUpdate("Insert into State(did,Total_patients,District) Select distinct did,count(did),Disrict from district where district.did=district.did GROUP BY did;");
		s.executeUpdate("Update State set Active = (Select count(*) from district where state.did=district.did AND Status='Active' GROUP BY did);");
		s.executeUpdate("Update State set Recovered = (Select count(*) from district where state.did=district.did AND Status='Recovered' GROUP BY did);");
		s.executeUpdate("Update State set Deaths = (Select count(*) from district where state.did=district.did AND Status='Death' GROUP BY did);");
		s.executeUpdate("Delete from Containment;");
		s.executeUpdate("Insert into Containment(did,District) Select distinct did,Disrict from district where district.did=district.did GROUP BY did;");
		s.executeUpdate("Update Containment set Total_Areas = (Select count(DISTINCT Area) from district where containment.did=district.did GROUP BY did);");
		s.executeUpdate("Update Containment set Active = (Select count(*) from district where containment.did=district.did AND Status='Active' GROUP BY did);");
		con.close();
	}catch(Exception e){ System.out.println(e);}
}

public void display_Patients()
{
	try{  
		Class.forName("com.mysql.jdbc.Driver");  
		Connection con=DriverManager.getConnection(  
		"jdbc:mysql://localhost:3306/covid","root","myfavehazard");
		Statement s = con.createStatement();
		ResultSet rs= s.executeQuery("Select * from Patients");
			System.out.println("Patients_id        Age        Status                                             Address");
			System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
		while(rs.next())  
			System.out.println("     "+rs.getInt(1)+"             "+rs.getInt(2)+"        "+rs.getString(3)+"                   "+rs.getString(4)+" , "+rs.getString(5)+" ,District_id:"+rs.getInt(7)+" , "+rs.getString(6));
		System.out.println("-----------------------------------------------------------------------------------------------------------------------------------");
		con.close();
	}catch(Exception e)
    { System.out.println(e); }
}

public void display_District()
{
	try{  
		Class.forName("com.mysql.jdbc.Driver");  
		Connection con=DriverManager.getConnection(  
		"jdbc:mysql://localhost:3306/covid","root","myfavehazard");
		Statement s = con.createStatement();
		System.out.println("District table");
		ResultSet rns= s.executeQuery("Select * from District");
		System.out.println("District_id         Patients_id         Status                  Address");
		System.out.println("-----------------------------------------------------------------------------------------------------------------------");
		while(rns.next())  
			System.out.println("    "+rns.getInt(1)+"                   "+rns.getInt(2)+"              "+rns.getString(4)+"            "+rns.getString(3)+" , "+rns.getString(5));
		System.out.println("-----------------------------------------------------------------------------------------------------------------------");
		con.close();
	}catch(Exception e)
    { System.out.println(e); }
}

public void display_State()
{
	try{  
		Class.forName("com.mysql.jdbc.Driver");  
		Connection con=DriverManager.getConnection(  
		"jdbc:mysql://localhost:3306/covid","root","myfavehazard");
		Statement s = con.createStatement();
		System.out.println("State table");
		ResultSet rnss= s.executeQuery("Select * from State");
		System.out.println("District_id         Total_Patients       Active        Recovered           Deaths              District");
		System.out.println("-----------------------------------------------------------------------------------------------------------------------");
		while(rnss.next())  
			System.out.println("     "+rnss.getInt(1)+"                   "+rnss.getInt(2)+"                "+rnss.getInt(3)+"             "+rnss.getInt(4)+"                 "+rnss.getInt(5)+"                   "+rnss.getString(6));
		System.out.println("-----------------------------------------------------------------------------------------------------------------------");
		con.close();
	}catch(Exception e)
    { System.out.println(e); }
}

public void display_Con()
{
	try{  
		Class.forName("com.mysql.jdbc.Driver");  
		Connection con=DriverManager.getConnection(  
		"jdbc:mysql://localhost:3306/covid","root","myfavehazard");
		Statement s = con.createStatement();
		System.out.println("Containment Table");
		ResultSet rnsss= s.executeQuery("Select * from Containment");
		System.out.println("Total_Areas       Total_Active     Distirct_id         District");
		System.out.println("----------------------------------------------------------------------------------------");
		while(rnsss.next())  
			System.out.println("    "+rnsss.getInt(1)+"               "+rnsss.getInt(2)+"                "+rnsss.getInt(3)+"                 "+rnsss.getString(4)+"  ");
		System.out.println("-----------------------------------------------------------------------------------------");
		con.close();
	}catch(Exception e)
    { System.out.println(e); }
}

public static void main(String[] args)
	{
		Scanner sc = new Scanner(System.in);
		Covid c = new Covid();
		int ch,ch1;
		System.out.println("---------------Covid-19 DataBase----------------");
		
		System.out.println("Enter your choice:");
		System.out.println("Enter 1 to add new Patient to the table.\nEnter 2 to Change Status of Patient.\nEnter 3 to View CoronaVirus Table.\nEnter 4 to Quit\n");
		ch=sc.nextInt();
		while(ch!=4)
		{
			
			if(ch==1)
			{
				c.insert();
			}
			else if(ch==2)
			{
				c.Change_Status();
			}
			else if(ch==3)
			{
				System.out.println("Enter 1 for Patients table. Enter 2 for Districts table. Enter 3 for State table. Enter 4 for Containment Table");
				ch1=sc.nextInt();
				if(ch1==1)
				{ 
					c.display_Patients();
				}
				else if(ch1==2)
				{
					c.display_District();
				}
				else if(ch1==3)
				{
					c.display_State();
				}
				else if(ch1==4)
				{
					c.display_Con();
				}
			}
			System.out.println("Enter 1 to add new Patient to the table.\nEnter 2 to Change Status of Patient.\nEnter 3 to View CoronaVirus Table.\nEnter 4 to Quit\n");
			ch=sc.nextInt();
		}
		
	}
	
}

