package com.equalize.model.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single invalid CSV record encountered during loading/validation.
 * Stores the row number, the raw line (optional), and one or more error messages.
 */
public class InvalidRow {

    private int rowNumber;
    private String rawLine;
    private List<String> errorMessages = new ArrayList<>();

    public InvalidRow(int rowNumber, String rawLine) {
        this.rowNumber = rowNumber;
        this.rawLine = rawLine;
    }

    public InvalidRow(int rowNumber, String rawLine, List<String> errorMessages) {
        this.rowNumber = rowNumber;
        this.rawLine = rawLine;
        if (errorMessages != null) {
            this.errorMessages = errorMessages;
        }
    }

    // --- Getters & Setters ---

    public int getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }

    public String getRawLine() {
        return rawLine;
    }

    public void setRawLine(String rawLine) {
        this.rawLine = rawLine;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void setErrorMessages(List<String> errorMessages) {
        this.errorMessages = errorMessages;
    }

    // --- Convenience methods ---

    public void addErrorMessage(String message) {
        if (message != null && !message.isBlank()) {
            this.errorMessages.add(message);
        }
    }

    @Override
    public String toString() {
        return "InvalidRow{" +
                "rowNumber=" + rowNumber +
                ", rawLine='" + rawLine + '\'' +
                ", errorMessages=" + errorMessages +
                '}';
    }
}
