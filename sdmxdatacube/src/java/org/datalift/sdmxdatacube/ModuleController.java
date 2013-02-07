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

import static javax.ws.rs.core.HttpHeaders.ACCEPT;

import java.io.ObjectStreamException;
import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.datalift.fwk.BaseModule;
import org.datalift.fwk.Configuration;
import org.datalift.fwk.ResourceResolver;
import org.datalift.fwk.i18n.PreferredLocales;
import org.datalift.fwk.log.Logger;
import org.datalift.fwk.project.Project;
import org.datalift.fwk.project.ProjectManager;
import org.datalift.fwk.project.ProjectModule;
import org.datalift.fwk.view.TemplateModel;
import org.datalift.fwk.view.ViewFactory;

/**
 * 
 * @version 010213
 */
public abstract class ModuleController extends BaseModule implements
		ProjectModule {

	// -------------------------------------------------------------------------
	// Constants
	// -------------------------------------------------------------------------

	/** Base name of the resource bundle for converter GUI. */
	protected static final String GUI_RESOURCES_BUNDLE = "resources";

	// -------------------------------------------------------------------------
	// Class members
	// -------------------------------------------------------------------------

	/** Datalift's logger. */
	protected static final Logger LOG = Logger.getLogger();

	// -------------------------------------------------------------------------
	// Instance members
	// -------------------------------------------------------------------------

	/** The requested module position in menu. */
	protected int position;
	/** The requested module label in menu. */
	protected String label;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * InterlinkingController with default behavior.
	 * 
	 * @param name
	 *            Name of the module.
	 * @param pos
	 *            Position of the module's button.
	 */
	public ModuleController(String name, int pos) {
		super(name);

		position = pos;
	}

	// -------------------------------------------------------------------------
	// Project management
	// -------------------------------------------------------------------------

	/**
	 * Resource getter.
	 * 
	 * @param key
	 *            The key to retrieve.
	 * @return The value of key.
	 */
	protected String getTranslatedResource(String key) {
		return PreferredLocales.get()
				.getBundle(GUI_RESOURCES_BUNDLE, ModuleController.class)
				.getString(key);
	}

	/**
	 * Retrieves a {@link Project} using its URI.
	 * 
	 * @param projuri
	 *            the project URI.
	 * 
	 * @return the project.
	 * @throws ObjectStreamException
	 *             if the project does not exist.
	 */
	protected final Project getProject(URI projuri)
			throws ObjectStreamException {
		ProjectManager pm = Configuration.getDefault().getBean(
				ProjectManager.class);
		Project p = pm.findProject(projuri);

		return p;
	}

	/**
	 * Handles our Velocity templates.
	 * 
	 * @param templateName
	 *            Name of the template to parse.
	 * @param it
	 *            Parameters for the template.
	 * @return A new viewable in Velocity Template Language
	 */
	protected final TemplateModel newViewable(String templateName, Object it) {
		return ViewFactory.newView("/" + this.getName() + templateName, it);
	}

	/**
	 * Tells the project manager to add a new button to projects with at least
	 * two sources.
	 * 
	 * @param p
	 *            Our current project.
	 * @return The URI to our project's main page.
	 */
	public abstract UriDesc canHandle(Project p);

	// -------------------------------------------------------------------------
	// Web services
	// -------------------------------------------------------------------------

	/**
	 * Index page handler for interlinking modules.
	 * 
	 * @param projectId
	 *            the project using our module.
	 * @return Our module's interface.
	 * @throws ObjectStreamException
	 *             A Obscene Reject Mixt Opt.
	 */
	public abstract Response getIndexPage(@QueryParam("project") URI projectId)
			throws ObjectStreamException;

	/**
	 * Traps accesses to module static resources and redirect them toward the
	 * default {@link ResourceResolver} for resolution.
	 * 
	 * @param path
	 *            the relative path of the module static resource being
	 *            accessed.
	 * @param uriInfo
	 *            the request URI data (injected).
	 * @param request
	 *            the JAX-RS request object (injected).
	 * @param acceptHdr
	 *            the HTTP "Accept" header value.
	 * 
	 * @return a {@link Response JAX-RS response} to download the content of the
	 *         specified public resource.
	 */
	@GET
	@Path("static/{path: .*$}")
	public Object getStaticResource(@PathParam("path") String path,
			@Context UriInfo uriInfo, @Context Request request,
			@HeaderParam(ACCEPT) String acceptHdr) {
		return Configuration
				.getDefault()
				.getBean(ResourceResolver.class)
				.resolveModuleResource(this.getName(), uriInfo, request,
						acceptHdr);
	}
}
