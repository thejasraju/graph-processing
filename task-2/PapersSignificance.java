import org.apache.giraph.graph.BasicComputation;
import org.apache.giraph.edge.Edge;
import org.apache.giraph.graph.Vertex;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.LongWritable;

import java.io.IOException;


public class PapersSignificance extends BasicComputation<
	LongWritable, LongWritable, NullWritable, DoubleWritable> {

	@Override
	public void compute(
			Vertex<LongWritable, LongWritable, NullWritable> vertex,
			Iterable<DoubleWritable> messages) throws IOException {
		if (getSuperstep() == 0) {
			Iterable<Edge<LongWritable, NullWritable>> edges = vertex.getEdges();
			for (Edge<LongWritable, NullWritable> edge : edges) {
				sendMessage(edge.getTargetVertexId(), new DoubleWritable(1.0));
			}
		} else if (getSuperstep() == 1){

			Iterable<Edge<LongWritable, NullWritable>> edges = vertex.getEdges();
			long sum = 0;
			for (DoubleWritable message : messages) {
				sum++;
			}

for (Edge<LongWritable, NullWritable> edge : edges) {
				sendMessage(edge.getTargetVertexId(), new DoubleWritable(sum));
}
			LongWritable vertexValue = vertex.getValue();
			vertexValue.set(sum);
			vertex.setValue(vertexValue);
			
		}  else if (getSuperstep() == 2){
			
		long sum = 0;
			for (DoubleWritable message : messages) {
				sum+=message.get();
			}
		LongWritable vertexValue1 = vertex.getValue();
		long vertexValue2 = vertexValue1.get();
		vertexValue2+=sum;
		vertexValue1.set(vertexValue2);
		vertex.setValue(vertexValue1);
			
			

		}else {

			vertex.voteToHalt();
			}
	}
}
