/**
 * Description:
 * Subclass of Instructor
 * Can teach subjects with specialism 1, 2
 * Checks if the object can teach a certain object
 *
 * @author Georgi Spasov
 * @version 1.0 17.11.2019
 */

public class Teacher extends Instructor
{
    //Creating a constructor
    public Teacher(String name, char gender, int age)
    {
        super(name, gender, age);
    }

    //Checks if the Teacher can teach a certain subject
    public boolean canTeach(Subject subject)
    {
        if(subject.getSpecialism() == 1 || subject.getSpecialism() == 2)
        {
            return true;
        }
        return false;
    }


}
