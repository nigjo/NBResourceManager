package de.nigjo.nbm.rm.core;

import java.util.Collection;
import java.util.Optional;

import org.openide.util.Lookup;

/**
 * Eine neue Klasse von hof. Erstellt Mar 6, 2017, 3:55:58 PM.
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

  public Optional<String> getResourcePath(String resId);

  public Collection<String> getResourceIds();

}
