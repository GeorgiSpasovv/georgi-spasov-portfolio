/**
 * Description:
 * Subclass of Instructor
 * Can teach subjects with specialism 2
 * Checks if the object can teach a certain object
 *
 * @author Georgi Spasov
 * @version 1.0 17.11.2019
 */

public class Demonstrator extends Instructor
{
    //Creating a constructor
    public Demonstrator(String name, char gender, int age)
    {
        super(name, gender, age);
    }

    //Checks if the Demonstrator can teach a certain subject
    public boolean canTeach(Subject subject)
    {
        if(subject.getSpecialism() == 2)
        {
            return true;
        }
        return false;
    }
}
