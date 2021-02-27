public class Main
    {


        public static void main(String[] args)
        {
            School school1 = new School ("High School of Mathematics");

            Teacher teacher1 = new Teacher ("Rizova", 'F', 50);
            GUITrainer guiTrainer1 = new GUITrainer ("Donka", 'F', 40);
            OOTrainer ooTrainer1 = new OOTrainer ("Pandurski", 'M', 30);
            Demonstrator demonstrator1 = new Demonstrator ("Ishev", 'M',38);
            GUITrainer guiTrainer2 = new GUITrainer ("Spaska", 'F', 48);

            Subject subject1 = new Subject (1,1,3);
            Subject subject2 = new Subject (2,2,2);
            Subject subject3 = new Subject (3,3,2);
            Subject subject4 = new Subject (4,4,3);
            Subject subject5 = new Subject (5,3,2);

            Student student1 = new Student ("Kaloqn", 'M',18);
            Student student2 = new Student ("Hristiqn", 'M',18);
            Student student3 = new Student ("Vesela", 'F',18);
            Student student4 = new Student ("Natali", 'F',18);
            Student student5 = new Student ("Evtim", 'M',18);
            Student student6 = new Student ("Plamen", 'M',18);
            Student student7 = new Student ("Ivaylo", 'M',18);
            Student student8 = new Student ("Mitkata", 'M',18);
            Student student9 = new Student ("Yokov", 'M',18);
            Student student10 = new Student ("Taki", 'M',18);
            Student student11 = new Student ("Alex", 'M',18);
            Student student12 = new Student ("Marti", 'M',18);
            Student student13 = new Student ("Mariq", 'M',18);

            school1.add (teacher1); school1.add (demonstrator1); school1.add (ooTrainer1);school1.add (guiTrainer1); school1.add (guiTrainer2);

            school1.add (subject1); school1.add (subject2); school1.add (subject3); school1.add (subject4); school1.add (subject5);

            school1.add (student1);  school1.add (student2); school1.add (student3); school1.add (student5); school1.add (student4); school1.add (student6); school1.add (student7); school1.add (student8);
            school1.add (student9);  school1.add (student10); school1.add (student11); school1.add (student12); school1.add (student13);



            for(int i=0; i<50; i++)
            {
                System.out.println (); System.out.println (); System.out.println ();
                System.out.println ("**************************  Day: " + i+"  *****************************");
                System.out.println (); System.out.println (); System.out.println ();
                System.out.println (school1);
                school1.aDayAtSchool ();
                System.out.println (); System.out.println (); System.out.println ();
            }

        }
    }
