#!/bin/bash
cp ressources/* classes/
cd classes
export CLASSPATH=`find ../lib -name "*.jar" | tr '\n' ':'`
java -cp ${CLASSPATH}:. $@
cd ..

