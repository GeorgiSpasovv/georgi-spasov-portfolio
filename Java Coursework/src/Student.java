/**
 * Description:
 *
 * Gives a certificate to the student
 * Returns the certificate
 * Checks if the student already has a certificate for a certain subject
 *
 * @author Georgi Spasov
 * @version 1.0 17.11.2019
 */


import java.util.ArrayList;

public class Student extends Person
{
    private ArrayList<Integer> certificates;
    public boolean isAssigned;

    //Creating a constructor
    public Student(String name, char gender, int age)
    {
        super(name, gender, age);
        //this.certificates.add();
        this.certificates = new ArrayList<Integer>();
    }

    //Gives a certificate to the student for the specified subject
    public void graduate(Subject subject)
    {
        this.certificates.add(subject.getID());
    }

    //Returns the certificate
    public ArrayList getCertificates()
    {
        return this.certificates;
    }

    //Checks if the student already has a certificate for the specified subject
    public boolean hasCertificate(Subject subject)
    {
        for(int c : certificates)
        {
            if(c == subject.getID())
            {
                return true;
            }
        }
        return false;
    }

    public String toString()
    {
        return "\nName: "+name+"\n"+"Gender: "+gender+"\n"+"Age: "+age+"\n"+"Assigned: "+isAssigned+"\n"+"Certificates: "+ getCertificates()+"\n***********************************\n";
    }
}
