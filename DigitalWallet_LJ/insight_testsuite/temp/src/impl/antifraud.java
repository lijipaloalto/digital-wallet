package impl;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import Graph.Graph;
import Graph.Vertex;
import algorithm.BFSearch;
import payments.paymentRecord;

public class antifraud {
	public static void main(String[] args) throws IOException {
		Collection<paymentRecord> batchPayments = readInputFile(args[0]);
		Collection<paymentRecord> streamPayments = readInputFile(args[1]);
		String graphName = "paymentMap";
		Graph graph = new Graph(graphName);
		processPaymentRecords(graph, batchPayments); 
		
		List<String> feature1 = new LinkedList<String>();
		List<String> feature2 = new LinkedList<String>();
		List<String> feature3 = new LinkedList<String>();
		
		for (paymentRecord record: streamPayments) {
			evaluateOnePaymentRecord(graph, record, feature1, feature2, feature3);
			processOnePaymentRecord(graph, record);
		}
		
		writeOutputFile(feature1, args[2]);
		writeOutputFile(feature2, args[3]);
		writeOutputFile(feature3, args[4]);
	}

	private static Collection<paymentRecord> readInputFile(String inputFileName) throws IOException {
		Collection<paymentRecord> outputList = new ArrayList<>();
		int errorEntryCount = 0;
		
		InputStream in = new FileInputStream(new File(inputFileName));
		try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
			String headerLine = null;
			if ((headerLine = br.readLine()) != null) { // read header
				String line = null;
				while ((line = br.readLine()) != null) { // read lines of records
					String[] elements = line.split(",");
					if (!isValidEntry(elements)) {
						errorEntryCount++;
						continue;
					}
					paymentRecord record = new paymentRecord(elements[0].trim(), elements[1].trim(), 
							elements[2].trim(), Double.parseDouble(elements[3].trim()));
					outputList.add(record);
				}
			} else {
				System.err.println("Error Reading HeaderLine - " + inputFileName);
			}
		}
		System.out.println("Finished Reading " + inputFileName + " with " + errorEntryCount
				+ " invalid records and " + outputList.size() + " valid records.");
		return outputList;
	}
	
	private static boolean isValidEntry(String[] elements) {
		if (elements.length < 4) 
			return false;
		if (elements[1] == null || elements[2] == null) 
			return false;
		try {
			Long.parseLong(elements[1].trim());
		} catch (Exception e) {
			return false;
		}
		try {
			Long.parseLong(elements[2].trim());
		} catch (Exception e) {
			return false;
		}
		try {
			Double.parseDouble(elements[3].trim());
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	private static void processPaymentRecords(Graph graph, Collection<paymentRecord> records) {
		for (paymentRecord record: records) {
			processOnePaymentRecord(graph, record);
		}
		System.out.println("Finished Processing Batch Payments.");
	}
	
	private static void processOnePaymentRecord(Graph graph, paymentRecord record) {
		graph.addNode(record.getId1());
		graph.addNode(record.getId2());
		graph.addEdge(record.getId1()+"-"+record.getId2(), record.getId1(), record.getId2());
		graph.addEdge(record.getId2()+"-"+record.getId1(), record.getId2(), record.getId1());
	}
	
	private static void evaluateOnePaymentRecord(Graph graph, paymentRecord record,
			List<String> feature1, List<String> feature2, List<String> feature3) {
		String label1 = "unverified";
		String label2 = "unverified";
		String label3 = "unverified";
		
		if (graph.containNode(record.getId1()) && graph.containNode(record.getId2())) {
			Vertex source = graph.getNode(record.getId1());
			Vertex target = graph.getNode(record.getId2());
			BFSearch bfs1 = new BFSearch();
			bfs1.execute(graph, source, 1);
			label1 = bfs1.getSettledNodes().contains(target)?"trusted":"unverified";
			
			if (label1.equals("trusted")) {
				label2 = "trusted";
				label3 = "trusted";
			} else {
				BFSearch bfs2 = new BFSearch();
				bfs2.execute(graph, source, 2);
				label2 = bfs2.getSettledNodes().contains(target)?"trusted":"unverified";
				if (label2.equals("trusted")) {
					label3 = "trusted";
				} else {
					BFSearch bfs3 = new BFSearch();
					bfs3.execute(graph, target, 2);
					bfs3.getSettledNodes().retainAll(bfs2.getSettledNodes());
					label3 = bfs3.getSettledNodes().isEmpty() ? "unverified":"trusted";
				}
			}
		}
		feature1.add(label1);
		feature2.add(label2);
		feature3.add(label3);
	}
	
	private static void writeOutputFile(List<String> features, String outputFileName) {
		try {
			File fout = new File(outputFileName);
			FileOutputStream fos = new FileOutputStream(fout);
			BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
			
			for (String result : features) {
				bw.write(result);
				bw.newLine();
			}
			bw.close();
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		System.out.println("Finished writing " + outputFileName);
	}
	
}
