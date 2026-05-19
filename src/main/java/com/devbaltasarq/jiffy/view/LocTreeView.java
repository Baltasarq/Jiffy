// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.view;


import java.awt.Font;
import java.awt.BorderLayout;
import javax.swing.JTree;
import javax.swing.JPanel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


/** The view of the tree of locs.
  * @author baltasarq
  */
public class LocTreeView extends JPanel {
    public LocTreeView()
    {
        this( null );
    }
    
    public LocTreeView(Font font)
    {
        final var LAY = new BorderLayout();
        this.root = new DefaultMutableTreeNode( "Story" );
        final var MODEL = new DefaultTreeModel( this.root );
        
        if ( font == null ) {
            font = new Font( Font.SANS_SERIF, Font.PLAIN, 12 );
        }
        
        LAY.setHgap( 10 );
        LAY.setVgap( 10 );
        this.setLayout( LAY );
        
        this.tree = new JTree();
        this.tree.setFont( font );
        this.tree.setModel( MODEL );
        this.add( this.tree, BorderLayout.CENTER );
    }
    
    public void add(LocPosition pos)
    {
        this.root.add( new DefaultMutableTreeNode( pos ) );
    }
    
    /** @return the root node. */
    public DefaultMutableTreeNode getRoot()
    {
        return this.root;
    }
    
    /**@return the tree. */
    public JTree getTree()
    {
        return this.tree;
    }
    
    private final JTree tree;
    private final DefaultMutableTreeNode root;
}
