package br.com.alugamais.web.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.NumberFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "pix")
public class Pix extends AbstractEntity<Long> {

    @NotNull
    private String transactionId;

    @Lob
    @NotNull
    private String qrCodeKey;

    @NotNull
    private String copyPasteKey;

    @NotNull
    private String contract;

    @NotNull
    private String lessor;

    @NotNull
    private String lessee ;

    @NotNull
    private String parcel;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    @Column(nullable = false, columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
    private BigDecimal amount;

    @NotNull
    private String situation;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "date_create", nullable = false, columnDefinition = "DATE")
    private LocalDate dateCreate;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "date_expiration", nullable = false, columnDefinition = "DATE")
    private LocalDate dateExpiration;

    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name = "due_date", nullable = false, columnDefinition = "DATE")
    private LocalDate dueDate;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    @Column(nullable = false, columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
    private BigDecimal discount;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    @Column(nullable = false, columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
    private BigDecimal interestValue;

    @NotNull
    @NumberFormat(style = NumberFormat.Style.CURRENCY, pattern = "#,##0.00")
    @Column(nullable = false, columnDefinition = "DECIMAL(20,2) DEFAULT 0.00")
    private BigDecimal feeValue;

    @NotNull
    @Size(max = 500, min = 1)
    @Column(name="type_of_payment", nullable = false, unique = false)
    private String typeOfPayment;

    @NotNull
    @Size(max = 100, min = 1)
    @Column(name="type_of_document", nullable = false, unique = false)
    private String typeOfDocument;

    @NotNull
    @Size(max = 100, min = 1)
    @Column(name="payment_method")
    private String paymentMethod;

    @NotNull
    @Column(nullable = false)
    private String user;

}
