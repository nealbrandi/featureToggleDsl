enum SetOperator
{
    UNKNOWN, UNION, INTERSECTION, SUBTRACTION
}

public class SetOperatorAstNode extends AstNode
{
    private SetOperator setOperator;

    public SetOperatorAstNode(SetOperator setOperator)
    {
        this.setOperator = setOperator;
    }

    public SetOperatorAstNode(SetOperator setOperator, AstNode left, AstNode right)
    {
        super(left, right);
 
        this.setOperator = setOperator;
    }

    public SetOperator getSetOperator()
    {
        return setOperator;
    }

    public String toString()
    {
        return setOperator.name();
    }
}

