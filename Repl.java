import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.*;

// Read Evaluate Print Loop

public class Repl
{
    private final Scanner                     scanner;
    private final FeatureToggleParser         featureToggleParser;
    private final Writer                      output;
    private final List<Pair<String, AstNode>> validActivationRules;

    private final String                      ruleFileName = "rules.txt";

    public Repl(Reader input, FeatureToggleParser featureToggleParser, Writer output)
    {
        this.scanner              = new Scanner(input);
        this.featureToggleParser  = featureToggleParser;
        this.output               = output;
        this.validActivationRules = new ArrayList<Pair<String, AstNode>>();
    }

    private Boolean parseAndStoreRule(String rule)
    {
        try
        {
            this.writeLine("Parsing rule " + (validActivationRules.size() + 1) + ": " + rule);

            long startTime = System.nanoTime();

            AstNode astRootNode = this.featureToggleParser.parse(rule);

            long endTime = System.nanoTime();
            
            this.writeLine("Parse duration: " + ((endTime - startTime) / 1000000) + " milliseconds");

            if (astRootNode == null)
            {
                this.writeLine("ERROR: Parse of activation rule failed.");

                return false;
            } 
            
            this.writeLine(astRootNode.printTree());

            validActivationRules.add(new Pair<>(rule, astRootNode));
        }
        catch (IOException e)
        {
            return false;
        }

        return true;
    }

    private int validateActivationRuleIndex(String ruleIndex)
    {
        int resultIndex = 0;

        try
        {
            resultIndex = Integer.parseInt(ruleIndex);
        }
        catch (NumberFormatException e)
        {
            throw new RuntimeException("Intergers are required for diff arguments. Received: '" + ruleIndex + "'.");
        }
      
        if (resultIndex <= 0 || resultIndex > validActivationRules.size())
        {
            throw new RuntimeException("Activation Rule index reference " + resultIndex + " invalid.  Valid values: 0 - " + validActivationRules.size());
        }

        return resultIndex - 1;
    }

    public void start() throws IOException
    {
        while (true)
        {
            try
            {
                this.writeLine("\nEnter a rule or command: ");

                String line = this.scanner.nextLine();

                if (line.equalsIgnoreCase("exit") || line.isEmpty())
                {
                    this.writeLine("Arrivederci");

                    break;
                }

                if (line.equalsIgnoreCase("help"))
                {
                    this.writeLine("Commands: list, save, load, help, exit");

                    continue;
                }

                if (line.equalsIgnoreCase("list"))
                {
                    int i = 0;

                    for (Pair<String, AstNode> rule : validActivationRules)
                    {
                        this.writeLine("\nRule: " + ++i + " - " + rule.first());
                        this.writeLine(rule.second().printTree());
                        this.writeLine();
                    }

                    this.writeLine("\n" + i + " rules listed.");

                    continue;
                }

                if (line.equalsIgnoreCase("save"))
                {
                    FileWriter fileWriter = new FileWriter (this.ruleFileName, false);

                    int i = 0;

                    for (Pair<String, AstNode> rule : validActivationRules)
                    {
                        fileWriter.write(rule.first() + System.lineSeparator());

                        ++i;
                    }

                    fileWriter.close();

                    this.writeLine("\n" + i + " rules saved to " + ruleFileName);

                    continue;
                }

                if (line.equalsIgnoreCase("load"))
                {
                    try
                    {
                        Scanner scanner = new Scanner(new File(ruleFileName));

                        int i = 0;

                        while (scanner.hasNextLine())
                        {
                            parseAndStoreRule(scanner.nextLine());

                            ++i;
                        }

                        scanner.close();

                        this.writeLine("\nLoad complete. " + i + " rules loaded.");
                    }
                    catch (FileNotFoundException e)
                    {
                        this.writeLine("\nERROR: " + e);
                    }

                    continue;
                }

                parseAndStoreRule(line);
            }
            catch (RuntimeException e)
            {
                String message = e.toString().replace("java.lang.RuntimeException: ", "\n");

                this.writeLine("\nERROR: " + message);
            }
            catch (Exception e)
            {
                this.writeLine("\nUnexpected exception: " + e);
            }
        }
    }

    private void write(String message) throws IOException
    {
        this.output.write(message);

        this.output.flush();
    }

    private void writeLine(String line) throws IOException
    {
        this.write(line + "\n");
    }

    private void writeLine() throws IOException
    {
        this.write("\n");
    }
}
