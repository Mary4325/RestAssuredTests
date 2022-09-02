package Pojo;

public class Login
{
    public Login(String login, String password, String profileType) {
        this.login = login;
        this.password = password;
        this.profileType = profileType;
    }

    private String password;

    private String profileType;

    private String login;

    public String getPassword ()
    {
        return password;
    }

    public void setPassword (String password)
    {
        this.password = password;
    }

    public String getProfileType ()
    {
        return profileType;
    }

    public void setProfileType (String profileType)
    {
        this.profileType = profileType;
    }

    public String getLogin ()
    {
        return login;
    }

    public void setLogin (String login)
    {
        this.login = login;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [password = "+password+", profileType = "+profileType+", login = "+login+"]";
    }
}