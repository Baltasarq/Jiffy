// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.view;


/** The controller for the tree of locs.
  * @author baltasarq
  */
public class LocTree {
    public LocTree()
    {
        this( new LocTreeView() );
    }
    
    public LocTree(LocTreeView treeView)
    {
        this.treeView = new LocTreeView();
    }
    
    /** @return the view of the loc tree. */
    public LocTreeView getTreeView()
    {
        return this.treeView;
    }
    
    private LocTreeView treeView;
}
