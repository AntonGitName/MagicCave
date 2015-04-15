package edu.amd.spbstu.magiccave.interfaces;

/**
 * @author iAnton
 * @since 15/04/15
 */
public interface OnCandleViewClickListener {
    void onCandleViewClick(int id, boolean needCheck);

    boolean isConnecting();
}
