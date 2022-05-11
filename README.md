#Generate Java Lexer, Parser and Test

$ cd /usr/local/lib
$ sudo curl -O https://www.antlr.org/download/antlr-4.10.1-complete.jar
$ export CLASSPATH=".:/usr/local/lib/antlr-4.10.1-complete.jar:$CLASSPATH"
$ alias antlr4='java -jar /usr/local/lib/antlr-4.10.1-complete.jar'
$ alias grun='java org.antlr.v4.gui.TestRig'

- java -cp "/usr/local/lib/antlr-4.10.1-complete.jar" *.java

#Run the REPL
- java Main
