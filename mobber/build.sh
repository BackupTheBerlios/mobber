#!/bin/bash

echo "This is the Mobber Build-System"


if [ -z "$JAVA_HOME" ]
then
JAVACMD=`which java`
if [ -z "$JAVACMD" ]
then
echo "Cannot find JAVA. Please set your PATH."
exit 1
fi
JAVA_BINDIR=`dirname $JAVACMD`
JAVA_HOME=$JAVA_BINDIR/..
fi

if [ "$ANT_OPTS" = "" ] ; then
  ANT_OPTS=""
fi

JAVACMD=$JAVA_HOME/bin/java $ANT_OPTS

cp=lib/ant.jar:lib/optional.jar:lib/jaxp.jar:lib/parser.jar:$JAVA_HOME/lib/tools.jar:$JAVA_HOME/lib/dev.jar


$JAVACMD -classpath $cp:$CLASSPATH org.apache.tools.ant.Main "$@"




