package controllers;

import com.sun.corba.se.impl.orb.PrefixParserData;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import static jdk.nashorn.internal.objects.NativeString.trim;

public class DataManager {

    public DataManager() { }


    /* Connects to database. Login credentials must be valid for MYSQL at localhost. */
    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Main.conn = DriverManager.getConnection(Main.CONNECTION_STRING, "tommy", "vfgtyxk37sql");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /* Creates table.
     * MUST be connected to database before method invocation.
     * MUST successfuly connect to student_mysql.sql, in the working directory of the project. (currently under schemas directory)
     *
     */
    public boolean createTables() {
        try {
            if (Main.conn == null) {
                System.out.println("\nConnection not created! Check that you are connected to the database.\n");
                return false;
            }

            DatabaseMetaData dbm = Main.conn.getMetaData();

            boolean createdAnything = true;

            // Check if Conditions table exists
            ResultSet tables = dbm.getTables(null, null, "Conditions", null);
            if (tables.next()) {
                System.out.println("Conditions table already exists, so we will keep the data!\n");

                createdAnything = false;
            } else {
                BufferedReader br = new BufferedReader(new FileReader("schemas/condition_mysql.sql"));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        Statement stmt = Main.conn.createStatement();
                        stmt.execute(line);
                    }
                }
                System.out.println("Conditions table created, with absolutely no data!\n");
            }

