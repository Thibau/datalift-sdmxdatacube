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

import java.io.FileNotFoundException;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Response.Status;

import org.datalift.fwk.BaseModule;
import org.datalift.fwk.Configuration;
import org.datalift.fwk.MediaTypes;
import org.datalift.fwk.ResourceResolver;
import org.datalift.fwk.i18n.PreferredLocales;
import org.datalift.fwk.log.Logger;
import org.datalift.fwk.project.Project;
import org.datalift.fwk.project.ProjectManager;
import org.datalift.fwk.project.ProjectModule;
import org.datalift.fwk.view.TemplateModel;
import org.datalift.fwk.view.ViewFactory;

/**
 * A generic base for a Datalift module which wraps Datalift's project
 * management in a convenient way.
 * 
 * @author T. Colas, T. Marmin
 * @version 090213
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
	protected final int MODULE_POSITION;

	/** The DataLift project manager. */
	protected ProjectManager projectManager = null;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * ModuleController with default behavior.
	 * 
	 * @param name
	 *            Name of the module.
	 * @param pos
	 *            Position of the module's button.
	 */
	public ModuleController(String name, int position) {
		super(name);

		this.MODULE_POSITION = position;
	}

	// -------------------------------------------------------------------------
	// Module contract support
	// -------------------------------------------------------------------------

	/** {@inheritDoc} */
	@Override
	public void postInit(Configuration configuration) {
		super.postInit(configuration);

		this.projectManager = configuration.getBean(ProjectManager.class);
		if (this.projectManager == null) {
			throw new TechnicalException("project.manager.not.available");
		}
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
	 * @param projectId
	 *            the project URI.
	 * 
	 * @return the project.
	 * @throws TechnicalException
	 *             if the project does not exist.
	 */
	protected final Project getProject(java.net.URI projectId) {
		Project p = this.projectManager.findProject(projectId);
		if (p == null) {
			throw new IllegalArgumentException(projectId.toString());
		}
		return p;
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

	/**
	 * Return a model for the specified template view, populated with the
	 * specified model object.
	 * <p>
	 * The template name shall be relative to the module, the module name is
	 * automatically prepended.
	 * </p>
	 * 
	 * @param templateName
	 *            the relative template name.
	 * @param it
	 *            the model object to pass on to the view.
	 * 
	 * @return a populated template model.
	 */
	protected TemplateModel newView(String templateName, Object it) {
		return ViewFactory.newView("/" + this.getName() + '/' + templateName,
				it);
	}

	// -------------------------------------------------------------------------
	// Web services
	// -------------------------------------------------------------------------

	/**
	 * Index page handler that each module must implement.
	 * 
	 * @param projectId
	 *            the project using our module.
	 * @return Our module's interface.
	 */
	@GET
	@Produces({ MediaTypes.TEXT_HTML_UTF8, MediaTypes.APPLICATION_XHTML_XML })
	public abstract Response getIndexPage(
			@QueryParam("project") java.net.URI projectId);

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

	/**
	 * Throws a {@link WebApplicationException} with a HTTP status set to 400
	 * (Bad request) to signal an invalid or missing web service parameter.
	 * 
	 * @param name
	 *            the parameter name in the web service interface.
	 * @param value
	 *            the invalid parameter value or <code>null</code> if the
	 *            parameter was absent.
	 * 
	 * @throws WebApplicationException
	 *             always.
	 */
	protected void throwInvalidParamError(String name, Object value)
			throws WebApplicationException {
		TechnicalException error = (value != null) ? new TechnicalException(
				"ws.invalid.param.error", name, value)
				: new TechnicalException("ws.missing.param", name);
		this.sendError(Status.BAD_REQUEST, error.getLocalizedMessage());
	}

	/**
	 * Logs and map an internal processing error onto HTTP status codes.
	 * 
	 * @param e
	 *            the error to map.
	 * 
	 * @throws WebApplicationException
	 *             always.
	 */
	protected void handleInternalError(Exception e)
			throws WebApplicationException {
		TechnicalException error = null;
		if (e instanceof WebApplicationException) {
			throw (WebApplicationException) e;
		} else if (e instanceof FileNotFoundException) {
			this.sendError(Status.NOT_FOUND, e.getLocalizedMessage());
		} else if (e instanceof TechnicalException) {
			error = (TechnicalException) e;
		} else {
			error = new TechnicalException("ws.internal.error", e,
					e.getLocalizedMessage());
		}
		LOG.fatal(e.getMessage(), e);
		this.sendError(Status.INTERNAL_SERVER_ERROR,
				error.getLocalizedMessage());
	}

}
