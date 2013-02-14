package org.sdmxsource.demo.rdf.main;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

import org.openrdf.rio.RDFFormat;
import org.sdmxsource.rdf.model.RDFDataOutputFormat;
import org.sdmxsource.rdf.model.RDFStructureOutputFormat;
import org.sdmxsource.sdmx.api.engine.DataReaderEngine;
import org.sdmxsource.sdmx.api.engine.DataWriterEngine;
import org.sdmxsource.sdmx.api.factory.ReadableDataLocationFactory;
import org.sdmxsource.sdmx.api.manager.output.StructureWritingManager;
import org.sdmxsource.sdmx.api.manager.parse.StructureParsingManager;
import org.sdmxsource.sdmx.api.model.StructureFormat;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.datastructure.DataflowBean;
import org.sdmxsource.sdmx.api.model.data.DataFormat;
import org.sdmxsource.sdmx.api.util.ReadableDataLocation;
import org.sdmxsource.sdmx.dataparser.manager.DataReaderManager;
import org.sdmxsource.sdmx.dataparser.manager.DataWriterManager;
import org.sdmxsource.sdmx.dataparser.transform.DataReaderWriterTransform;
import org.sdmxsource.sdmx.structureretrieval.manager.InMemoryRetrievalManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class RDFDataTransformer {

	@Autowired
	private StructureParsingManager structureParsingManager; // Required to
																// Build
																// SdmxBeans
																// from a
																// SDMX/EDI
																// source

	@Autowired
	private StructureWritingManager structureWritingManager; // Required to
																// StructureWriterEngine

	@Autowired
	private DataWriterManager dataWriterManager; // Required to get the required
													// DataWriterEngine

	@Autowired
	private DataReaderManager dataReaderManager; // Required to get the
													// DataReaderEngine capable
													// of reading the datasource

	@Autowired
	private DataReaderWriterTransform dataReaderWriterTransform; // required to
																	// read from
																	// the data
																	// source,
																	// and write
																	// to the
																	// target
																	// source

	@Autowired
	private ReadableDataLocationFactory readableDataLocationFactory;

	@Autowired
	private InMemoryRetrievalManager inMemoryRetrievalManager;

	// May need a javaagent on the path to ensure weaving works, example
	// -javaagent:C:/JavaAgent/aspectjweaver-1.6.9.jar
	public static void main(String[] args) throws Exception {
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext(
				"spring/spring-beans.xml");
		RDFDataTransformer rdfDataTransformer = applicationContext
				.getBean(RDFDataTransformer.class);

		StructureFormat structureFormat = new RDFStructureOutputFormat(
				RDFFormat.TURTLE);

		System.out.println("Enter the SDMX local file path: ");
		InputStreamReader converter = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(converter);

		String filePath = in.readLine();

		// Output Structures, and then data
		SdmxBeans beans = rdfDataTransformer.outputStructures(structureFormat,
				filePath);

		DataFormat dataFormat = new RDFDataOutputFormat((DataflowBean) beans
				.getDataflows().toArray()[0], RDFFormat.TURTLE);
		rdfDataTransformer.outputData(beans, dataFormat, filePath);
	}

	public SdmxBeans outputStructures(StructureFormat structureFormat,
			String filePath) throws Exception {
		File f = new File(filePath);
		ReadableDataLocation structures = readableDataLocationFactory
				.getReadableDataLocation(f);

		// Build an object representation of the SDMX
		SdmxBeans beans = structureParsingManager.parseStructures(structures)
				.getStructureBeans(false);

		// Write to the in memory manager, this is used to resolve cross
		// referenced structures later
		inMemoryRetrievalManager.saveStructures(beans);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		structureWritingManager.writeStructures(beans, structureFormat, out);
		System.out.println(new String(out.toByteArray()));

		return beans;
	}

	public void outputData(SdmxBeans beans, DataFormat dataFormat,
			String filePath) throws Exception {
		File f = new File(filePath);
		ReadableDataLocation data = readableDataLocationFactory
				.getReadableDataLocation(f);

		// Create a reader, we either need the datastructure at this point, or
		// access to get the datastructure
		DataReaderEngine dataReader = dataReaderManager.getDataReaderEngine(
				data, new InMemoryRetrievalManager(beans));

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DataWriterEngine dataWriter = dataWriterManager.getDataWriterEngine(
				dataFormat, out);

		// Copy to writer, copyheader=true, closewriter on completion=true
		dataReaderWriterTransform.copyToWriter(dataReader, dataWriter, true,
				true);

		System.out.println(new String(out.toByteArray()));
	}
}
