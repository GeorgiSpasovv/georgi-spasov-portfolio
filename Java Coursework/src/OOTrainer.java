/**
 * Description:
 * Subclass of Teacher
 * Can teach subjects with specialism 1, 2 , 3
 * Checks if the object can teach a certain object
 *
 * @author Georgi Spasov
 * @version 1.0 17.11.2019
 */

public class OOTrainer extends Teacher
{
    //Creating a constructor
    public OOTrainer(String name, char gender, int age)
    {
        super(name, gender, age);
    }

    //Checks if the Trainer can teach a certain subject
    public boolean canTeach(Subject subject)
    {
        if(subject.getSpecialism() == 1 || subject.getSpecialism() == 2 || subject.getSpecialism() == 3)
        {
            return true;
        }
        return false;
    }
}
