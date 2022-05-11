
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class FeatureToggleParser
{
    public AstNode parse(String input)
    {
        CharStream chars = CharStreams.fromString(input);

        Lexer lexer = new FTLexer(chars);

        lexer.removeErrorListeners();

        CommonTokenStream tokens = new CommonTokenStream(lexer);

        FTParser parser = new FTParser(tokens);

        parser.removeErrorListeners();

        parser.addErrorListener(new BaseErrorListener()
        {
            @Override
            public void syntaxError(Recognizer<?,?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e)
                {
                    throw new RuntimeException(String.format("Syntax error at line %d:%d due to %s", line, charPositionInLine, msg));
                }
        });

        ParseTree syntaxTree = parser.activationRule();

        FeatureToggleListener listener = new FeatureToggleListener(parser);

        ParseTreeWalker walker = new ParseTreeWalker();

        walker.walk(listener, syntaxTree);
   
        return listener.expressionTree();
    }
}

