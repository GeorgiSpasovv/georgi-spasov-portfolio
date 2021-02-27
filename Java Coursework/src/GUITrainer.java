/**
 * Description:
 * Subclass of Teacher
 * Can teach subjects with specialism 1, 2, 4
 * Checks if the object can teach a certain object
 *
 * @author Georgi Spasov
 * @version 1.0 17.11.2019
 */

public class GUITrainer extends Teacher
{
    //Creating a constructor
    public GUITrainer(String name, char gender, int age)
    {
        super(name, gender, age);
    }

    //Checks if the Trainer can teach a certain subject
    public boolean canTeach(Subject subject)
    {
        if(subject.getSpecialism() == 1 || subject.getSpecialism() == 2 || subject.getSpecialism() == 4)
        {
            return true;
        }
        return false;
    }
}
