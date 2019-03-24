#! /bin/bash

# check cmd args
if [ $# != 1 ]; then
    echo "USAGE: $0 module_name"
    echo "$0 springboot"
    exit 1;
fi

# create module dir
mkdir -p study-$1/src/main/java/cm/study/$1/demo
mkdir -p study-$1/src/main/resources
mkdir -p study-$1/src/test/java/cm/study/$1/demo
mkdir -p study-$1/src/test/resources

# init module pom.xml
sed "s#<artifactId>{module-name}</artifactId>#<artifactId>onepiece-$1</artifactId>#" init-pom.xml > study-$1/pom.xml

# add module config in parent pom.xml
awk '{if($1 ~ "</modules>") {printf("        <module>%s</module>\n%s\n", module_name, $0)} else print $0}' module_name=study-$1  pom.xml > pom-tmp.xml
mv pom-tmp.xml pom.xml

echo -e "module [\033[31mstudy-$1\033[0m] create success!"

