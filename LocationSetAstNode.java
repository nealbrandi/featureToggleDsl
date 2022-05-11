import java.util.Set;

enum LocationType
{
    CLUSTER, MACHINE, STAGE, TAG;

    boolean isNaturalHierarchy(LocationType locationType)
    {
        return locationType == LocationType.CLUSTER ||
               locationType == LocationType.MACHINE;
    }
}

public class LocationSetAstNode extends AstNode
{
    private LocationType locationType;
    private Set<String>  locations;
    private Boolean      exclude;

    public LocationSetAstNode(LocationType locationType, Set<String> locations, Boolean exclude)
    {
        this.locationType = locationType;
        this.locations    = locations;
        this.exclude      = exclude;
    }

    public LocationType getLocationType()
    {
        return locationType;
    }

    public Set<String> getLocations()
    {
        return locations;
    }

    public boolean getExclude()
    {
        return exclude;
    }

    public String toString()
    {
        return locationType.name() + (exclude ? " NOT " : " " + locations);
    }
}
