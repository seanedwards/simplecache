# Mark Royer
#
# A simple Makefile for compiling java programs and generating the related 
# java documentation.  To compile and generate javadoc a user can simply type
# 
# make
#
# at the command line.  To remove all of the files that were built and leave
# only the source files, a user can type
#
# make clean
#
# at the command line.  For more information about Makefiles see
#
# http://www.gnu.org/software/make/manual/make.html
#
# Additional information about javadoc can be found at
#
# http://java.sun.com/j2se/javadoc/


# The top level directory
rootdir = .

libs = $(rootdir)/lib/junit-4.10.jar:$(rootdir)/lib/commons-cli-1.2.jar:$(rootdir)/lib/ini4j-0.5.2-SNAPSHOT.jar:$(rootdir)/lib/log4j-1.2.16.jar

# Where the source files and libraries are
CLASSPATH = $(libs):$(outdir)

tests = edu.umaine.cs.cos226.SimpleCache.Server.ServerTest
outdir=./out
# List of source files
sourcelist = $(shell find $(rootdir) -name '*.java' | sed "s,[.]/,,g")

# Where the generated documentation will be put
docdir = ./docs 

default: all 

all:
        # Compile source files
	@javac -d $(outdir) -cp $(CLASSPATH) $(sourcelist) 

        # Invoke javadoc with using the list in source list
        # -d output generated documentation to docdir
        # -linksource include a link to the source in documenation
	@javadoc -d $(docdir) -linksource $(sourcelist)

        # Create a convenient script to run test classes
	@echo "java -cp" $(CLASSPATH) "org.junit.runner.JUnitCore " $(tests) > runTests
	@chmod +x runTests

clean:
        # Remove all of the documentation
	@if [ -d $(docdir) ]; then rm -r $(docdir); fi;

        # Remove all the class files in the classpath
	@-find $(rootdir) \( -name "*~" -o -name "*.class" -o -name "runTests" \) -exec rm '{}' \; 


