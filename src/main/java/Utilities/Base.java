package Utilities;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class Base {
	public static String User="";
	public static String UserIm="";
	public static String UserFi="";
	public static File fXmlFile;
	public static Document doc;
	public static String SSpath;
	public static WebDriver driver;
	public static JavascriptExecutor jse;
	public static Statement sqlStatement;
	public static Connection myConnection;
	public static String dbURL;
	public static String strUserID;
	public static String strPassword;
	public static String db;
	public static String environment;
	public static ExtentReports extent;
	public static ExtentTest test;
	public static String ExtentReportsPath;
	public static File scrFile;
	public static File destFile;
	public static String tests[];


	public static String url = "http://www.work-around.co.il";

	public static String sign_in_xmlPath=  System.getProperty("user.dir") + "\\sign_in.xml";
	public static String xmlPath=  System.getProperty("user.dir") + "\\sanity.xml";
	public static String ReportFilePath=System.getProperty("user.dir")+ "\\report\\";
	public static String PDfPath=System.getProperty("user.dir")+ "\\PDF";
	public static String ReportImagePath=System.getProperty("user.dir")+"\\report\\images\\";


	public static String user_name= System.getProperty("user.name");

//############################################################
//########################   XML  ###########################

	//retrieving data from xml file
// nodeNumber start from 0
	public static String getData_wa(String xmlFile, String nodeName, int nodeNumber, String FileName) throws ParserConfigurationException, SAXException, IOException
	{
		switch (FileName)
		{
			case "init":
			case "sign_in":
				fXmlFile=new File(xmlFile);
				break;
			default: fXmlFile=new File("Pl insert a valid XML file name");
				break;

		}
// create the object of doc
		DocumentBuilderFactory dbFactory=DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder=dbFactory.newDocumentBuilder();
		doc=dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();

		return doc.getElementsByTagName(nodeName).item(nodeNumber).getTextContent();
	}

	public static String getData(String xmlFile,String nodeName,Integer indx) throws ParserConfigurationException, SAXException, IOException
	{
		File fXmlFile= new File(xmlFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder =dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		return doc.getElementsByTagName(nodeName).item(indx).getTextContent();

	}

	public static void setData(String xmlFile,String nodeName,Integer indx,String value) throws ParserConfigurationException, SAXException, IOException
	{
		File fXmlFile= new File(xmlFile);
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder =dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		doc.getElementsByTagName(nodeName).item(indx).setTextContent(value);

	}

	public static int getNodeCount(String xmlFile,String nodeName) throws ParserConfigurationException, SAXException, IOException
	{
		File fXmlFile= new File(xmlFile);
		int nodeCount=0;
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder =dbFactory.newDocumentBuilder();
		Document doc = dBuilder.parse(fXmlFile);
		doc.getDocumentElement().normalize();
		return nodeCount=doc.getElementsByTagName(nodeName).getLength();


	}


	public static void report_folders()
	{
		mkdir_by_path(ReportFilePath);
		mkdir_by_path(ReportImagePath);
		mkdir_by_path(PDfPath);
	}
	public static void mkdir_by_path(String path)
	{
		File theDir = new File(path);
// if the directory does not exist, create it
		if (!theDir.exists()) {
			System.out.println("creating directory: " + theDir.getName());
			boolean result = false;

			try{
				theDir.mkdir();
				result = true;
			}
			catch(SecurityException se){
				//handle it
			}
			if(result) {
				System.out.println("DIR created");
			}
		}
	}


	public static void initBrowser(String browserType) throws ParserConfigurationException, SAXException, IOException
	{
		switch (browserType.toLowerCase())
		{
			case "firefox":
				driver=initFFDriver();
				break;
			case "ie":
				driver=initIEDriver();
				break;
			case "chrome":
				driver=initChromeDriver();
				break;

		}

	}

	public static WebDriver initFFDriver() throws ParserConfigurationException, SAXException, IOException
	{
		System.setProperty("webdriver.gecko.driver",getData(xmlPath,"fireFoxPath",0));
		WebDriver driverff=new FirefoxDriver();
		return driverff;
	}

	public static WebDriver initIEDriver() throws ParserConfigurationException, SAXException, IOException
	{
		System.setProperty("webdriver.ie.driver",getData(xmlPath,"IEPath",0));
		WebDriver driverie=new InternetExplorerDriver();
		return driverie;
	}

	public static WebDriver initChromeDriver()
	{

		try {
			System.setProperty("webdriver.chrome.driver",getData(xmlPath,"ChromePath",0));

			ChromeOptions options = new ChromeOptions();

			HashMap<String, Object> chromeOptionsMap = new HashMap<String, Object>();
			chromeOptionsMap.put("plugins.plugins_disabled", new String[] {"Chrome PDF Viewer"});
			chromeOptionsMap.put("plugins.always_open_pdf_externally", true);
			options.setExperimentalOption("prefs", chromeOptionsMap);
			options.addArguments("--no-sandbox");
			chromeOptionsMap.put("download.default_directory", PDfPath);

			DesiredCapabilities cap = DesiredCapabilities.chrome();
			cap.setCapability(ChromeOptions.CAPABILITY, chromeOptionsMap);
			cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
			cap.setCapability(ChromeOptions.CAPABILITY, options);
			driver = new ChromeDriver(cap);
		}
		catch (Exception e) {
// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return driver;
	}





	public static String takeSS()
	{
		String SSpath = null;
		try
		{
			Date date = new Date();
			SimpleDateFormat sdf=new SimpleDateFormat("ddMMyyyy_HHmmss");
			String ds=sdf.format(date);
			SSpath= "images\\" + ds +".png" ;
			File scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(scrFile, new File("report\\" + SSpath));
			System.out.println(SSpath);
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		return SSpath;
	}


	public static void dbConnection(String LCC){
		try{
//step1 load the driver class
			Class.forName("oracle.jdbc.driver.OracleDriver");

//step2 create  the connection object
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@//****","****","****");

//step3 create the statement object
			Statement stmt=con.createStatement();

//step4 execute query
			ResultSet rs=stmt.executeQuery("select .........");
			ResultSetMetaData rsmt=rs.getMetaData();
			int colCount=rsmt.getColumnCount();

			while(rs.next()){
				for(int i=1; i<=colCount;i++){
					System.out.print(rs.getString(i) + " ");

				}

			}

//step5 close the connection object
			con.close();

		}catch(Exception e){

			System.out.println(e);}

	}

	public static String dbGetContainer(String containerType){


		String containerNumber = null;
		try{
//step1 load the driver class
			Class.forName("oracle.jdbc.driver.OracleDriver");

//step2 create  the connection object
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@//hfaexa-scan:1521/****","****","****");

//step3 create the statement object
			Statement stmt=con.createStatement();

//step4 execute query
			ResultSet rs=stmt.executeQuery("SELECT ........................................");
//ResultSetMetaData rsmt=rs.getMetaData();
			rs.next();
			containerNumber=rs.getString(1);
// int colCount=rsmt.getColumnCount();
// while(rs.next()){
// for(int i=1; i<=colCount;i++){
// System.out.print(rs.getString(i) + " ");
//
// }
//
// }


//step5 close the connection object
			con.close();

		}catch(Exception e){

			System.out.println(e);}





		return containerNumber;


	}


	public void web_connection_string ()
	{
		String dbURL = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS_LIST=(ADDRESS=(PROTOCOL=TCP)(HOST=******)(PORT=1521)))(CONNECT_DATA=(SERVICE_NAME=****)))";
		String strUserID = "****";
		String strPassword = "****";
		try
		{
			myConnection=DriverManager.getConnection(dbURL,strUserID,strPassword);
			sqlStatement = myConnection.createStatement();
		}
		catch (SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}




	public Connection connaction_string_sqlserver() throws ClassNotFoundException, SQLException
	{
		//Loading the required JDBC Driver class
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

		// Create a variable for the connection string.
		String connectionUrl = "jdbc:sqlserver://*****\\*****;" +
				"databaseName=****;user=****;password=****";

		// Declare the JDBC objects.
		Connection con = null;

		// Establish the connection.
		Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		con = DriverManager.getConnection(connectionUrl);

		return con;
	}

	public static void InstanceReports(String rn) throws ParserConfigurationException, SAXException, IOException
	{
		Date date = new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("ddMMyyyy_HHmmss");
		String ds=sdf.format(date);
		String reportName=rn+ " _ " + ds + ".html" ;
		extent=new ExtentReports(ReportFilePath+ reportName,true);

	}

	public static void initReportTest(String testName,String testDesc)
	{
		test=extent.startTest(testName,testDesc);
	}
	public static void finalizeReportTest()
	{
		extent.endTest(test);
	}

	public static void finalizeExtentReportTest()
	{
		extent.flush();
		extent.close();
	}

	public static void EndTest()
	{
		extent.endTest(test);
	}

	public int getNodesNumber(String nodeName) throws ParserConfigurationException, SAXException, IOException
	{
//   doc = xmlElement();
		int length = doc.getElementsByTagName(nodeName).getLength();
		return length ;
	}
//########################  END XML  ########################
//###########################################################


//############################################################
//####################   Reports & Screenshot ################

	public static void ReportsInstance (String xmlFile) throws ParserConfigurationException, SAXException, IOException
	{
// Reports - new instance
		String ReportName = "WorkaroundReport.html";

//String getData_wa( String xmlFile,String nodeName,int nodeNumber,String FileName )
		ExtentReportsPath = getData_wa( xmlFile,"ExtentReportsPath", 0,"init");
		System.out.println("Report - Start test " + ExtentReportsPath+ReportName);
		extent= new ExtentReports(ExtentReportsPath+ReportName ,true);
	}

	public static String Screenshot(String xmlFile) throws ParserConfigurationException, SAXException, IOException
	{
		String ImagePath = getData_wa(xmlFile,"ImagePath", 0,"init") + "wa.png";
		scrFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
		destFile = new File(ImagePath);
		FileUtils.copyFile(scrFile, destFile);
		return ImagePath;
	}

	public static void StartReportTest(String TestName, String Description)
	{
		test = extent.startTest(TestName,Description);
	}





}