public class ItemStatus
{
    private String[] ids;

    private String status;

    public ItemStatus(String[] ids, String status) {
        this.ids = ids;
        this.status = status;
    }

    public String[] getIds ()
    {
        return ids;
    }

    public void setIds (String[] ids)
    {
        this.ids = ids;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [ids = "+ids+", status = "+status+"]";
    }
}