import java.util.Set;

enum UserType
{
    FIRM, DEPARTMENT, UUID;

    boolean isNaturalHierarchy(UserType userType)
    {
        return true;
    }
}

public class UserSetAstNode extends AstNode
{
    private UserType    userType;
    private Set<String> users;
    private Boolean     exclude;

    public UserSetAstNode(UserType userType, Set<String> users, Boolean exclude)
    {
        this.userType = userType;
        this.users    = users;
        this.exclude  = exclude;
    }

    public UserType getUserType()
    {
        return userType;
    }

    public Set<String> getUsers()
    {
        return users;
    }

    public boolean getExclude()
    {
        return exclude;
    }

    public String toString()
    {
        return userType.name() + (exclude ? " NOT " : " " + users);
    }
}
