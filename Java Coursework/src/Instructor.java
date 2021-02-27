/**
 * Description:
 * Subclass of Person
 * Assigns a course to the object
 * Unassigns the course from the object
 * Returns the course
 *
 * @author Georgi Spasov
 * @version 1.0 17.11.2019
 */


public abstract class Instructor extends Person
{
    protected Course assignedCourse;

    //Creating a constructor
    public Instructor(String name, char gender, int age)
    {
        super(name, gender, age);
    }

    //Assigning a course to the instructor
    public void assignCourse(Course course)
    {
        this.assignedCourse = course;
    }

    //Checking if the instructor is assigned to a course
    public boolean isAssigned()
    {
        if(this.assignedCourse != null)
        {
            return true;
        }
        return false;
    }

    //Unassign the instructor from the course
    public void unassignCourse()
    {
        this.assignedCourse = null;
    }

    //Getting the instructor's course
    public Course getAssignedCourse()
    {
        return this.assignedCourse;
    }

    //Creating an abstract method
    public abstract boolean canTeach(Subject subject);

    public String toString()
    {
        return "\nName: "+name+"\n"+"Gender: "+gender+"\n"+"Age: "+age+"\n"+"Assigned Course: \n"+ getAssignedCourse()+"\n*********************\n";
    }
}
