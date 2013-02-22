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

import info.aduna.app.config.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.LinkedList;

import org.datalift.fwk.project.Project;
import org.datalift.fwk.project.ProjectManager;
import org.datalift.fwk.project.Source;
import org.datalift.fwk.project.Source.SourceType;
import org.datalift.fwk.project.SparqlSource;
import org.datalift.fwk.project.TransformedRdfSource;
import org.datalift.fwk.project.XmlSource;
import org.datalift.fwk.rdf.RdfUtils;
import org.datalift.fwk.rdf.Repository;
import org.datalift.fwk.util.PrefixUriMapper;
import org.datalift.fwk.util.UriMapper;
import org.datalift.sdmxdatacube.utils.SdmxFileUtils;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryFactory;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;

import com.google.common.net.MediaType;
import com.google.common.primitives.Bytes;

/**
 * A module to convert SDMX (XML) data to DataCube (RDF). Uses the SDMXRDFParser
 * library from SDMXSource.
 * 
 * @author T. Colas, T. Marmin
 * @version 090213
 */
public class SDMXDataCubeModel extends ModuleModel {
	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Creates a new SDMXDataCubeModel instance.
	 * 
	 * @param name
	 *            Name of the module.
	 */
	public SDMXDataCubeModel(String name) {
		super(name);
	}

	// -------------------------------------------------------------------------
	// Sources management.
	// -------------------------------------------------------------------------

	/**
	 * Checks if a given {@link Source} contains valid SDMX-structured data.
	 * 
	 * @param src
	 *            The source to check.
	 * @return True if src is {@link TransformedRdfSource} or
	 *         {@link SparqlSource}.
	 */
	public boolean isValidSource(Source src) {
		if (src.getType().equals(SourceType.XmlSource)) {
			XmlSource xmlsrc = (XmlSource) src;
			try {
				if (SdmxFileUtils.isSdmx(xmlsrc.getInputStream())) {
					return true;
				}
			} catch (IOException e) {
				LOG.fatal("Failed to check status of source {}: {}", e,
						src.getUri(), e.getMessage());
			}
		}
		return false;
	}

	public void lauchSdmxToDatacubeProcess(Project project, XmlSource source,
			TransformedRdfSource destination) throws Exception {

		LOG.debug("Lauching process to convert the SDMX source {} to RDF {}",
				source.getFilePath(), destination.getUri());
		TransformedRdfSource d = (TransformedRdfSource) destination;

		Repository repo = org.datalift.fwk.Configuration.getDefault()
				.getInternalRepository();

		RdfUtils.upload(convert(source),
				javax.ws.rs.core.MediaType.APPLICATION_XML_TYPE, repo, new URI(
						d.getTargetGraph()), null);

	}

	private InputStream convert(XmlSource source) {
		// TODO Use the SdmxSource library

		String rdfxml = "<?xml version=\"1.0\"?>\n"
				+ "<rdf:RDF\n"
				+ "xmlns:rdf=\"http://www.w3.org/1999/02/22-rdf-syntax-ns#\"\n"
				+ "xmlns:cd=\"http://www.recshop.fake/cd#\">\n"
				+ "<rdf:Description\n"
				+ "rdf:about=\"http://www.recshop.fake/cd/Empire Burlesque\">\n"
				+ "<cd:artist>Bob Dylan</cd:artist>\n"
				+ "<cd:country>USA</cd:country>\n"
				+ "<cd:company>Columbia</cd:company>\n"
				+ "<cd:price>10.90</cd:price>\n" + "<cd:year>1985</cd:year>\n"
				+ "</rdf:Description>\n" + "<rdf:Description\n"
				+ "rdf:about=\"http://www.recshop.fake/cd/Hide your heart\">\n"
				+ "<cd:artist>Bonnie Tyler</cd:artist>\n"
				+ "<cd:country>UK</cd:country>\n"
				+ "<cd:company>CBS Records</cd:company>\n"
				+ "<cd:price>9.90</cd:price>\n" + "<cd:year>1988</cd:year>\n"
				+ "</rdf:Description>\n" + "</rdf:RDF>\n";
		return new ByteArrayInputStream(rdfxml.getBytes());
	}
}
