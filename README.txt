COMP47470 Project-3: Graph Processing
By: Thejas Raju (18204398)

TASK-1: Hadoop MapReduce

Please use the dataset labelled as 'in1.txt' which is present in the zip file for input as this file is clear of all textual data.

Commands-
To start the data and name node:
$HADOOP_HOME/bin/start-all.sh

To insert input dataset into Hadoop file system:
$HADOOP_HOME/bin/hadoop dfs -put in1.txt /user/hduser/input/

To compute the citation count, navigate to directory containing the pc.jar file and then execute the following command:
$HADOOP_HOME/bin/hadoop jar pc.jar wordcount /user/hduser/input/in1.txt /user/hduser/output1

To obtain top 10 cited papers:
$HADOOP_HOME/bin/hadoop dfs -cat /user/hduser/output1/p*| sort -k2 -r -n | head -n10

TASK-2: Giraph

To compile the PapersSignificance.java file.

javac -classpath /usr/local/hadoop2/hadoop-core-1.2.1.jar:/usr/local/giraph/giraph-examples/target/giraph-examples-1.3.0-SNAPSHOT-for-hadoop-1.2.1-jar-with-dependencies.jar PapersSignificance.java

Creating the jar file:
jar cvf id.jar PapersSignificance*.class

export HADOOP_CLASSPATH=/usr/local/giraph/giraph-examples/target/giraph-examples-1.3.0-SNAPSHOT-for-hadoop-1.2.1-jar-with-dependencies.jar:<path of id.jar file>

export LIBJARS=/usr/local/giraph/giraph-examples/target/giraph-examples-1.3.0-SNAPSHOT-for-hadoop-1.2.1-jar-with-dependencies.jar,<path of id.jar file>

To execute the graph-processing job:

$HADOOP_HOME/bin/hadoop jar /usr/local/giraph/giraph-examples/target/giraph-examples-1.3.0-SNAPSHOT-for-hadoop-1.2.1-jar-with-dependencies.jar org.apache.giraph.GiraphRunner -libjars $LIBJARS PapersSignificance -vif org.apache.giraph.io.formats.LongLongNullTextInputFormat -vip /user/hduser/input/in1.txt -vof org.apache.giraph.io.formats.IdWithValueTextOutputFormat -op /user/hduser/output2 -w 1

To obtain the top ten cited papers:
$HADOOP_HOME/bin/hadoop dfs -cat /user/hduser/output2/p*| sort -k2 -r -n | head -n10


