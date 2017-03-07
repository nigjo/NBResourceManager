package de.nigjo.nbm.rm.core;

import java.util.Collection;
import java.util.Optional;

import org.openide.util.Lookup;

/**
 * A central registration to all known (icon-)resources.
 *
 * @todo Hier fehlt die Beschreibung der Klasse.
 *
 * @author Jens Hofschröer
 */
public interface ResourceManager
{
  public static ResourceManager getDefault()
  {
    // One ResourceManager has to be registered (e.g. via ServiceProvider)
    return Lookup.getDefault().lookup(ResourceManager.class);
  }

  /**
   * Resolves a resourceID the the actual resource path.
   *
   * @param resId ID of the resource to resolve.
   *
   * @return An Optional with the actual path of the resource if the ID could be resolved.
   * The Optional is empty if the resource is unknown or could not be resolved otherwise.
   */
  public Optional<String> getResourcePath(String resId);

  /**
   * Returns a collection of all currently known resource IDs.
   * 
   * @return All known resourceIDs or an empty collection.
   */
  public Collection<String> getResourceIds();

}
