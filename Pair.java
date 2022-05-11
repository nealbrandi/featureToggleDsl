public class Pair<FIRST, SECOND>
{
    private FIRST  first;
    private SECOND second;

    public Pair(FIRST first, SECOND second)
    {
        this.first  = first;
        this.second = second;
    }

    public FIRST first()
    {
        return first;
    }

    public SECOND second()
    {
        return second;
    }
}

