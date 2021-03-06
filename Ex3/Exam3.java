package Exam;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;
import org.apache.hadoop.mapred.TextInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;

public class Exam3 {
	public static class Map extends MapReduceBase implements Mapper<LongWritable,Text,Text,Text>{
		public void map(LongWritable key,Text value,OutputCollector<Text,Text>output,Reporter report) throws IOException{
			String count = "count";	
			String[] str = value.toString().split("\t");
				output.collect(new Text(count), new Text(str[0]));
			}
		}
	public static class Reduce extends MapReduceBase implements Reducer<Text,Text,Text,Text>{
		public void reduce(Text key,Iterator<Text>values, OutputCollector<Text,Text>output,Reporter report) throws IOException{		
			int count = 0;
			while(values.hasNext()){
				String ip = values.next().toString();
				count++;
			}
			output.collect(key, new Text("IP:"+count));
		}
	}
	public static void main( String[] args ) throws IOException {
		JobConf conf = new JobConf(Exam3.class);
		conf.setJobName("Exam3");
		conf.setMapperClass(Map.class);
		conf.setReducerClass(Reduce.class);
		
		conf.setNumReduceTasks(2);
		conf.setOutputKeyClass(Text.class);
		conf.setOutputValueClass(Text.class);
		
		conf.setInputFormat(TextInputFormat.class);
		conf.setOutputFormat(TextOutputFormat.class);

		FileInputFormat.setInputPaths(conf, new Path(args[0]));
		FileOutputFormat.setOutputPath(conf, new Path(args[1]));

		JobClient.runJob(conf);
	}
}
