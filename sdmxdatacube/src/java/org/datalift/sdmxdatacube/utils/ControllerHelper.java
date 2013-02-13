package org.datalift.sdmxdatacube.utils;

import org.datalift.fwk.project.Project;
import org.datalift.fwk.project.Source;
import org.datalift.sdmxdatacube.SDMXDataCubeModel;

/**
 * Helper methods to make the relationship between the main controller 
 * and the main view easier.
 * 
 * @author tcolas
 * @date 130213
 */
public class ControllerHelper {
	
	private SDMXDataCubeModel model;
	
	private static final String SDMX_SUFFIX = "sdmx";
	private static final String DATACUBE_SUFFIX = "qb";

	public ControllerHelper(SDMXDataCubeModel model) {
		this.model = model;
	}

	/**
	 * Uses a SDMXDataCubeModel to tell if a given source is valid SDMX.
	 * @param parent A source which might be converted to DataCube.
	 * @return True if the model says the source is valid.
	 */
	public boolean isValidSource(Source parent) {
		return model.isValidSource(parent);
	}
	
	/**
	 * Generates a new source title from an existing one.
	 * @param parent The parent source for this potential one.
	 * @return A title which is not guaranteed to be unique.
	 */
	public String generateSourceTitle(Source parent) {
		String oldTitle = parent.getTitle();
		String newTitle;
		if (oldTitle.contains(SDMX_SUFFIX)) {
			// Reuse the previous formatting if it differs from the "content-format" pattern.
			newTitle = oldTitle.replace(SDMX_SUFFIX, DATACUBE_SUFFIX);
		}
		else {
			newTitle = oldTitle + "-" + DATACUBE_SUFFIX;
		}
		
		return newTitle;
	}
	
	/**
	 * Generates a new source URI from an existing one.
	 * @param parent The parent source for this potential one.
	 * @return A URI which is guaranteed to be unique.
	 */
	public String generateSourceURI(Source parent) {
		String oldURI = parent.getUri();
		String newURI;
		if (oldURI.contains(SDMX_SUFFIX)) {
			// Reuse the previous formatting if it differs from the "content-format" pattern.
			newURI = oldURI.replace(SDMX_SUFFIX, DATACUBE_SUFFIX);
		}
		else {
			newURI = oldURI + "-" + DATACUBE_SUFFIX;
		}
		
		// If this URI already exists, we'll add a number at its end.
		if (parent.getProject().getSource(newURI) != null) {
			newURI = generateNumberedURI(parent.getProject(), newURI);
		}
		
		return newURI;
	}
	
	/**
	 * Adds a number at the end of a URI if the unnumbered version already exists.
	 * Assumes that there will never be more than 100 "same" URIs.
	 * @param parentProject Parent project of the sources.
	 * @param newURI The unnumbered URI.
	 * @return A URI with a number at the end.
	 */
	private static String generateNumberedURI(Project parentProject, String newURI) {
		int cpt = 1;
		boolean alreadyExists;
		String numberedURI;
		do {
			cpt++;
			numberedURI = newURI + "-" + cpt;
			alreadyExists = parentProject.getSource(numberedURI) != null;
		} while (alreadyExists && cpt < 100);
		
		return numberedURI;
	}
}
