package org.docear.plugin.bibtex.actions;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JViewport;

import net.sf.jabref.BibtexEntry;
import net.sf.jabref.gui.MainTable;

import org.docear.plugin.bibtex.ReferencesController;
import org.freeplane.core.ui.AFreeplaneAction;
import org.freeplane.core.ui.EnabledAction;
import org.freeplane.features.map.NodeModel;
import org.freeplane.features.mode.Controller;

@EnabledAction(checkOnPopup = true)
public class ShowInReferenceManagerAction extends AFreeplaneAction {
	
	private static final long serialVersionUID = 1L;
	public static final String KEY = "ShowInRefManagerAction";
	
	
	public ShowInReferenceManagerAction() {
		super(KEY);
	}
	
	public void setEnabled() {
		NodeModel node = Controller.getCurrentModeController().getMapController().getSelectedNode();
		if (node == null) {
			setEnabled(false);
			return;
		}
		final String bibtexKey = ReferencesController.getController().getJabRefAttributes().getBibtexKey(node);
		
		if (bibtexKey != null && bibtexKey.length()>0) {
			setEnabled(true);
		}
		else {
			setEnabled(false);
		}
		
	}
	
	
	public void actionPerformed(ActionEvent e) {
		NodeModel node = Controller.getCurrentModeController().getMapController().getSelectedNode();
		if (node == null) {
			return;
		}
		if(ReferencesController.getController().getJabrefWrapper().getBasePanel().getSelectedEntries().length <= 1) {
			final String bibtexKey = ReferencesController.getController().getJabRefAttributes().getBibtexKey(node);			
			showInReferenceManager(bibtexKey);		
		}
		
	}
	
	public static void showInReferenceManager(String bibtexKey) {
		if (bibtexKey != null && bibtexKey.length()>0) {
			
			BibtexEntry referenceEntry = ReferencesController.getController().getJabrefWrapper().getDatabase().getEntryByKey(bibtexKey);
			MainTable table = ReferencesController.getController().getJabrefWrapper().getBasePanel().getMainTable();
			
			List<BibtexEntry> list = table.getTableRows();
			int viewHeight = table.getPane().getHeight()-table.getTableHeader().getHeight();
			Rectangle viewRect = new Rectangle(0,((JViewport)table.getParent()).getViewPosition().y, 4, viewHeight);
			int pos = 0;
			Rectangle rowArea = new Rectangle(); 
			for(BibtexEntry row : list) {
				if(row.equals(referenceEntry)) {
					rowArea.setBounds(0, (table.getRowHeight()*pos), 2, table.getRowHeight());					
					table.clearSelection();
					table.addRowSelectionInterval(pos,pos);
					if(isRowOutsideViewArea(viewRect, rowArea)) {
						((JViewport)table.getParent()).setViewPosition(rowArea.getLocation());
					}
					break;
				}
				pos++;
			}
		}
	}
	
	private static boolean isRowOutsideViewArea(final Rectangle viewArea, final Rectangle row) {
		if(viewArea.contains(row)) {
			return false;
		}
		return true;
	}

}
