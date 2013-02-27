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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.datalift.fwk.MediaTypes;
import org.datalift.fwk.project.Project;
import org.datalift.fwk.project.Source;
import org.datalift.fwk.project.Source.SourceType;
import org.datalift.fwk.project.SparqlSource;
import org.datalift.fwk.project.TransformedRdfSource;
import org.datalift.fwk.project.XmlSource;
import org.datalift.fwk.rdf.RdfException;
import org.datalift.fwk.rdf.RdfUtils;
import org.datalift.fwk.rdf.Repository;
import org.datalift.sdmxdatacube.utils.SdmxFileUtils;
import org.openrdf.rio.RDFFormat;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * A module to convert SDMX (XML) data to DataCube (RDF). Uses the SDMXRDFParser
 * library from SDMXSource.
 * 
 * @author T. Colas, T. Marmin
 * @version 260213
 */
public class SDMXDataCubeModel extends ModuleModel {

	// -------------------------------------------------------------------------
	// Instance members
	// -------------------------------------------------------------------------

	private SDMXDataCubeTransformer sdmxDataCubeTransformer;

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

		// Initialize the sdmxDataCubeTransformer, which is a Spring bean.
		// It is also referenced in spring-beans.xml.
		Thread.currentThread().setContextClassLoader(
				this.getClass().getClassLoader());
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext();
		ctx.setConfigLocation("spring/spring-beans.xml");
		ctx.refresh();

		sdmxDataCubeTransformer = ctx.getBean(SDMXDataCubeTransformer.class);
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

	/**
	 * Launches a conversion process on a given project, using a given source.
	 * 
	 * @param project
	 *            The project where a new DataCube source will be created.
	 * @param source
	 *            The source which will be converted.
	 * @param destination
	 *            Where the new source will be.
	 * @throws Exception
	 */
	public void lauchSdmxToDatacubeProcess(Project project, XmlSource source,
			TransformedRdfSource destination) {

		LOG.debug("Lauching process to convert the SDMX source {} to RDF {}",
				source.getFilePath(), destination.getUri());
		TransformedRdfSource d = (TransformedRdfSource) destination;

		Repository repo = org.datalift.fwk.Configuration.getDefault()
				.getInternalRepository();

		try {
			// Turtle is one of the lightest, ideal for direct upload.
			RdfUtils.upload(convert(source, RDFFormat.TURTLE),
					MediaTypes.TEXT_TURTLE_TYPE, repo,
					new URI(d.getTargetGraph()), null);
		} catch (RdfException e) {
			LOG.fatal("Failed to upload RDF to source {}: {}", e,
					d.getTargetGraph(), e.getMessage());
		} catch (URISyntaxException e) {
			LOG.fatal("Failed to upload RDF to source {}: {}", e,
					d.getTargetGraph(), e.getMessage());
		}

	}

	// TODO Find a way to define URI templates elsewhere.
	private InputStream convert(XmlSource source, RDFFormat rdfFormat) {
		ByteArrayOutputStream convertedStream = null;

		try {
			convertedStream = sdmxDataCubeTransformer.convertSDMXToDataCube(
					source.getInputStream(), rdfFormat);
		} catch (IOException e) {
			LOG.fatal("Failed to load stream of source {}: {}", e,
					source.getUri(), e.getMessage());
		}

		return new ByteArrayInputStream(convertedStream.toByteArray());
	}
}
