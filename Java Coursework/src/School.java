/**
 * Description:
 *
 * Has subjects, students, instructors, courses ArrayLists
 * Adds and Removes elements from the ArrayLists
 * Returns the ArrayLists
 * Simulates a normal day at school
 *
 * @author Georgi Spasov
 * @version 1.0 20.11.2019
 */

import java.util.ArrayList;
import java.util.Iterator;

public class School
{
    private String name;
    private ArrayList<Subject> subjects = new ArrayList<>();
    private ArrayList<Course> courses = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Instructor> instructors = new ArrayList<>();

    //Creating a constructor
    public School(String name)
    {
        this.name = name;
    }

    //Adding a student
    public void add (Student student)
    {
        this.students.add(student);
    }

    //Remove a student
    public void remove(Student student)
    {
        this.students.remove(student);
    }

    //Returning the student
    public ArrayList getStudents()
    {
        return this.students;
    }

    //Adding a subject
    public void add (Subject subject)
    {
        this.subjects.add(subject);
    }

    //Removing a subject
    public void remove(Subject subject)
    {
        this.subjects.remove(subject);
    }

    //Returning the subjects
    public ArrayList getSubjects()
    {
        return this.subjects;
    }

    //Adding a course
    public void add (Course course)
    {
        this.courses.add(course);
    }

    //Removing a course
    public void remove(Course course)
    {
        this.courses.remove(course);
    }

    //Returning the courses
    public ArrayList getCourses()
    {
        return this.courses;
    }

    //Adding an instructor
    public void add (Instructor instructor)
    {
        this.instructors.add(instructor);
    }

    //Removing an instructor
    public void remove(Instructor instructor)
    {
        this.instructors.remove(instructor);
    }

    //Returning the instructors
    public ArrayList getInstructors()
    {
        return this.instructors;
    }

    public String toString()
    {
        return "Name: "+name+"\n"+"Subjects:\n"+subjects+"\n\n"+"Courses:\n"+courses+"\n\n"+"Students:\n"+ students+"\n\n"+"Instructors: \n"+instructors+"\n\n";
    }

    //Simulating a normal day at school
    public void aDayAtSchool()
    {

        boolean kill = false;
        boolean kill2 = false;

       //Creating the courses
       for (Subject s: subjects)
       {
           for(Course c : courses)
           {
               //Checking if there is already a course with this subject which has not started
               if(s == c.getSubject() && c.getStatus() < -1)
               {
                   kill = true;
               }
           }

           //Checking if there are any students that do not have a certificate for the subject s
           for(Student st: students)
           {
               if(!st.hasCertificate(s))
               {
                   kill2 = true;
               }
           }

           //Creating a course
           if(!kill && kill2)
           {
               courses.add(new Course(s, 2));
               kill = false;
               kill2 = false;
           }
       }


       //Setting instructors to the courses
       for(Course c: courses)
       {
            for(Instructor i: instructors)
            {
                //Checking if the course has an instructor
                if(!c.hasInstructor())
                {
                    //Checking if the instructor is assigned
                    if(!i.isAssigned())
                    {
                        //Setting the instructor to the course
                        c.setInstructor(i);

                       //break;
                    }
                }
            }
       }

       //Enrolling students to the courses
       for(Student st: students)
       {
           for(Course c: courses)
           {
               //Checking if the student is already assigned to a course and if the course has an instructor
               if(!st.isAssigned && c.hasInstructor())
               {
                   //Checking if the course is full and if the student already completed this course
                   if(c.getSize()<3 && !st.hasCertificate(c.getSubject()))
                   {
                       //Enrolling the student
                       c.enrolStudent(st);
                       //break;
                   }
               }
           }
       }

       //Advancing to the next day
       for(Course c: courses)
       {
           c.aDayPasses();
       }

        Iterator<Course> it = courses.iterator();

       //Removing finished or cancelled courses
       while(it.hasNext())
       {
           Course it1 = it.next();
           //Checking if the course has finished or was cancelled
           if(it1.getStatus() == 0 || it1.isCancelled())
           {
               //Removing the course
               it.remove();
           }
       }
    }
}
