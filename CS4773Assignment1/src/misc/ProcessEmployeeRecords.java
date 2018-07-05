package misc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

public class ProcessEmployeeRecords {
	private static Scanner file;
	private static StringBuffer string;
	private static String [] firstName;
	private static String [] lastName;
	private static String [] employeeType;
	private static int [] age;
	private static double [] pay;
	
	public static String processFile(String f) {
		String fileForOpen = "data1.txt";
		string = new StringBuffer();
		int testIfFileIsNull;
		
		testIfFileIsNull = openFile(fileForOpen);
		if(testIfFileIsNull == 0) {
			return null;
		}
		
		setSizeOfEachRecord();
		
		closeFile();
		
		testIfFileIsNull = openFile(fileForOpen);
		if(testIfFileIsNull == 0) {
			return null;
		}

		int currentLine = 0;
		while(file.hasNextLine()) {
			String line = file.nextLine();
			if(line.length() > 0) {
				
				String [] words = line.split(",");
				
				testIfFileIsNull = splitLinesIntoWordsAndNumber(words, currentLine);
				if(testIfFileIsNull == 0) {
					return null;
				}
				
				currentLine++;
			}
		}
		
		if(currentLine == 0) {
			System.err.println("No records found in data file");
			closeFile();
			return null;
		}
			
		int sumOfAges = 0;
		float averageAge = 0f;
		
		int sumOfCommissionEmployees = 0;
		double sumOfCommissionEmployeesPay = 0;
		double averageCommission = 0;
		
		int sumOfHourlyEmployees = 0;
		double sumOfHourlyEmployeesPay = 0;
		double averageHourlyWage = 0;
		
		int sumOfSalaryEmployees = 0;
		double sumOfSalaryEmployeesPay = 0;
		double averageSalary = 0;
		
		for(int i = 0; i < firstName.length; i++) {
			sumOfAges += age[i];
			if(employeeType[i].equals("Commission")) {
				sumOfCommissionEmployeesPay += pay[i];
				sumOfCommissionEmployees++;
			} else if(employeeType[i].equals("Hourly")) {
				sumOfHourlyEmployeesPay += pay[i];
				sumOfHourlyEmployees++;
			} else if(employeeType[i].equals("Salary")) {
				sumOfSalaryEmployeesPay += pay[i];
				sumOfSalaryEmployees++;
			}
		}
		averageAge = (float) sumOfAges / firstName.length;
		averageCommission = sumOfCommissionEmployeesPay / sumOfCommissionEmployees;
		averageHourlyWage = sumOfHourlyEmployeesPay / sumOfHourlyEmployees;
		averageSalary = sumOfSalaryEmployeesPay / sumOfSalaryEmployees;
		
		printRowsOfInfo(string, averageAge, averageCommission, averageHourlyWage, averageSalary);
		
		comparingNamesAndPrinting(firstName, "First");
		comparingNamesAndPrinting(lastName, "Last");
	
		closeFile();
		
		return string.toString();
	}
	
	/**
	 * Will set the size of each type of record (firsName, lastName, age, employeeType, and pay)
	 * to the size of how many lines there are in the file.
	 */
	public static void setSizeOfEachRecord() {
		int numberOfLines = 0;
		
		while(file.hasNextLine()) {
			String line = file.nextLine();
			if(line.length() > 0)
				numberOfLines++;
		}
		
		firstName = new String[numberOfLines];
		lastName = new String[numberOfLines];
		age = new int[numberOfLines];
		employeeType = new String[numberOfLines];
		pay = new double[numberOfLines];
	}

	/**
	 * Will split the lines by a comma and will say which place in the line is the firstName, lastName
	 * employeeType, age, and pay. It will also call inputInfoIntoEmployeeRecords() to put the info that's in the file into each type of record 
	 * for each employee.
	 * @param words array of Strings that will tell what placement in the line each type of record is
	 * @param currentLine int of the current line number it is on
	 * @return an int to see if the file was empty or not (0 = null)
	 */
	public static int splitLinesIntoWordsAndNumber(String[] words, int currentLine) {
		int counter = 0; 
		
		for(;counter < lastName.length; counter++) {
			if(lastName[counter] == null)
				break;
					
			if(lastName[counter].compareTo(words[1]) > 0) {
				inputInfoIntoEmployeeRecords(currentLine, counter);
				break;
			}
		}
				
		firstName[counter] = words[0];
		lastName[counter] = words[1];
		employeeType[counter] = words[3];

		try {
			age[counter] = Integer.parseInt(words[2]);
			pay[counter] = Double.parseDouble(words[4]);
		} catch(Exception e) {
			printErrorMessage(e);
			closeFile();
			return 0;
		}
		return 1;
	}
	
