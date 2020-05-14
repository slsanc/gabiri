package slsanc.gabiri.models;

import javax.persistence.*;

@Entity
@Table(name="Documents")
public class Document {

    @Column(name="document_id") @Id @GeneratedValue(strategy = GenerationType.IDENTITY)  private int documentId;
    @Column(name="applicant_id") private int applicantId;
    @Column(name="file_name") private String fileName;
    @Column(name="document_data") @Lob private byte[] documentData;

    public int getDocumentId() {
        return documentId;
    }

    public void setDocumentId(int documentId) {
        this.documentId = documentId;
    }

    public int getApplicantId() {
        return applicantId;
    }

    public void setApplicantId(int applicantId) {
        this.applicantId = applicantId;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getDocumentData() {
        return documentData;
    }

    public void setDocumentData(byte[] documentData) {
        this.documentData = documentData;
    }

    public Document(int applicantId, String fileName, byte[] documentData) {
        this.applicantId = applicantId;
        this.fileName = fileName;
        this.documentData = documentData;
    }

    public Document() {
    }
}
