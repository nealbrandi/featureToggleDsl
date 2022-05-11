enum CompoundLogicalOperator
{
    AND, OR
}

public class CompoundLogicalAstNode extends AstNode
{
    private CompoundLogicalOperator compoundLogicalOperator;

    public CompoundLogicalAstNode(CompoundLogicalOperator compoundLogicalOperator)
    {
        this.compoundLogicalOperator = compoundLogicalOperator;
    }

    public CompoundLogicalAstNode(CompoundLogicalOperator compoundLogicalOperator, AstNode left, AstNode right)
    {
        super(left, right);

        this.compoundLogicalOperator = compoundLogicalOperator;
    }

    public CompoundLogicalOperator getCompoundLogicalOperator()
    {
        return compoundLogicalOperator;
    }

    public String toString()
    {
        return compoundLogicalOperator.name();
    }
}
