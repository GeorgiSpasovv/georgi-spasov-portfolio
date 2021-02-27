/**
 * Description:
 *
 * Has subject, students, instructor
 * Returns the status of the course
 * Advances one day
 * Enrolls students
 * Sets instructor
 *
 * @author Georgi Spasov
 * @version 1.0 18.11.2019
 */

import java.util.Arrays;

public class Course
{
    private Subject subject;
    private int daysUntilStarts;
    private int daysToRun;
    private Student[] students;
    private Instructor instructor;
    private boolean cancelled;

    //Creating a constructor that initialises the properties
    public Course(Subject subject, int daysUntilStarts)
    {
        this.subject = subject;
        this.daysUntilStarts = daysUntilStarts;
        this.students = new Student[3];
        this.daysToRun = this.subject.getDuration();
    }

    //Returning the subject
    public Subject getSubject()
    {
        return this.subject;
    }

    //Returning the status of the course
    public int getStatus()
    {
        //Checking if the course hasn't started
        if(daysUntilStarts > 0)
        {
            int days = - daysUntilStarts;
            return days;
        }
        //Checking if the course has started
        else if(daysToRun > 0)
        {
            return daysToRun;
        }
        //Returning 0 if ti has finished
        else return 0;

    }

    //Advancing one day for the course
    public void aDayPasses()
    {
            //Checking if the course hasn't started
            if(daysUntilStarts > 0)
            {
                this.daysUntilStarts--;
            }
            //Checking if the course has started
            else if(daysToRun > 0)
            {
                this.daysToRun--;
            }

            //Checking if the course has finished
            if(daysToRun == 0)
            {
                //Graduating the students;
                for(Student s: getStudents())
                {
                    s.graduate(this.getSubject());
                    //s = null;
                    s.isAssigned = false;
                }
                //Unassigning the instructor
                this.instructor.unassignCourse();

            }

            //Checking if the course is about to start
            if(daysUntilStarts == 0)
            {
                //Checking if the course has instructor or students
                if(!this.hasInstructor() || this.getSize() == 0)
                {
                    //cancelling the course
                    this.cancelled = true;
                }

                if(this.isCancelled())
                {
                    //Checking if the course had an instructor and unassigns him
                    if(this.hasInstructor())
                    {
                        this.instructor.unassignCourse();
                    }

                    //Checking if the course had students and releases them
                    if(getSize()>0)
                    {
                        for(int i= 0; i<this.getSize();i++)
                        {
                            students[i].isAssigned = false;
                            students[i] = null;

                        }
                    }
                }
              }
    }

    //Enrolling the students
    public boolean enrolStudent (Student student)
    {
        //Checks if the course has already started
        if(daysUntilStarts <= 0)
        {
            return false;
        }

        //Checks if the course is full
        else if (this.getSize() == 3)
        {
            return false;
        }

        //Enrolls the student
        else
        {
            student.isAssigned = true;
            students[this.getSize()] = student;

            return true;
        }


    }

    //Getting the number of students in the course
    public int getSize()
    {
        int size = 0;

        for (Student s: students)
        {
            if (s != null)
            {
                size++;
            }
        }
        return size;
    }

    //Returning the students
    public Student[] getStudents()
    {
        Student[] st = new Student[this.getSize()];

        if(this.getSize() != 0)
        {
            for (int i = 0; i < this.getSize(); i++)
            {
                st[i] = students[i];
            }
        }
        return st;

    }

    //Sets an instructor to the course
    public boolean setInstructor(Instructor instructor)
    {
        //Checks if the instructor can teach the subject
        if(instructor.canTeach(this.getSubject()))
        {
            instructor.assignCourse(this);
            this.instructor = instructor;
            return true;
        }
        return false;
    }

    //Checks if the course has instructor
    public boolean hasInstructor()
    {
        if(this.instructor != null)
        {
            return true;
        }
        return false;
    }

    /*
    public Instructor getInstructor()
    {
        return this.instructor;
    }
*/

    //Checks if the course is cancelled
    public boolean isCancelled()
    {
        if(cancelled)
        {
            return true;
        }
        return false;
    }

    //Returning all properties of the course
    public String toString()
    {
        return "\nCourse\nSubject: "+getSubject()+"\n"+"DaysUntilStart: "+daysUntilStarts+"\n"+"DaysToRun: "+daysToRun+"\n"+"Status: "+ getStatus()+"\n"+"Size: "+getSize()+"\n"+"Students: \n"+Arrays.toString(this.getStudents())+"\n"+"Instructor: "+hasInstructor()+"\n"+"Cancelled: "+isCancelled() + "\n***********************************\n";
    }
}