	/**
	 * It will also call inputInfoIntoEmployeeRecords() to put the info that's in the file into each type of record 
	 * for each employee.
	 * @param currentLine int of the current line number it is on
	 * @param counter int that is the counter being used in the function that called it that uses it to keep track of what Employee
	 * it is currently on.
	 */
	public static void inputInfoIntoEmployeeRecords(int currentLine, int counter) {
		for(int i = currentLine; i > counter; i--) {
			firstName[i] = firstName[i - 1];
			lastName[i] = lastName[i - 1];
			age[i] = age[i - 1];
			employeeType[i] = employeeType[i - 1];
			pay[i] = pay[i - 1];
		}
	}
	
	/**
	 * Function responsible for printing most of the template, rows of record employees, and averages.
	 * @param string this is a String array that will hold all of the information of each employee in the
	 * correct formatt and order.
	 * @param averageAge float average number of ages of all employees
	 * @param averageCommission double average number of commission based employees
	 * @param averageHourlyWage double average number of hourly based employees
	 * @param averageSalary double average number of salary based employees
	 */
	public static void printRowsOfInfo(StringBuffer string, float averageAge, double averageCommission, double averageHourlyWage, double averageSalary) {
		string.append(String.format("# of people imported: %d\n", firstName.length));
		string.append(String.format("\n%-30s %s  %-12s %12s\n", "Person Name", "Age", "Emp. Type", "Pay"));
		
		for(int i = 0; i < 30; i++)
			string.append(String.format("-"));
		
		string.append(String.format(" ---  "));
		
		for(int i = 0; i < 12; i++)
			string.append(String.format("-"));
		
		string.append(String.format(" "));
		
		for(int i = 0; i < 12; i++)
			string.append(String.format("-"));
		
		string.append(String.format("\n"));
				
		for(int i = 0; i < firstName.length; i++) {
			string.append(String.format("%-30s %-3d  %-12s $%12.2f\n", firstName[i] + " " + lastName[i], age[i], employeeType[i], pay[i]));
		}
		
		string.append(String.format("\nAverage age:         %12.1f\n", averageAge));
		string.append(String.format("Average commission:  $%12.2f\n", averageCommission));
		string.append(String.format("Average hourly wage: $%12.2f\n", averageHourlyWage));
		string.append(String.format("Average salary:      $%12.2f\n", averageSalary));
	}
	
	/**
	 * Will compare how many people share the same first name or last name and what the name is.
	 * @param name String array that stands for either firstName or lastName.
	 * @param namePlacement String literal that represents First or Last.
	 */
	public static void comparingNamesAndPrinting(String[] name, String namePlacement) {
		HashMap<String, Integer> hm = new HashMap<String, Integer>();
		int counter = 0;
		for(int i = 0; i < name.length; i++) {
			if(hm.containsKey(name[i])) {
				hm.put(name[i], hm.get(name[i]) + 1);
				counter++;
			} else {
				hm.put(name[i], 1);
			}
		}

		string.append(String.format("\n%s names with more than one person sharing it:\n", namePlacement));
		if(counter > 0) {
			Set<String> set = hm.keySet();
			for(String str : set) {
				if(hm.get(str) > 1) {
					string.append(String.format("%s, # people with this name: %d\n", str, hm.get(str)));
				}
			}
		} else { 
			string.append(String.format("All %s names are unique", namePlacement));
		}
	}
	
	/**
	 * Will open the file or call printErrorMessage if it is not found
	 * @param fileForOpen String name of the file to open
	 * @return an int to see if the file was empty or not (0 = null)
	 */
	public static int openFile(String fileForOpen) {
		try {
			file = new Scanner(new File(fileForOpen));
		} catch (FileNotFoundException e) {
			printErrorMessage(e);
			return 0;
		}
		return 1;
	}
	
	/**
	 * Will close the file
	 */
	public static void closeFile() {
		file.close();
	}
	
	/**
	 * Will print error message
	 * @param e type of exception
	 */
	public static void printErrorMessage(Exception e) {
		System.err.println(e.getMessage());
	}
}