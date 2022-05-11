import java.util.*;
import java.util.Stack;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class FeatureToggleListener extends FTBaseListener
{
    private final FTParser       parser;
    private final Stack<AstNode> expressionTree = new Stack<>();
    private final Stack<AstNode> operators      = new Stack<>();

    FeatureToggleListener(FTParser parser)
    {
        this.parser = parser;
    }

    private void bindOperandsToTopOperator()
    {
        AstNode operatorNode = operators.pop();

        operatorNode.setRight(expressionTree.pop());

        operatorNode.setLeft(expressionTree.pop());

        expressionTree.push(operatorNode);
    }

    private HashSet<String> stringToHashSet(String valueString)
    {
        // Remove opening and closing brackets, whitespace characters from the string and 
        // split comma delimited value string into an array of strings

        String[] elements = valueString.replace("[","").replace("]","").replaceAll("\\s+","").split(",");

        // Convert array to set

        return new HashSet<String>(Arrays.asList(elements));
    }

    @Override public void exitActivationRule(FTParser.ActivationRuleContext ctx)
    {
        if (ctx.KW_ACTIVATE_ALL() != null)
        {
            expressionTree.push(new ActivateAllAstNode());
        }
    }

    @Override public void exitLocationActivationTerm(FTParser.LocationActivationTermContext ctx)
    {
        expressionTree.push(new LocationSetAstNode(LocationType.valueOf(ctx.locationType.getText().toUpperCase()),
                                                   stringToHashSet(ctx.value.getText()),
                                                   ctx.KW_NOT() != null));
    }

    @Override public void exitUserActivationTerm(FTParser.UserActivationTermContext ctx)
    {
        expressionTree.push(new UserSetAstNode(UserType.valueOf(ctx.userType.getText().toUpperCase()),
                                               stringToHashSet(ctx.value.getText()),
                                               ctx.KW_NOT() != null));
    }

    @Override public void exitCompoundLogicalActivationTerm(FTParser.CompoundLogicalActivationTermContext ctx)
    {
        // Give precednce to Set Operators

        while (!operators.isEmpty() && operators.peek() instanceof SetOperatorAstNode)
        {
            bindOperandsToTopOperator();
        }

        operators.push(new CompoundLogicalAstNode(CompoundLogicalOperator.valueOf(ctx.operator.getText().toUpperCase())));
    }

    private void createSetOperatorNode(int operatorTokenType)
    {
        String symbolicName = this.parser.getVocabulary().getSymbolicName(operatorTokenType);

        operators.push(new SetOperatorAstNode(SetOperator.valueOf(symbolicName)));

        while (!operators.isEmpty() && !(operators.peek() instanceof ParenthesisAstNode))
        {
            bindOperandsToTopOperator();
        }
    }

    @Override public void exitLocationSetOperator(FTParser.LocationSetOperatorContext ctx)
    {
        createSetOperatorNode(ctx.operator.getType());
    }

    @Override public void exitUserSetOperator(FTParser.UserSetOperatorContext ctx)
    {
        createSetOperatorNode(ctx.operator.getType());
    }

    @Override public void enterParenthesizedActivationTerm(FTParser.ParenthesizedActivationTermContext ctx)
    {
        operators.push(new ParenthesisAstNode());
    }

    @Override public void exitParenthesizedActivationTerm(FTParser.ParenthesizedActivationTermContext ctx)
    {
        // Give precednce to Set Operators

        while (!operators.isEmpty() && !(operators.peek() instanceof ParenthesisAstNode))
        {
            bindOperandsToTopOperator();
        }

        operators.pop();
    }

    public AstNode expressionTree()
    {
        if (!operators.isEmpty())
        {
            bindOperandsToTopOperator();
        }
 
        return expressionTree.empty() ? null : expressionTree.pop();
    }
}
