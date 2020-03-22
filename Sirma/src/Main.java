import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class Main
{
    private static ArrayList<Record> records = new ArrayList<>();

    private static BufferedReader reader;

    private static HashMap<Integer, ArrayList<Record>> workedTogether = new HashMap<>();

    //Reading a file
    private static void readFile() throws IOException, ParseException
    {
        while (reader.ready())
        {
            Record record = new Record();
            String line = reader.readLine();
            if(line == null) break;

            String [] lineVector = line.split(", ");

            record.setEmpID(Integer.parseInt(lineVector[0]));
            record.setProjectID(Integer.parseInt(lineVector[1]));

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            record.setDateFrom(dateFormat.parse(lineVector[2]));

            //Checking if the date is today's date
            if(lineVector[3].equals("NULL"))
            {

                record.setDateTo(Calendar.getInstance().getTime());
            }
            else
            {
                record.setDateTo(dateFormat.parse(lineVector[3]));
            }

            records.add(record);

        }
    }

    //Finding the employees who worked the longest together
    private static void compare()
    {
        for(int i=0; i<records.size()-1; i++)
        {
            for(int j=i+1; j<records.size(); j++)
            {
                //Finding the employees who worked on the same project
                if(records.get(i).getProjectID() == records.get(j).getProjectID())
                {

                    Record rec1 = records.get(i);
                    Record rec2 = records.get(j);

                    //Finding the number of days they worked together
                    if(rec1.getDateFrom().before(rec2.getDateFrom()))
                    {
                        if(rec1.getDateTo().after(rec2.getDateFrom()))
                        {
                            if(rec1.getDateTo().before(rec2.getDateTo()))
                            {
                                Integer a = getDifferenceDays(rec2.getDateFrom(), rec1.getDateTo());
                                ArrayList<Record> employees = new ArrayList<>();
                                employees.add(rec1);
                                employees.add(rec2);
                                workedTogether.put(a, employees);

                            }
                            else
                            {
                                Integer a = getDifferenceDays(rec2.getDateFrom(), rec2.getDateTo());
                                ArrayList<Record> employees = new ArrayList<>();
                                employees.add(rec1);
                                employees.add(rec2);
                                workedTogether.put(a, employees);
                            }
                        }

                    }
                    else
                    {
                        if(rec2.getDateTo().after(rec1.getDateFrom()))
                        {
                            if(rec1.getDateTo().before(rec2.getDateTo()))
                            {
                                Integer a = getDifferenceDays(rec1.getDateFrom(), rec1.getDateTo());
                                ArrayList<Record> employees = new ArrayList<>();
                                employees.add(rec1);
                                employees.add(rec2);
                                workedTogether.put(a, employees);
                            }
                            else
                            {
                                Integer a = getDifferenceDays(rec1.getDateFrom(), rec2.getDateTo());
                                ArrayList<Record> employees = new ArrayList<>();
                                employees.add(rec1);
                                employees.add(rec2);
                                workedTogether.put(a, employees);
                            }
                        }

                    }
                }
            }
        }

        //Finding the maximum number of days
        int maxValue=(Collections.max(workedTogether.keySet()));

        //Outputting the employees
        System.out.println("The employees who worked together the longest are:\n" + workedTogether.get(maxValue).get(0)+ "\n"+ workedTogether.get(maxValue).get(1)+ "\nThey worked "+ maxValue+" days together.");

    }

    //Get the number of days between two dates
    public static Integer getDifferenceDays(Date d1, Date d2)
    {
        long diff = d2.getTime() - d1.getTime();

        return (int)TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }


    public static void main(String [] args)
    {
        Scanner myObj = new Scanner(System.in);
        System.out.print("Input data file directory: (ex: C:\\Users\\user\\Desktop\\data.txt)  ");
        String fileDirectory = myObj.nextLine();


        try {
            File file = new File(fileDirectory);
            FileReader fr = new FileReader(file);
            reader = new BufferedReader(fr);

            readFile();

            //If you want to output all the records
            /*for(Record r : records)
            {
                System.out.println(r.toString());
            }
             */
        }
        catch (Exception e)
        {
        System.out.println("Error with the file! Please check if the file is the right format and the path is correct.");
        }
        compare();

    }

}
