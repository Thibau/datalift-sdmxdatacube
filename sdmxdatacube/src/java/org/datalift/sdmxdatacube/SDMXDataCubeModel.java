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
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;

import org.datalift.fwk.project.Project;
import org.datalift.fwk.project.ProjectManager;
import org.datalift.fwk.project.Source;
import org.datalift.fwk.project.Source.SourceType;
import org.datalift.fwk.project.SparqlSource;
import org.datalift.fwk.project.TransformedRdfSource;
import org.datalift.fwk.project.XmlSource;
import org.datalift.fwk.rdf.Repository;
import org.datalift.sdmxdatacube.utils.SdmxFileUtils;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.config.RepositoryFactory;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;

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

	/**
	 * SDMXDataCube error checker with message generation
	 * 
	 * @param proj
	 *            the project using SDMXDataCube.
	 * @param inputSource
	 *            context of our source (reference) data.
	 * @param outputSourceName
	 *            name of the source which will be created.
	 * @param outputSourceURI
	 *            URI of the source (graph) which will be created to store the
	 *            result.
	 * @return
	 */
	public final LinkedList<String> getErrorMessages(Project proj,
			String inputSource, String outputSourceName, String outputSourceURI) {

		// TODO : Look at OntologyMapper.java
		// LinkedList<String> errors = new LinkedList<String>();

		// // We have to test every value one by one in order to add the right
		// // error message.
		// // TODO Add custom errors for empty values.
		// try {
		// Source s = proj.getSource(inputSource);
		// if (s == null)
		// errors.add(getTranslatedResource("error.inputSourceNotFound"));
		// } catch (IllegalArgumentException e) {
		// errors.add(getTranslatedResource("error.inputSourceNotSpecified"));
		// }

		// try {
		// Source s = proj.getSource(outputSourceURI);
		// if (s != null)
		// errors.add(getTranslatedResource("error.outputSourceAlreadyExists"));
		// } catch (IllegalArgumentException e) {
		// errors.add(getTranslatedResource("error.outputSourceURINotSpecified"));
		// }

		// if (outputSourceName.isEmpty())
		// errors.add(getTranslatedResource("error.outputSourceNameNotSpecified"));

		return null; // errors;
	}

	public final LinkedList<LinkedList<String>> launchSDMXDataCube(
			Project proj, String inputSource, String outputSourceName,
			String outputSourceURI) {
		LinkedList<LinkedList<String>> ret = new LinkedList<LinkedList<String>>();
		LinkedList<String> errors = this.getErrorMessages(proj, inputSource,
				outputSourceName, outputSourceURI);
		if (errors.isEmpty()) {
			LOG.debug("lauching SDMXDataCube !");
		} else {
			// Should never happen.
			LOG.fatal("Oops it should never have happened...");
			ret = new LinkedList<LinkedList<String>>();
			ret.add(errors);
		}
		return ret;
	}

	/**
	 * Generate a default output name to prefill the form, based on the current
	 * project
	 * 
	 * @param proj
	 *            the current project
	 * @return the name
	 */
	public String generateOutputSourceName(Project proj) {
		// TODO Generate a good name :)
		return "TODO générer un nom ici";
	}

	/**
	 * Generate a default output URI for prefill the form, based on the current
	 * project
	 * 
	 * @param proj
	 *            the current project
	 * @return the URI
	 */
	public String generateOutputSourceURI(Project proj) {
		// TODO generate a good URI :)
		return "TODO générer une bonne URI ini";
	}

	public void lauchSdmxToDatacubeProcess(Project project, XmlSource source,
			TransformedRdfSource destination) throws Exception {

		LOG.debug("Lauching process to convert the SDMX source {} to RDF {}",
				source.getFilePath(), destination.getUri());
		try {
			TransformedRdfSource d = (TransformedRdfSource) destination;
			HTTPRepository repo = new HTTPRepository(d.getTargetGraph());
			repo.initialize();
			LOG.debug("Repo initialized");
			RepositoryConnection cnt = repo.getConnection();
			LOG.debug("Connection ok");
			// TODO resolve exception authorization next ligne
			// cnt.add(convert(source), null, RDFFormat.RDFXML);
			// LOG.debug("Connection add ok");
			cnt.close();
			LOG.debug("Connection closed");
			// Source datalift_source =
		} catch (RepositoryException e) {// | RDFParseException | IOException e)
											// {
			throw e;
		}
	}

	private InputStream convert(XmlSource source) {
		// TODO Use the SdmxSource library
		return new ByteArrayInputStream("".getBytes());
	}
}
