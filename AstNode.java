import java.util.*;

public class AstNode
{
    private AstNode left;
    private AstNode right;

    public AstNode()
    {
        this.left  = null;
        this.right = null;
    }

    public AstNode(AstNode left, AstNode right)
    {
        this.left  = left;
        this.right = right;
    }

    public String printTree()
    {
        StringBuilder buffer = new StringBuilder(1024);

        buffer.append('\n');

        print(buffer, "", "");

        return buffer.toString();
    }

    private void print(StringBuilder buffer, String nodePrefix, String childPrefix)
    {
        buffer.append(nodePrefix);

        buffer.append(toString());

        buffer.append('\n');

        if (left != null)
        {
            left.print (buffer, childPrefix + "┣━━ ", childPrefix + "┃   ");

            right.print(buffer, childPrefix + "┗━━ ", childPrefix + "    ");
        }
    }

    public AstNode getLeft()
    {
        return left;
    }

    public void setLeft(AstNode left)
    {
        this.left = left; 
    }

    public AstNode getRight()
    {
        return right;
    }

    public void setRight(AstNode right)
    {
        this.left = right; 
    }
}
