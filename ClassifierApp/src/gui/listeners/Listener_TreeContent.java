package gui.listeners;

import gui.TutorialWindow;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;

public class Listener_TreeContent implements TreeSelectionListener {
	private TutorialWindow w;
	private JTree tree;

	public Listener_TreeContent(TutorialWindow w, JTree tree) {
		this.w = w;
		this.tree = tree;
	}

	@Override
	public void valueChanged(TreeSelectionEvent e) {
		// TODO Auto-generated method stub
		DefaultMutableTreeNode node = (DefaultMutableTreeNode)
				tree.getLastSelectedPathComponent();

		/* if nothing is selected */ 
		if (node == null) return;

		/* retrieve the node that was selected */ 
		Object nodeInfo = node.getUserObject();
		w.addToMainPanel(nodeInfo.toString());
	}
}
