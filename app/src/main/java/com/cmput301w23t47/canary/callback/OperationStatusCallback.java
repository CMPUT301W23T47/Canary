package com.cmput301w23t47.canary.callback;

/**
 * Callback interface for informing the caller about the status of the operation
 */
public interface OperationStatusCallback {
    /**
     * Operation status.
     *
     * @param status the status
     */
    public void operationStatus(boolean status);
}
