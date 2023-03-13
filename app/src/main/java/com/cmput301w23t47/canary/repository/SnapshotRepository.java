package com.cmput301w23t47.canary.repository;

import com.cmput301w23t47.canary.controller.SnapshotController;
import com.cmput301w23t47.canary.model.Snapshot;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;

/**
 * Repository for the snapshot
 */
public class SnapshotRepository {
    @DocumentId
    private String docId;

    private String data;
    private DocumentReference owner;

    // Default Constructor
    public SnapshotRepository() {}

    /**
     * Instantiates a new Snapshot repository.
     *
     * @param data  the data
     * @param owner the owner
     */
// Param constructor
    public SnapshotRepository(String data, DocumentReference owner) {
        this.data = data;
        this.owner = owner;
    }

    /**
     * Gets doc id.
     *
     * @return the doc id
     */
    public String getDocId() {
        return docId;
    }

    /**
     * Sets doc id.
     *
     * @param docId the doc id
     */
    public void setDocId(String docId) {
        this.docId = docId;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public String getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(String data) {
        this.data = data;
    }

    /**
     * Gets owner.
     *
     * @return the owner
     */
    public DocumentReference getOwner() {
        return owner;
    }

    /**
     * Sets owner.
     *
     * @param owner the owner
     */
    public void setOwner(DocumentReference owner) {
        this.owner = owner;
    }

    /**
     * Retrieves the Snapshot object from the repo model
     * @return the Snapshot object
     */
    public Snapshot retrieveSnapshot() {
        return new Snapshot(SnapshotController.getImage(data));
    }
}
