import  java.util.Random;

public class Administrator
{
    private School school = new School("ECS Java Training School ");
    private Random rand = new Random();


    void run()
    {
        for(int i=0; i<=rand.nextInt(2); i++)
        {
            school.add(new Student("Jorko",'M', 18));
        }

        int a1 = rand.nextInt(4);
        if(a1 == 1)
        {
            school.add(new Teacher("Katq", 'F', 44));
        }

        int a2 = rand.nextInt(9);
        if(a2 == 1)
        {
            school.add(new Demonstrator("Mitko", 'M', 51));
        }

        int a3 = rand.nextInt(19);
        if(a3 == 1)
        {
            school.add(new OOTrainer("Qsen", 'M', 35));
        }

        int a4 = rand.nextInt(19);
        if(a4 == 1)
        {
            school.add(new GUITrainer("Vqra", 'F', 32 ));
        }

        
        school.aDayAtSchool();
    }
}
