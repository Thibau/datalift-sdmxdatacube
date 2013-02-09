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

import java.util.Iterator;
import java.util.LinkedList;

import org.datalift.fwk.Configuration;
import org.datalift.fwk.i18n.PreferredLocales;
import org.datalift.fwk.log.Logger;
import org.datalift.fwk.project.Project;
import org.datalift.fwk.project.Source;
import org.datalift.fwk.project.SparqlSource;
import org.datalift.fwk.project.TransformedRdfSource;
import org.datalift.fwk.rdf.Repository;

/**
 * An abstract class for all of modules, combining default operations and
 * values.
 * 
 * @author T. Colas, T. Marmin
 * @version 090213
 */
public abstract class ModuleModel {

	// -------------------------------------------------------------------------
	// Constants
	// -------------------------------------------------------------------------

	/** Base name of the resource bundle for converter GUI. */
	protected static String GUI_RESOURCES_BUNDLE = ModuleController.GUI_RESOURCES_BUNDLE;

	// -------------------------------------------------------------------------
	// Class members
	// -------------------------------------------------------------------------

	/** Datalift's internal Sesame {@link Repository repository}. **/
	protected static final Repository INTERNAL_REPO = Configuration
			.getDefault().getInternalRepository();
	/** Datalift's internal Sesame {@link Repository repository} URL. */
	protected static final String INTERNAL_URL = INTERNAL_REPO.getEndpointUrl();
	/** Datalift's logging system. */
	protected static final Logger LOG = Logger.getLogger();

	// -------------------------------------------------------------------------
	// Instance members
	// -------------------------------------------------------------------------

	/** The module name. */
	protected final String moduleName;

	// -------------------------------------------------------------------------
	// Constructors
	// -------------------------------------------------------------------------

	/**
	 * Creates a new ModuleModel instance.
	 * 
	 * @param name
	 *            Name of the module.
	 */
	public ModuleModel(String name) {
		this.moduleName = name;
	}

	/**
	 * Resource getter.
	 * 
	 * @param key
	 *            The key to retrieve.
	 * @return The value of key.
	 */
	protected String getTranslatedResource(String key) {
		return PreferredLocales.get()
				.getBundle(GUI_RESOURCES_BUNDLE, ModuleModel.class)
				.getString(key);
	}

	// -------------------------------------------------------------------------
	// Sources management.
	// -------------------------------------------------------------------------

	/**
	 * Checks if a given {@link Source} is valid for our uses.
	 * 
	 * @param src
	 *            The source to check.
	 * @return True if src is {@link TransformedRdfSource} or
	 *         {@link SparqlSource}.
	 */
	protected abstract boolean isValidSource(Source src);

	/**
	 * Checks if a {@link Project proj} contains valid sources for the current
	 * module.
	 * 
	 * @param proj
	 *            The project to check.
	 * @param minValid
	 *            The number of sources we want to have.
	 * @return True if there are more than minValid valid sources.
	 */
	protected final boolean hasMultipleValidSources(Project proj, int minValid) {
		int cpt = 0;
		Iterator<Source> sources = proj.getSources().iterator();

		while (sources.hasNext() && cpt < minValid) {
			if (isValidSource(sources.next())) {
				cpt++;
			}
		}
		return cpt >= minValid;
	}

	/**
	 * Returns all of the URIs (as strings) from the {@link Project project}.
	 * 
	 * @param proj
	 *            The project to use.
	 * @return A LinkedList containing source file's URIs as strings.
	 */
	protected final LinkedList<String> getSourcesURIs(Project proj) {
		LinkedList<String> ret = new LinkedList<String>();

		for (Source src : proj.getSources()) {
			if (isValidSource(src)) {
				ret.add(src.getUri());
			}
		}
		return ret;
	}
}
