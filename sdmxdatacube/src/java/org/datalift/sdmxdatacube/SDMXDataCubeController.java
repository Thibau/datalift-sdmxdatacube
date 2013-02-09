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

import java.io.ObjectStreamException;
import java.net.URISyntaxException;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.datalift.fwk.MediaTypes;
import org.datalift.fwk.project.Project;
import org.datalift.fwk.view.TemplateModel;

/**
 * The SDMX DataCube module's main class which exposes the SDMXRDFParser engine
 * to the Datalift architecture.
 * 
 * @author T. Colas, T. Marmin
 * @version 090213
 */
@Path(SDMXDataCubeController.MODULE_NAME)
public class SDMXDataCubeController extends ModuleController {
	// -------------------------------------------------------------------------
	// Constants
	// -------------------------------------------------------------------------

	/** The module's name. */
	public static final String MODULE_NAME = "sdmxdatacube";

	// -------------------------------------------------------------------------
	// Instance members
	// -------------------------------------------------------------------------

	protected SDMXDataCubeModel model;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Creates a new SDMXDataCubeController instance, sets its button position.
	 */
	public SDMXDataCubeController() {
		// TODO Switch to the right position.
		super(MODULE_NAME, 13371337);
		model = new SDMXDataCubeModel(MODULE_NAME);
	}

	// -------------------------------------------------------------------------
	// Project management
	// -------------------------------------------------------------------------

	/**
	 * Tells the project manager to add a new button to projects with at least
	 * two sources.
	 * 
	 * @param p
	 *            Our current project.
	 * @return The URI to our project's main page.
	 */
	public final UriDesc canHandle(Project p) {
		UriDesc projectPage = null;
		try {
			// The project can be handled if it has at least one SDMX source.
			if (true || model.hasMultipleValidSources(p, 1)) {
				// link URL, link label
				projectPage = new UriDesc(this.getName() + "?project="
						+ p.getUri(), HttpMethod.GET,
						getTranslatedResource(MODULE_NAME + ".button"));
				projectPage.setPosition(this.position);

				LOG.debug("Project {} can use SDMXToDataCube", p.getTitle());
			} else {
				LOG.debug("Project {} cannot use SDMXToDataCube", p.getTitle());
			}
		} catch (URISyntaxException e) {
			LOG.fatal("Failed to check status of project {}: {}", e,
					p.getUri(), e.getMessage());
		}
		return projectPage;
	}

	// -------------------------------------------------------------------------
	// Web services
	// -------------------------------------------------------------------------

	/**
	 * Index page handler of the SDMXToDataCube module.
	 * 
	 * @param projectId
	 *            the project using SDMXToDataCube
	 * @return Our module's interface.
	 */
	@GET
	@Produces({ MediaTypes.TEXT_HTML_UTF8, MediaTypes.APPLICATION_XHTML_XML })
	public Response getIndexPage(@QueryParam("project") java.net.URI projectId) {
		Response response = null;
		try {
			// Retrieve project.
			Project p = this.getProject(projectId);
			// Display conversion configuration page.
			TemplateModel view = this.newView("convert-form.vm", p);
			view.put("defaultOutputSourceName",
					model.generateOutputSourceName(p));
			view.put("defaultOutputSourceURI", model.generateOutputSourceURI(p));
			response = Response.ok(view, MediaTypes.TEXT_HTML_UTF8).build();
		} catch (IllegalArgumentException e) {
			TechnicalException error = new TechnicalException(
					"ws.invalid.param.error", "project", projectId);
			this.sendError(Status.BAD_REQUEST, error.getLocalizedMessage());
		}
		return response;
	}

	/**
	 * Form submit handler : launching SDMXDataCube.
	 * 
	 * @param projectId
	 *            the project using SDMXDataCube.
	 * @param inputSource
	 *            context of our source (reference) data.
	 * @param outputSourceName
	 *            name of the source which will be created.
	 * @param outputSourceUri
	 *            URI of the source (graph) which will be created to store the
	 *            result.
	 * @return Our module's post-process page.
	 * @throws ObjectStreamException
	 */
	@POST
	@Consumes(MediaTypes.APPLICATION_FORM_URLENCODED)
	@Produces(MediaTypes.TEXT_PLAIN)
	public Response doSubmit(@QueryParam("projectId") java.net.URI projectId,
			@QueryParam("inputSource") String inputSource,
			@QueryParam("outputSourceName") String outputSourceName,
			@QueryParam("outputSourceURI") String outputSourceURI)
			throws WebApplicationException {

		// TODO : voir le code de OntologyMapper.java
		// Project proj = this.getProject(projectId);

		// inputSource = inputSource.trim();
		// outputSourceName = outputSourceName.trim();
		// outputSourceURI = outputSourceURI.trim();

		// String view;
		// HashMap<String, Object> args = new HashMap<String, Object>();
		// args.put("it", proj);

		// // We first validate all of the fields.
		// LinkedList<String> errorMessages = model.getErrorMessages(proj,
		// inputSource, outputSourceName, outputSourceURI);

		// if (errorMessages.isEmpty()) {
		// args.put("inputSource", inputSource);
		// args.put("outputSourceName", outputSourceName);
		// args.put("outputSourceURI", outputSourceURI);
		// // StringToURI is launched if and only if our values are all valid.
		// args.put("SDMXDataCube", model.launchSDMXDataCube(proj,
		// inputSource, outputSourceName, outputSourceURI));
		// view = "stringtouri-success.vm";
		// } else {
		// args.put("errormessages", errorMessages);
		// view = "stringtouri-error.vm";
		// }

		return null; // Response.ok(this.newViewable("/" + view, args)).build();
	}
}
