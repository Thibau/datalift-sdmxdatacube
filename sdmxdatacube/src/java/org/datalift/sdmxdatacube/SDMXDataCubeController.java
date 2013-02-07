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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.LinkedList;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.datalift.fwk.project.Project;

/**
 * 
 * @version 010213
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

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Creates a new InterconnectionController instance.
	 */
	public SDMXDataCubeController() {
		// TODO Switch to the right position.
		super(MODULE_NAME, 13371337);

		label = getTranslatedResource(MODULE_NAME + ".button");
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
		UriDesc uridesc = null;

		try {
			// The project can be handled if it has at least one RDF source.
			if (true) {
				// link URL, link label
				uridesc = new UriDesc(
						this.getName() + "?project=" + p.getUri(), this.label);

				if (this.position > 0) {
					uridesc.setPosition(this.position);
				}
				LOG.debug("Project {} can use SDMXToDataCube", p.getTitle());
			} else {
				LOG.debug("Project {} can not use SDMXToDataCube", p.getTitle());
			}
		} catch (URISyntaxException e) {
			LOG.fatal("Uh!", e);
			throw new RuntimeException(e);
		}
		return uridesc;
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
	 * @throws ObjectStreamException
	 */
	@GET
	@Produces(MediaType.TEXT_HTML)
	public Response getIndexPage(@QueryParam("project") URI projectId)
			throws ObjectStreamException {
		// Retrieve the current project and its sources.
		Project proj = this.getProject(projectId);

		HashMap<String, Object> args = new HashMap<String, Object>();
		args.put("it", proj);

		return Response.ok(this.newViewable("/convert.vm", args)).build();
	}
}
