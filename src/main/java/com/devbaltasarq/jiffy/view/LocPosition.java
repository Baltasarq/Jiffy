// Jiffy (c) 2023 Baltasar MIT License <baltasarq@gmail.com>


package com.devbaltasarq.jiffy.view;


/** A position of a given loc inside the document.
  * @author baltasarq
  */
public record LocPosition(int pos, String locName) {
    @Override
    public String toString()
    {
        return this.locName();
    }
}
