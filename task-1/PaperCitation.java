import java.io.IOException;
import java.util.StringTokenizer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;

public class PaperCitation {
public static class PaperCitationMapper1
extends Mapper < Object , Text , Text, Text >{

private String fromNode = new String ();
private String toNode=new String();

public void map ( Object key , Text value , Context context
) throws IOException , InterruptedException {
String[] tokens = value.toString().trim().split("\\s");

fromNode=tokens[0];
toNode=tokens[1];
context . write (new Text(toNode), new Text(fromNode));
context.write(new Text(fromNode), new Text("-"+toNode));
}
}

public static class PaperCitationReducer1
extends Reducer < Text, Text , Text, Text > {

public void reduce ( Text key , Iterable < Text > values ,Context context
) throws IOException , InterruptedException {

String all = new String();
for ( Text val : values ) {
all += ","+val.toString();
}

all=all.substring(1,all.length());
context . write ( new Text(key) , new Text(all) );
}
}


public static class PaperCitationMapper2
extends Mapper < Text , Text , Text, IntWritable >{

private Text fromNode = new Text ();
private Text toNode=new Text();

public void map ( Text key , Text value , Context context
) throws IOException , InterruptedException {
String[] tokens = value.toString().trim().split(",");
int count=1;
for(String key1:tokens)
{
if(!key1.startsWith("-"))
{
count+=1;
}
}

for(String key1:tokens)
{
if(key1.startsWith("-"))
{
context.write(new Text(key1.substring(1,key1.length())), new IntWritable(count));
}
}

}
}

public static class PaperCitationReducer2
extends Reducer < Text, IntWritable , Text, IntWritable > {

public void reduce ( Text key , Iterable <IntWritable>  values ,Context context
) throws IOException , InterruptedException {

int sum=0;

for(IntWritable v:values)
{
sum+=v.get();
}


context . write ( key ,new IntWritable(sum) );
}
}



public static void main ( String [] args ) throws Exception {


Configuration conf = new Configuration();
Job job= Job.getInstance(conf, "PaperCitation at distance 2");
job.setJarByClass(PaperCitation.class);
job.setMapperClass(PaperCitationMapper1.class);
job.setReducerClass(PaperCitationReducer1.class);
job.setMapOutputKeyClass(Text.class);
job.setMapOutputValueClass(Text.class);
FileInputFormat.addInputPath(job, new Path(args[0]));
FileOutputFormat.setOutputPath(job, new Path(args[1]));
job . waitForCompletion ( true );

Job job2 = Job.getInstance(conf, "PaperCitation Continued");
job2.setJarByClass(PaperCitation.class);
job2.setMapperClass(PaperCitationMapper2.class);
job2.setReducerClass(PaperCitationReducer2.class); job2.setMapOutputKeyClass(Text.class);
job2.setMapOutputValueClass(IntWritable.class);
// to ensure job2 mapper takes the input as key,value as produced by Reducer
job2.setInputFormatClass(KeyValueTextInputFormat.class); 
FileInputFormat.addInputPath(job2, new Path(args[1]));
FileOutputFormat.setOutputPath(job2, new Path(args[2]));
System . exit ( job2 . waitForCompletion ( true ) ? 0 : 1);


}
}
