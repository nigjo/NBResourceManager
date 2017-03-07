package de.nigjo.nbm.rm.core;

import java.util.Collection;
import java.util.Optional;

import java.awt.Image;

import javax.swing.ImageIcon;

import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;

/**
 * A central registration to all known (icon-)resources.
 *
 * @author Jens Hofschröer
 */
public interface ResourceManager
{
  static ResourceManager getDefault()
  {
    // One ResourceManager has to be registered (e.g. via ServiceProvider)
    return Lookup.getDefault().lookup(ResourceManager.class);
  }

  static Image getResouceImage(String resourceID)
  {
    return getDefault().getResourcePath(resourceID)
        .map(path -> ImageUtilities.loadImage(path, true))
        .orElse(null);
  }

  static ImageIcon getResouceImageIcon(String resourceID)
  {
    return getDefault().getResourcePath(resourceID)
        .map(path -> ImageUtilities.loadImageIcon(path, true))
        .orElse(null);
  }

  /**
   * Resolves a resourceID the the actual resource path.
   *
   * @param resId ID of the resource to resolve.
   *
   * @return An Optional with the actual path of the resource if the ID could be resolved.
   * The Optional is empty if the resource is unknown or could not be resolved otherwise.
   */
  Optional<String> getResourcePath(String resId);

  /**
   * Returns a collection of all currently known resource IDs.
   *
   * @return All known resourceIDs or an empty collection.
   */
  Collection<String> getResourceIds();

}
