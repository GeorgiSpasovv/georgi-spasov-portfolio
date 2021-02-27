/**
 * Description:
 * Has a name, gender and age
 * Has methods that return the properties
 * Has a method that sets the age
 *
 * @author Georgi Spasov
 * @version 1.0 17.11.2019
 */

public class Person
{
    protected String name;
    protected char gender;
    protected int age;

    //Creating a constructor that initialises the variables
    public Person (String name, char gender, int age)
    {
        this.name = name;
        this.gender = gender;
        this.age = age;
    }

    //Returning the name
    public String getName()
    {
        return this.name;
    }

    //Returning the gender
    public char getGender()
    {
        return this.gender;
    }

    //Setting the age
    public void setAge(int age)
    {
        this.age = age;
    }

    //Returning the age
    public int getAge()
    {
        return this.age;
    }
}
