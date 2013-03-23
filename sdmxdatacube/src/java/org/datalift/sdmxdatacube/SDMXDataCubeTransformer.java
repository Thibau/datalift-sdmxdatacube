/*
 * Copyright / LIRMM 2013
 * Contributor(s) : T. Colas, T. Marmin
 *
 * Contact: thibaut.marmin@etud.univ-montp2.fr
 * Contact: thibaud.colas@etud.univ-montp2.fr
 *
 * This software is governed by the CeCILL license under French law and
 * abiding by the rules of distribution of free software. You can use,
 * modify and/or redistribute the software under the terms of the CeCILL
 * license as circulated by CEA, CNRS and INRIA at the following URL
 * "http://www.cecill.info".
 *
 * As a counterpart to the access to the source code and rights to copy,
 * modify and redistribute granted by the license, users are provided only
 * with a limited warranty and the software's author, the holder of the
 * economic rights, and the successive licensors have only limited
 * liability.
 *
 * In this respect, the user's attention is drawn to the risks associated
 * with loading, using, modifying and/or developing or reproducing the
 * software by the user in light of its specific status of free software,
 * that may mean that it is complicated to manipulate, and that also
 * therefore means that it is reserved for developers and experienced
 * professionals having in-depth computer knowledge. Users are therefore
 * encouraged to load and test the software's suitability as regards their
 * requirements in conditions enabling the security of their systems and/or
 * data to be ensured and, more generally, to use and operate it in the
 * same conditions as regards security.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL license and that you accept its terms.
 */

package org.datalift.sdmxdatacube;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.datalift.fwk.log.Logger;
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
import org.springframework.stereotype.Service;

/**
 * A Spring Bean which uses SDMXRDFParser from Metadata Technologies' SdmxSource
 * to convert SDMX 2.1 datasets / structures to DataCube.
 *
 * This code is mainly from the example of usage provided by Matt Nelson
 * (org.sdmxsource.demo.rdf.main.RDFDataTransformer, rev 11 on SVN).
 *
 * @author T. Colas, T. Marmin, Matt Nelson (Metadata Tech)
 * @version 260213
 */
@Service
public class SDMXDataCubeTransformer {

	/** Required to Build SdmxBeans from a SDMX/EDI source. */
	@Autowired
	private StructureParsingManager structureParsingManager;

	/** Required for StructureWriterEngine. */
	@Autowired
	private StructureWritingManager structureWritingManager;

	/** Required to get the required DataWriterEngine. */
	@Autowired
	private DataWriterManager dataWriterManager;

	/** Required to get the DataReaderEngine capable of reading the datasource. */
	@Autowired
	private DataReaderManager dataReaderManager;

	 /** Required to read from the data source, and write to the target source. */
	@Autowired
	private DataReaderWriterTransform dataReaderWriterTransform;

	@Autowired
	private ReadableDataLocationFactory readableDataLocationFactory;

	@Autowired
	private InMemoryRetrievalManager inMemoryRetrievalManager;

	/** Datalift's logger. */
	protected static final Logger LOG = Logger.getLogger();


	public SdmxBeans outputStructures(StructureFormat structureFormat)
			throws Exception {
		ReadableDataLocation structures = readableDataLocationFactory
				.getReadableDataLocation(new URL(
						"http://imf.sdmxregistry.org/ws/restInterfaceV2_1/dataflow/ALL/ALL/LATEST/?detail=full&references=descendants"));

		// Build an object representation of the SDMX
		SdmxBeans beans = structureParsingManager.parseStructures(structures)
				.getStructureBeans(false);

		// Write to the in memory manager, this is used to resolve cross
		// referenced structures later
		inMemoryRetrievalManager.saveStructures(beans);

		ByteArrayOutputStream out = new ByteArrayOutputStream();
		structureWritingManager.writeStructures(beans, structureFormat, out);

		return beans;
	}

	public ByteArrayOutputStream outputData(SdmxBeans beans, DataFormat dataFormat)
			throws Exception {
		ReadableDataLocation data = readableDataLocationFactory
				.getReadableDataLocation(new URL(
						"http://imf.sdmxregistry.org/ws/restInterfaceV2_1/Data/IMF,PGI,1.0/193+223+156.BCA+BGS...?lastNObservations=12"));

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

		return out;
	}

	public ByteArrayOutputStream convertSDMXToDataCube(InputStream sourceStream, RDFFormat rdfFormat) {
		// TODO replace structures and data with dataset.
		ReadableDataLocation dataset = readableDataLocationFactory.getReadableDataLocation(sourceStream);
		ReadableDataLocation structures = null;
		ReadableDataLocation data = null;
		try {
			structures = readableDataLocationFactory.getReadableDataLocation(new URL("http://imf.sdmxregistry.org/ws/restInterfaceV2_1/dataflow/ALL/ALL/LATEST/?detail=full&references=descendants"));
			data = readableDataLocationFactory.getReadableDataLocation(new URL("http://imf.sdmxregistry.org/ws/restInterfaceV2_1/Data/IMF,PGI,1.0/193+223+156.BCA+BGS...?lastNObservations=12"));

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		/* Working on structure */

		// Build an object representation of the SDMX.
		SdmxBeans beans = structureParsingManager.parseStructures(structures).getStructureBeans(false);

		// Write to the in memory manager, this is used to resolve cross-referenced structures later
		inMemoryRetrievalManager.saveStructures(beans);

		structureWritingManager.writeStructures(beans, new RDFStructureOutputFormat(rdfFormat), new ByteArrayOutputStream());

		/* Working on data */

		// Create a reader, we either need the datastructure at this point, or access to get the datastructure.
		DataReaderEngine dataReader = dataReaderManager.getDataReaderEngine(data, new InMemoryRetrievalManager(beans));

		ByteArrayOutputStream convertedStream = new ByteArrayOutputStream();
		DataWriterEngine dataWriter = dataWriterManager.getDataWriterEngine(new RDFDataOutputFormat((DataflowBean)beans.getDataflows().toArray()[0], rdfFormat), convertedStream);

		// Copy to writer, copyheader=true, closewriter on completion=true
		dataReaderWriterTransform.copyToWriter(dataReader, dataWriter, true, true);

		return convertedStream;
	}
}
