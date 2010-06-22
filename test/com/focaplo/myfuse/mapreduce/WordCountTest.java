package com.focaplo.myfuse.mapreduce;

import java.io.IOException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
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


public class WordCountTest {

	@SuppressWarnings("deprecation")
	public static class Map extends MapReduceBase implements Mapper<LongWritable, Text, Text, IntWritable>{
		private final static IntWritable one = new IntWritable(1);
		Text word = new Text();
		@Override
		public void map(LongWritable key, Text value,
				OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			String line = value.toString();
			StringTokenizer st = new StringTokenizer(line);
			while(st.hasMoreTokens()){
				word.set(st.nextToken());
				output.collect(word, one);
			}
			
		}

		@Override
		public void configure(JobConf arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void close() throws IOException {
			// TODO Auto-generated method stub
			
		}
		
		
	}
	
	public static class Reduce extends MapReduceBase implements Reducer<Text, IntWritable, Text, IntWritable>{

		@Override
		public void reduce(Text key, Iterator<IntWritable> values,
				OutputCollector<Text, IntWritable> output, Reporter reporter)
				throws IOException {
			int sum=0;
			while(values.hasNext()){
				sum+=values.next().get();
			}
			output.collect(key, new IntWritable(sum));
		}
		
	}
	
	public static void main(String[] args){
		JobConf jc = new JobConf(WordCountTest.class);
		jc.setJobName("wordcount");
		jc.setOutputKeyClass(Text.class);
		jc.setOutputValueClass(IntWritable.class);
		jc.setMapperClass(WordCountTest.Map.class);
		jc.setReducerClass(WordCountTest.Reduce.class);
		jc.setCombinerClass(WordCountTest.Reduce.class);
		jc.setInputFormat(TextInputFormat.class);
		jc.setOutputFormat(TextOutputFormat.class);
		FileInputFormat.setInputPaths(jc, new Path(args[0]));
		FileOutputFormat.setOutputPath(jc, new Path(args[1]));
		
		try {
			JobClient.runJob(jc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
