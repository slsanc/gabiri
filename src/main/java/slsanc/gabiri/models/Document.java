package slsanc.gabiri.models;

import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Table(name="Documents")
public class Document {

    @Column(name="document_id") @Id @GeneratedValue(strategy = GenerationType.IDENTITY)  private int documentId;
    @Column(name="applicant_id") private int applicantId;
    @Column(name="document") @Lob private byte[] document;

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

    public byte[] getDocument() {
        return document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public Document(int applicantId, byte[] document) {
        this.applicantId = applicantId;
        this.document = document;
    }

    public Document() {
    }
}