            // Check if Locations table exists
            tables = dbm.getTables(null, null, "Locations", null);
            if (tables.next()) {
                System.out.println("Locations table already exists, so we will keep the data!\n");

                createdAnything = false;
            } else {
                BufferedReader br = new BufferedReader(new FileReader("schemas/location_mysql.sql"));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        Statement stmt = Main.conn.createStatement();
                        stmt.execute(line);
                    }
                }
                System.out.println("Locations table created, with absolutely no data!\n");
            }

            // Check if Patients table exists
            tables = dbm.getTables(null, null, "Patients", null);
            if (tables.next()) {
                System.out.println("Patients table already exists, so we will keep the data!\n");

                createdAnything = false;
            } else {
                BufferedReader br = new BufferedReader(new FileReader("schemas/patient_mysql.sql"));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        Statement stmt = Main.conn.createStatement();
                        stmt.execute(line);
                    }
                }
                System.out.println("Patients table created, with absolutely no data!\n");
            }

            // Check if Patient_Phones table exists
            tables = dbm.getTables(null, null, "Patient_Phones", null);
            if (tables.next()) {
                System.out.println("Patient_Phones table already exists, so we will keep the data!\n");

                createdAnything = false;
            } else {
                BufferedReader br = new BufferedReader(new FileReader("schemas/patient_phone_mysql.sql"));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        Statement stmt = Main.conn.createStatement();
                        stmt.execute(line);
                    }
                }
                System.out.println("Patient_Phones table created, with absolutely no data!\n");
            }

            // Check if Prescriptions table exists
            tables = dbm.getTables(null, null, "Prescriptions", null);
            if (tables.next()) {
                System.out.println("Prescriptions table already exists, so we will keep the data!\n");

                createdAnything = false;
            } else {
                BufferedReader br = new BufferedReader(new FileReader("schemas/prescriptions_mysql.sql"));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        Statement stmt = Main.conn.createStatement();
                        stmt.execute(line);
                    }
                }
                System.out.println("Prescriptions table created, with absolutely no data!\n");
            }

            // Check if Hospitals table exists
            tables = dbm.getTables(null, null, "Hospitals", null);
            if (tables.next()) {
                System.out.println("Hospitals table already exists, so we will keep the data!\n");

                createdAnything = false;
            } else {
                BufferedReader br = new BufferedReader(new FileReader("schemas/hospitals.sql"));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        Statement stmt = Main.conn.createStatement();
                        stmt.execute(line);
                    }
                }
                System.out.println("Hospitals table created, with absolutely no data!\n");
            }

            // Check if Doctors table exists
            tables = dbm.getTables(null, null, "Doctors", null);
            if (tables.next()) {
                System.out.println("Doctors table already exists, so we will keep the data!\n");

                createdAnything = false;
            } else {
                BufferedReader br = new BufferedReader(new FileReader("schemas/doctor_mysql.sql"));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        Statement stmt = Main.conn.createStatement();
                        stmt.execute(line);
                    }
                }
                System.out.println("Doctors table created, with absolutely no data!\n");
            }

            // Check if DoctorToPatient table exists
            tables = dbm.getTables(null, null, "DoctorToPatient", null);
            if (tables.next()) {
                System.out.println("DoctorToPatient table already exists, so we will keep the data!\n");

                createdAnything = false;
            } else {
                BufferedReader br = new BufferedReader(new FileReader("schemas/doctor_patient_mysql.sql"));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        Statement stmt = Main.conn.createStatement();
                        stmt.execute(line);
                    }
                }
                System.out.println("DoctorToPatient table created, with absolutely no data!\n");
            }

            // Check if CreditCards table exists
            tables = dbm.getTables(null, null, "CreditCards", null);
            if (tables.next()) {
                System.out.println("CreditCards table already exists, so we will keep the data!\n");

                createdAnything = false;
            } else {
                BufferedReader br = new BufferedReader(new FileReader("schemas/creditcards.sql"));
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.length() > 0) {
                        Statement stmt = Main.conn.createStatement();
                        stmt.execute(line);
                    }
                }
                System.out.println("CreditCards table created, with absolutely no data!\n");
            }

            // Check if PatientData view exists
            tables = dbm.getTables(null, null, "vPatientData", null);
            if (tables.next()) {
                System.out.println("vPatientData view already exists, so we will keep the data!\n");

                createdAnything = false;
            } else {
                BufferedReader br = new BufferedReader(new FileReader("schemas/patient_data_view.sql"));
                String query = "";
                String line;
                while ( (line = br.readLine()) != null) {
                    if (!line.contains(";")) {
                        query += line;
                    }
                    else if (line.length() > 0) {
                        query += line;

                        Statement stmt = Main.conn.createStatement();
                        stmt.execute(query);
                    }
                }
                System.out.println("PatientData view created, with absolutely no data!\n");
            }

            // Check if HospitalData view exists
            tables = dbm.getTables(null, null, "vHospitalData", null);
            if (tables.next()) {
                System.out.println("vHospitalData view already exists, so we will keep the data!\n");

                createdAnything = false;
            } else {
                BufferedReader br = new BufferedReader(new FileReader("schemas/hospital_data_view.sql"));
                String query = "";
                String line;
                while ( (line = br.readLine()) != null) {
                    if (!line.contains(";")) {
                        query += line;
                    }
                    else if (line.length() > 0) {
                        query += line;

                        Statement stmt = Main.conn.createStatement();
                        stmt.execute(query);
                    }
                }
                System.out.println("vHospitalData view created, with absolutely no data!\n");
            }

            // Check if vDoctorData view exists
            tables = dbm.getTables(null, null, "vDoctorData", null);
            if (tables.next()) {
                System.out.println("vDoctorData view already exists, so we will keep the data!\n");

                createdAnything = false;
            } else {
                BufferedReader br = new BufferedReader(new FileReader("schemas/doctor_data_view.sql"));
                String query = "";
                String line;
                while ( (line = br.readLine()) != null) {
                    if (!line.contains(";")) {
                        query += line;
                    }
                    else if (line.length() > 0) {
                        query += line;

                        Statement stmt = Main.conn.createStatement();
                        stmt.execute(query);
                    }
                }
                System.out.println("vDoctorData view created, with absolutely no data!\n");
            }
            return createdAnything;
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        return false;

    }

    public void generateData() {
        try {
            DatabaseMetaData dbm = Main.conn.getMetaData();

            // Must match whatever file generated by DataGenerator.php
            FileReader demographics = new FileReader("demographic_data.csv");
            FileReader hospitals = new FileReader("specific_data.csv");

            Iterable<CSVRecord> patientRecords = CSVFormat.EXCEL.parse(demographics);
            Iterable<CSVRecord> hospitalRecords = CSVFormat.EXCEL.parse(hospitals);

            // Just to show user that it's doing work
            System.out.println("Loading...\n\n");

            ArrayList<String> prescriptionNames = new ArrayList<String>(1000);
            ArrayList<String> prescriptionDrugs = new ArrayList<String>(1000);
            ArrayList<Integer> conditionIds = new ArrayList<Integer>(1000);

            for (CSVRecord hospitalRecord : hospitalRecords) {
                String prescriptionName = trim(hospitalRecord.get(0));
                String prescriptionDrug = trim(hospitalRecord.get(1));
                String generalDiagnosis = trim(hospitalRecord.get(2));
                String specificDiagnosis = trim(hospitalRecord.get(3));
                int severity = (int)(Math.random() * 10);

                /* Get auto generated ConditionId so that it can be used as foreign key */
                PreparedStatement st = Main.conn.prepareStatement("INSERT INTO Conditions (GeneralDiagnosis, SpecificDiagnosis, Severity)"
                        + "VALUES (?, ?, ?)", new String[]{"ConditionId"});
                st.setString(1, generalDiagnosis);
                st.setString(2, specificDiagnosis);
                st.setInt(3, severity);

                int result = st.executeUpdate();

                if (result == -1)
                {
                    System.out.println("We apologize, but the insert statement was unsuccessful. The data provided is invalid.");
                    break;
                }

                ResultSet keys = st.getGeneratedKeys();

                if (keys.next()) {
                    conditionIds.add(keys.getInt(1));
                }

                prescriptionNames.add(prescriptionName);
                prescriptionDrugs.add(check(prescriptionDrug));
            }

            int i = 0;
            for (CSVRecord patientRecord : patientRecords) {

                // Trim to get rid of whitespace from each attribute
                String patientName = trim(patientRecord.get(0));
                String patientAddress = trim(patientRecord.get(1));
                String patientState = trim(patientRecord.get(2));
                String patientPostCode = trim(patientRecord.get(3));
                String patientMobilePhoneNum = trim(patientRecord.get(4));
                String patientHomePhoneNum = trim(patientRecord.get(5));
                String patientICEPhoneNum = trim(patientRecord.get(6));
                String hospitalPhoneNum = trim(patientRecord.get(7));
                String hospitalTollFreePhoneNum = trim(patientRecord.get(8));
                String hospitalAddress = trim(patientRecord.get(9));
                String hospitalState = trim(patientRecord.get(10));
                String hospitalPostCode = trim(patientRecord.get(11));
                String doctorName = trim(patientRecord.get(12));
                String doctorPhone = trim(patientRecord.get(13));
                String patientCreditCardType = trim(patientRecord.get(14));
                String patientCreditCardNum = trim(patientRecord.get(15));
                String patientCreditCardExpDate = trim(patientRecord.get(16));
                String prescriptionName = prescriptionNames.get(i);
                String prescriptionDrug = prescriptionDrugs.get(i);
                int conditionId = conditionIds.get(i);
                String hospitalLastName = trim(patientRecord.get(17));
                String hospitalName = generateHospitalName(hospitalLastName);

                /* Get auto generated LocationId so that it can be used as foreign key */
                PreparedStatement st = Main.conn.prepareStatement("INSERT INTO Locations (Address, State, PostCode)"
                        + "VALUES (?, ?, ?)", new String[]{"LocationId"});
                st.setString(1, patientAddress);
                st.setString(2, patientState);
                st.setString(3, patientPostCode);

                int result = st.executeUpdate();

                if (result == -1) {
                    System.out.println("We apologize, but the insert statement was unsuccessful. The data provided is invalid.");
                    break;
                }

                ResultSet keys = st.getGeneratedKeys();

                int patientLocationId = -1;
                if (keys.next()) {
                    patientLocationId = keys.getInt(1);
                }

                /* Get auto generated LocationId so that it can be used as foreign key */
                st = Main.conn.prepareStatement("INSERT INTO Locations (Address, State, PostCode)"
                        + "VALUES (?, ?, ?)", new String[]{"LocationId"});
                st.setString(1, hospitalAddress);
                st.setString(2, hospitalState);
                st.setString(3, hospitalPostCode);

                result = st.executeUpdate();

                if (result == -1) {
                    System.out.println("We apologize, but the insert statement was unsuccessful. The data provided is invalid.");
                    break;
                }

                keys = st.getGeneratedKeys();

                int hospitalLocationId = -1;
                if (keys.next()) {
                    hospitalLocationId = keys.getInt(1);
                }

                /* Get auto generated CreditCardId so that it can be used as foreign key */
                st = Main.conn.prepareStatement("INSERT INTO CreditCards (CreditCardNum, CreditCardType, CreditCardExpType)" +
                        "VALUES (?, ?, ?)", new String[]{"CreditCardId"});
                st.setString(1, patientCreditCardNum);
                st.setString(2, patientCreditCardType);
                st.setString(3, patientCreditCardExpDate);

                result = st.executeUpdate();

                if (result == -1) {
                    System.out.println("We apologize, but the insert statement was unsuccessful. The data provided is invalid.");
                    break;
                }

                keys = st.getGeneratedKeys();

                int creditCardId = -1;
                if (keys.next()) {
                    creditCardId = keys.getInt(1);
                }

                /* Get auto generated HospitalId so that it can be used as foreign key */
                st = Main.conn.prepareStatement("INSERT INTO Hospitals (Name, LocationId, PhoneNum, TollFreePhoneNum)"
                        + "VALUES (?, ?, ?, ?)", new String[]{"HospitalId"});
                st.setString(1, hospitalName);
                st.setInt(2, hospitalLocationId);
                st.setString(3, hospitalPhoneNum);
                st.setString(4, hospitalTollFreePhoneNum);

                result = st.executeUpdate();

                if (result == -1) {
                    System.out.println("We apologize, but the insert statement was unsuccessful. The data provided is invalid.");
                    break;
                }

                keys = st.getGeneratedKeys();

                int hospitalId = -1;
                if (keys.next()) {
                    hospitalId = keys.getInt(1);
                }

                /* Get auto generated DoctorId so that it can be used as foreign key */
                st = Main.conn.prepareStatement("INSERT INTO Doctors (HospitalId, Name, Phone)"
                        + "VALUES (?, ?, ?)", new String[]{"DoctorId"});
                st.setInt(1, hospitalId);
                st.setString(2, doctorName);
                st.setString(3, doctorPhone);

                result = st.executeUpdate();

                if (result == -1) {
                    System.out.println("We apologize, but the insert statement was unsuccessful. The data provided is invalid.");
                    break;
                }

                keys = st.getGeneratedKeys();

                int doctorId = -1;
                if (keys.next()) {
                    doctorId = keys.getInt(1);
                }

                /* Get auto generated PatientId so that it can be used as foreign key */
                st = Main.conn.prepareStatement("INSERT INTO Patients (Name, ConditionId, LocationId, CreditCardId)"
                        + "VALUES (?, ?, ?, ?)", new String[]{"PatientId"});
                st.setString(1, patientName);
                st.setInt(2, conditionId);
                st.setInt(3, patientLocationId);
                st.setInt(4, creditCardId);

                result = st.executeUpdate();

                if (result == -1) {
                    System.out.println("We apologize, but the insert statement was unsuccessful. The data provided is invalid.");
                    break;
                }

                keys = st.getGeneratedKeys();

                int patientId = -1;
                if (keys.next()) {
                    patientId = keys.getInt(1);
                }

                /* Get auto generated PhoneId so that it can be used as foreign key */
                st = Main.conn.prepareStatement("INSERT INTO Patient_Phones (PatientId, MobilePhone, HomePhone, ICEPhone)" +
                        "VALUES (?, ?, ?, ?)", new String[]{"PhoneId"});
                st.setInt(1, patientId);
                st.setString(2, patientMobilePhoneNum);
                st.setString(3, patientHomePhoneNum);
                st.setString(4, patientICEPhoneNum);

                result = st.executeUpdate();

                if (result == -1) {
                    System.out.println("We apologize, but the insert statement was unsuccessful. The data provided is invalid.");
                    break;
                }

                keys = st.getGeneratedKeys();

                int phoneId = -1;
                if (keys.next()) {
                    phoneId = keys.getInt(1);
                }


                if (prescriptionName.length() > 254) {
                    prescriptionName = prescriptionName.substring(0, 255);
                }

                if (prescriptionDrug.length() > 254) {
                    prescriptionDrug = prescriptionDrug.substring(0, 255);
                }

                st = Main.conn.prepareStatement("INSERT INTO Prescriptions (PatientId, PrescriptionName, PrescriptionDrug)" +
                        "VALUES (?, ?, ?)");
                st.setInt(1, patientId);
                st.setString(2, prescriptionName);
                st.setString(3, prescriptionDrug);

                result = st.executeUpdate();

                if (result == -1) {
                    System.out.println("We apologize, but the insert statement was unsuccessful. The data provided is invalid.");
                    break;
                }

                ++i;        // Keep track of which record we're on
            }

            System.out.println("Data inputted into the database! Thank you so much for using our system!");
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
            System.out.println("Something went wrong with SQL!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Something went wrong because a file was not found!");
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Something went wrong with files in general!");
        }
    }

    public void exportPatientData()
    {
        try {
            String query = "SELECT * from vPatientData";

            PreparedStatement statement = Main.conn.prepareStatement(query);

            ResultSet rs = statement.executeQuery();

            // Gets patient data and appends date to the end so there can be multiple copies
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String filename = "patient_data_" + dtf.format(LocalDate.now()) + ".csv";

            BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename));

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("ID", "Name", "Address", "Condition"));

            while (rs.next()) {

                csvPrinter.printRecord(Arrays.asList(rs.getInt("PatientId"), rs.getString("Name"),
                        rs.getString("Address"), rs.getString("GeneralDiagnosis")));
            }
            csvPrinter.flush();
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void exportHospitalData()
    {
        try {
            String query = "SELECT * from vHospitalData";

            PreparedStatement statement = Main.conn.prepareStatement(query);

            ResultSet rs = statement.executeQuery();

            // Gets hospital data and appends date to the end so there can be multiple copies
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String filename = "hospital_data_" + dtf.format(LocalDate.now()) + ".csv";

            BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename));

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("ID", "Name", "Toll-Free Phone Number", "Address", "Doctor Name"));

            while (rs.next()) {

                csvPrinter.printRecord(Arrays.asList(rs.getInt("HospitalId"), rs.getString("hosName"),
                       rs.getString("TollFreePhoneNum"), rs.getString("Address")));
            }
            csvPrinter.flush();
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Exports data from Doctor View
    public void exportDoctorData()
    {
        try {
            String query = "SELECT * from vDoctorData";

            PreparedStatement statement = Main.conn.prepareStatement(query);

            ResultSet rs = statement.executeQuery();

            // Gets doctor data and appends date to the end so there can be multiple copies
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String filename = "doctor_data_" + dtf.format(LocalDate.now()) + ".csv";

            BufferedWriter writer = Files.newBufferedWriter(Paths.get(filename));

            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                    .withHeader("ID", "Name", "Phone", "Hospital Name"));

            // For each CSV record, prints it to the file
            while (rs.next()) {

                csvPrinter.printRecord(Arrays.asList(rs.getInt("DoctorId"), rs.getString("Name"),
                        rs.getString("Phone"), rs.getString("hosName")));
            }
            csvPrinter.flush();
        } catch (IOException ioe)
        {
            ioe.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Generates hospital names for the data generation
    private String generateHospitalName(String hospitalLastName) {
        int rand = (int) (Math.random() * 8) + 1;
        String hospitalName = hospitalLastName;
        if (rand == 1) {
            hospitalName += " General Hospital";
        } else if (rand == 2) {
            hospitalName += " Medical Center";
        } else if (rand == 3) {
            hospitalName += " Family Practice";
        } else if (rand == 4) {
            hospitalName += " Chiropractice";
        } else if (rand == 5) {
            hospitalName += " Dental Office";
        } else if (rand == 6) {
            hospitalName += " Hospital";
        } else if (rand == 7) {
            hospitalName += " Children's Hospital";
        } else if (rand == 8) {
            hospitalName += " and Friends Hospital";
        }
        return hospitalName;
    }

    // Checks for unnecessary commas (which would ruin the comma separated file)
    private String check(String prescriptionDrug) {
        return prescriptionDrug.replace(',', ' ');

    }
}
