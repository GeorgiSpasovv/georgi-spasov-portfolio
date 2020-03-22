import java.text.SimpleDateFormat;
import java.util.Date;

public class Record
{
    private int empID;
    private int projectID;
    private Date dateFrom;
    private Date dateTo;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    //Creating getters and setters for easy access to the class
    protected void setEmpID(int id)
    {
        empID = id;
    }

    protected void setProjectID(int id)
    {
        projectID = id;
    }

    protected void setDateFrom(Date date)
    {
        dateFrom = date;
    }

    protected void setDateTo(Date date)
    {
        dateTo = date;
    }

    protected int getEmpID()
    {
        return empID;
    }

    protected int getProjectID()
    {
        return projectID;
    }

    protected Date getDateFrom()
    {

        return dateFrom;
    }

    protected Date getDateTo()
    {
        return dateTo;
    }

    public String toString()
    {
        return getEmpID()+" "+getProjectID()+" "+dateFormat.format(getDateFrom())+" "+dateFormat.format(getDateTo());
    }

}
