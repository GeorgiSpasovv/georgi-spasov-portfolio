/**
 * Description:
 * Has an id, specialism, duration, description
 * Has a constructor that initialises the variables
 * Has methods that return each variable
 *
 * @author Georgi Spasov
 * @version 1.0 17.11.2019
 */

import  java.util.Random;

public class Subject
{
    private Integer id;
    private int specialism;
    private int duration;
    private String description;
    //private Random rand = new Random();

    //Creating a constructor which initialises the variables
    public Subject(int id, int specialism, int duration)
    {
        this.id = id;
        this.specialism = specialism;
        this.duration = duration;

    }

    //Returning the id
    public int getID()
    {
        return this.id;
    }

    //Returning the specialism
    public int getSpecialism()
    {
        return this.specialism;
    }

    //Returning the duration
    public int getDuration()
    {
        return this.duration;
    }

    //Setting the description
    public void setDescription(String description)
    {
        this.description = description;
    }

    //Returning the description
    public String getDescription()
    {
        return this.description;
    }

    //Returns all properties
    public String toString()
    {
        return "\nId: "+getID()+"\n"+"Specialism: "+getSpecialism()+"\n"+"Duration: "+getDuration()+"\n*****************\n";
    }
}
