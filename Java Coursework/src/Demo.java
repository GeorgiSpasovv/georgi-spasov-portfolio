public class Demo {

    public static void main(String[] args)
    {
        School school1 = new School ("High School of Mathematics");

        Teacher teacher1 = new Teacher ("Yvonne", 'F', 55);
        GUITrainer guiTrainer1 = new GUITrainer ("Sara", 'F', 48);
        OOTrainer ooTrainer1 = new OOTrainer ("Chris", 'M', 62);
        Demonstrator demonstrator1 = new Demonstrator ("Beth", 'F',45);
        Teacher teacher2 = new Teacher ("Rizova", 'F', 50);
        GUITrainer guiTrainer2 = new GUITrainer ("Donka", 'F', 40);
        OOTrainer ooTrainer2 = new OOTrainer ("Pandurski", 'M', 30);
        Demonstrator demonstrator2 = new Demonstrator ("Ishev", 'M',38);
        Teacher teacher3 = new Teacher ("Rizova", 'F', 50);
        GUITrainer guiTrainer3 = new GUITrainer ("Donka", 'F', 40);
        OOTrainer ooTrainer3 = new OOTrainer ("Pandurski", 'M', 30);
        Demonstrator demonstrator3 = new Demonstrator ("Ishev", 'M',38);

        Subject subject1 = new Subject (1,1,5);
        Subject subject2 = new Subject (2,2,2);
        Subject subject3 = new Subject (3,1,4);
        Subject subject4 = new Subject (4,4,1);
        Subject subject8 = new Subject (8,1,2);
        Subject subject7 = new Subject (7,2,3);
        Subject subject6 = new Subject (6,3,1);
        Subject subject5 = new Subject (5,4,2);
        Subject subject9 = new Subject (9,1,2);
        Subject subject10 = new Subject (10,2,3);
        Subject subject11 = new Subject (11,3,1);
        Subject subject12 = new Subject (12,4,2);

        Student student1 = new Student ("Peter", 'M',60);
        Student student2 = new Student ("John", 'M',22);
        Student student3 = new Student ("Annabelle", 'F',31);
        Student student4 = new Student ("Maggie", 'F',58);
        Student student5 = new Student ("Alex", 'M',23);
        Student student6 = new Student ("Ivaylo", 'M',18);
        Student student7 = new Student ("Spasov", 'M',18);
        Student student8 = new Student ("Yokov", 'F',18);
        Student student9 = new Student ("Kostov", 'F',18);
        Student student10 = new Student ("Mone", 'M',18);
        Student student11 = new Student ("Done", 'M',18);
        Student student12 = new Student ("Rade", 'M',18);
        Student student13 = new Student ("Plamen", 'F',18);
        Student student14 = new Student ("Stefata", 'F',18);
        Student student15 = new Student ("Mariq", 'M',18);
        Student student16 = new Student ("Tomcho", 'M',18);
        Student student17 = new Student ("Rampata", 'M',18);
        Student student18 = new Student ("Malkiq Nikolov", 'F',18);
        Student student19 = new Student ("Petre", 'F',18);
        Student student20 = new Student ("Raicho", 'M',18);
        Student student21 = new Student ("Taki", 'M',18);
        Student student22 = new Student ("Aleks", 'M',18);
        Student student23 = new Student ("Martin", 'F',18);
        Student student24 = new Student ("Vase", 'F',18);
        Student student25 = new Student ("Vlade", 'M',18);

        school1.add (teacher1); school1.add (demonstrator1); school1.add (ooTrainer1); school1.add (guiTrainer1);
        school1.add (teacher2); school1.add (demonstrator2); school1.add (ooTrainer2); school1.add (guiTrainer2);
        school1.add (teacher3); school1.add (demonstrator3); school1.add (ooTrainer3); school1.add (guiTrainer3);

        school1.add (subject1); school1.add (subject2); school1.add (subject3); school1.add (subject4);
        school1.add (subject5); school1.add (subject6); school1.add (subject7); school1.add (subject8);
        school1.add (subject9); school1.add (subject10); school1.add (subject11); school1.add (subject12);

        school1.add (student1); school1.add (student2); school1.add (student3); school1.add (student4); school1.add (student5);
        school1.add (student6); school1.add (student7); school1.add (student8); school1.add (student9); school1.add (student10);
        school1.add (student11); school1.add (student12); school1.add (student13); school1.add (student14); school1.add (student15);
        school1.add (student16); school1.add (student17); school1.add (student18); school1.add (student19); school1.add (student20);
        school1.add (student21); school1.add (student22); school1.add (student23); school1.add (student24); school1.add (student25);

        for(int i=0; i<125; i++)
        {
            System.out.println (); System.out.println (); System.out.println ();
            System.out.println ("                                 Day: " + i);
            System.out.println (); System.out.println (); System.out.println ();
            System.out.println (school1);
            school1.aDayAtSchool ();
            System.out.println (); System.out.println (); System.out.println ();
        }

    }




}
