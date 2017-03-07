/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.nigjo.nbm.rm.debugui;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

import org.netbeans.swing.outline.DefaultOutlineCellRenderer;

import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.view.OutlineView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.BeanNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.NbBundle.Messages;

import de.nigjo.nbm.rm.core.ResourceManager;
import de.nigjo.nbm.rm.core.ResourceRegistration;

@ActionID(
    category = "Tools",
    id = "de.nigjo.nbm.rm.debugui.IconCollectionAction"
)
@ActionRegistration(
    iconBase = "de/nigjo/nbm/rm/debugui/images.png",
    displayName = "#CTL_IconCollectionAction"
)
@ActionReference(path = "Menu/Tools", position = 1020)
@Messages("CTL_IconCollectionAction=Display all Icon Resources")
public final class IconCollectionAction implements ActionListener
{
  @ResourceRegistration(resource = "images.png")
  public static final String TEST = "de.nigjo.nbm.rm.debugui.action";

  @Override
  public void actionPerformed(ActionEvent e)
  {
    OutlineView view = new OutlineView("ID");
    view.addPropertyColumn("nameIcon", "Icon");
    view.addPropertyColumn("resoucePath", "Path");
    view.setShowNodeIcons(false);
    view.setQuickSearchAllowed(true);
    //view.set
    view.getOutline().setRootVisible(false);
    view.getOutline().setDefaultRenderer(Node.Property.class, new ImageRenderer());

    class EMWrapper extends JPanel implements ExplorerManager.Provider
    {
      private static final long serialVersionUID = -5855257582611241156L;
      private final ExplorerManager em;

      public EMWrapper()
      {
        super(new BorderLayout());
        this.em = new ExplorerManager();
      }

      @Override
      public ExplorerManager getExplorerManager()
      {
        return em;
      }

    }

    EMWrapper wrapper = new EMWrapper();
    wrapper.add(view);
    wrapper.getExplorerManager().setRootContext(new AbstractNode(
        Children.create(new ResourceEntryFactory(), false)));

    wrapper.setPreferredSize(new java.awt.Dimension(800, 600));

    DialogDisplayer.getDefault().notify(
        new NotifyDescriptor.Message(wrapper));
  }
  
  private static class ResourceEntryFactory extends ChildFactory<String>
  {
    @Override
    protected boolean createKeys(List<String> toPopulate)
    {
      Collection<String> resourceIds = ResourceManager.getDefault().getResourceIds();
      toPopulate.addAll(resourceIds);
      return true;
    }

    @Override
    protected Node createNodeForKey(String key)
    {
      try
      {
        return new BeanNode<>(new ResourceEntry(key));
      }
      catch(IntrospectionException ex)
      {
        return Node.EMPTY;
      }
    }

  }

  private static class ImageRenderer extends DefaultOutlineCellRenderer
  {
    private static final long serialVersionUID = 8765297459152368673L;

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
        boolean isSelected, boolean hasFocus, int row, int column)
    {
      Object displayValue = value;
      try
      {
        if(value instanceof Node.Property)
        {
          displayValue = ((Node.Property<? extends Object>)value).getValue();
        }
      }
      catch(IllegalAccessException | InvocationTargetException ex)
      {
        //displayValue = value;
      }

      if(displayValue instanceof ImageIcon)
      {
        return new JLabel((ImageIcon)displayValue);
      }
      else
      {
        Class<?> colClass = displayValue != null
            ? displayValue.getClass()
            : table.getColumnClass(column);
        TableCellRenderer renderer =
            table.getDefaultRenderer(colClass);
        if(renderer != null && renderer != this)
        {
          return renderer.getTableCellRendererComponent(table,
              displayValue, isSelected, hasFocus, row, column);
        }
      }
      return super.getTableCellRendererComponent(
          table, value, isSelected, hasFocus, row, column);
    }

  }
}
