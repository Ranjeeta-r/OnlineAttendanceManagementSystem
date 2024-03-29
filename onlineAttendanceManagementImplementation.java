package onlineAttendanceManagement.impl;

import java.util.Scanner;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import onlineAttendanceManagement.entity.Attendance;
import onlineAttendanceManagement.entity.Course;
import onlineAttendanceManagement.entity.Student;
import onlineAttendanceManagement.entity.StudentCourse;
import onlineAttendanceManagement.exceptions.InvalidDateFormatException;
public class onlineAttendanceManagementImplementation {
    public static void main(String[] args) {
        SessionFactory factory = new Configuration()
                .configure("hibernate.cfg.xml")
                .addAnnotatedClass(Student.class)
                .addAnnotatedClass(Course.class)
                .addAnnotatedClass(StudentCourse.class)
                .addAnnotatedClass(Attendance.class)
                .buildSessionFactory();

        Session session = factory.getCurrentSession();

        try {
            // Start a transaction
            session.beginTransaction();

            Scanner scanner = new Scanner(System.in);
           

            // create multiple students
            Student[] students = new Student[10];
            for (int i = 0; i < 10; i++) {
                Student student = new Student();
                student.setFirstName("Student " + (i+1));
                student.setLastName("Lastname");
                student.setEmail("student" + (i+1) + ".lastname@firstname.com");
                student.setPhoneNumber(1234567890 + i);
                student.setDateOfBirth("05/03/24");
                student.setEnrollmentDate("05/03/24");
                students[i] = student;
                session.save(students[i]);
            }

            // create two courses
            Course course1 = new Course();
            course1.setCourseName("Java Programming");
            course1.setCourseCode("JAVA");
            course1.setStartDate("05/03/24");
            course1.setEndDate("05/06/24");
            session.save(course1);

            Course course2 = new Course();
            course2.setCourseName("Python Programming");
            course2.setCourseCode("PYTHON");
            course2.setStartDate("05/03/24");
            course2.setEndDate("05/05/24");
            session.save(course2);
            
            System.out.println("Enter today's date in DD/MM/YY format");
            String date =scanner.next();
            
            if (!isValidDateFormat(date)) {
                throw new InvalidDateFormatException("Invalid date format. Please enter date in DD/MM/YY format.");
            }
            // associate the first 3 students with course1
            for (int i = 0; i < 3; i++) {
                StudentCourse studentCourse = new StudentCourse();
                studentCourse.setStudent(students[i]);
                studentCourse.setCourse(course1);
                session.save(studentCourse);
            }

            // associate the rest of the students with course2
            for (int i = 3; i < 10; i++) {
                StudentCourse studentCourse = new StudentCourse();
                studentCourse.setStudent(students[i]);
                studentCourse.setCourse(course2);
                session.save(studentCourse);
            }
          

            // record attendance for the first 3 students
            System.out.println("Enter attendance for which course?");
            System.out.println("1. Java Programming (Course 1)");
            System.out.println("2. Python Programming (Course 2)");
            System.out.print("Enter your choice (1 or 2): ");

            int courseChoice = scanner.nextInt();
            Course chosenCourse;

        

            switch (courseChoice) {
            case 1:
                chosenCourse = course1;
                // Record attendance for course1
                for (int i = 0; i < 3; i++) {
                    Attendance attendance = new Attendance();
                    attendance.setStudent(students[i]);
                    attendance.setCourse(chosenCourse);
                    attendance.setAttendanceDate(date);

                    System.out.println("Enter attendance for student " + (i + 1) + ":");
                    System.out.println("1. Present");
                    System.out.println("2. Absent");

                    int choice = scanner.nextInt();
                    String status = (choice == 1) ? "Present" : "Absent";
                    attendance.setStatus(status);

                    session.save(attendance);
                }
                break;
            case 2:
                chosenCourse = course2;
                // Record attendance for course2
                for (int i = 3; i < 10; i++) {
                    Attendance attendance = new Attendance();
                    attendance.setStudent(students[i]);
                    attendance.setCourse(chosenCourse);
                    attendance.setAttendanceDate(date);

                    System.out.println("Enter attendance for student " + (i + 1) + ":");
                    System.out.println("1. Present");
                    System.out.println("2. Absent");

                    int choice = scanner.nextInt();
                    String status = (choice == 1) ? "Present" : "Absent";
                    attendance.setStatus(status);

                    session.save(attendance);
                }
                break;
            default:
                System.out.println("Invalid choice. Attendance recording skipped.");
                chosenCourse = null; // Indicate no attendance recording
                break;
        }

  
            

            // commit the transaction
            session.getTransaction().commit();
        } catch (InvalidDateFormatException e) {
            System.out.println("Error: " + e.getMessage());
            // Handle the exception accordingly
        } finally {
            session.close();
        }
    }


//Method to validate date format (DD/MM/YY)
private static boolean isValidDateFormat(String date) {
    // Your validation logic here
    return date.matches("\\d{2}/\\d{2}/\\d{2}");
}
}