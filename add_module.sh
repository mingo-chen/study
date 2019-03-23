#! /bin/bash

if [ $# != 1 ]; then
    echo "USAGE: $0 module_name"
    exit 1;
fi

mkdir study-$1
mkdir -p study-$1/src
mkdir -p study-$1/src/main
mkdir -p study-$1/src/main/java
mkdir -p study-$1/src/main/resources
mkdir -p study-$1/src/test
mkdir -p study-$1/src/test/java
mkdir -p study-$1/src/test/resources

sed "s#<artifactId>{module-name}</artifactId>#<artifactId>onepiece-$1</artifactId>#" init-pom.xml > study-$1/pom.xml
awk '{if($1 ~ "</modules>") {printf("        <module>%s</module>\n%s\n", module_name, $0)} else print $0}' module_name=study-$1  pom.xml > pom-tmp.xml

mv pom-tmp.xml pom.xml

echo -e "module [\033[31mstudy-$1\033[0m] create success!"

