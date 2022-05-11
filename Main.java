import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Main
{
    public static void main(String[] args) throws IOException
    {
        Repl repl = new Repl(new InputStreamReader(System.in),
                             new FeatureToggleParser(),
                             new PrintWriter(System.out)
        );

        repl.start();
    }
}
